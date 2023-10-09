package com.example.demo.it;

import com.example.demo.businessLogic.product.exception.NotEnoughSortException;
import com.example.demo.controller.PersonController;
import com.example.demo.controller.ProductController;
import com.example.demo.controller.SaleController;
import com.example.demo.dto.SaleDto;
import com.example.demo.postgres.entity.ProductEntity;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.TreeMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:database/fixCreatedSchema.sql")
@AutoConfigureMockMvc
public class UserRepositoryTCIntegrationTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6.12")
            .withDatabaseName("db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> String.format("jdbc:tc:postgresql://localhost:%s/%s", postgreSQLContainer.getFirstMappedPort(), postgreSQLContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PersonRepoPostgres personRepoPostgres;

    @Autowired
    ProductRepoPostgres productRepoPostgres;

    @Autowired
    SaleRepoPostgres saleRepoPostgres;


    @Autowired
    SaleController saleController;

    @Autowired
    ProductController productController;

    @Autowired
    PersonController personController;


    @AfterEach
    public void clearDB() {
        saleRepoPostgres.deleteAll();
        personRepoPostgres.deleteAll();
        productRepoPostgres.deleteAll();
    }


    @Test
    public void scenarioWithStandardAddSale() {
        String productNameStandard = "Standard";
        String person = "Ada";
        personController.savePerson(person);
        personController.savePerson("person");

        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        saleController.addSale(productNameStandard, 88F, person, null);

        //should decrease amount
        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(12);

        //should calcualte money
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(3520 - 2464);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(3520);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(2464);


        //should throw exception and calculate lack
        NotEnoughSortException exception = assertThrows(NotEnoughSortException.class, () -> {
            saleController.addSale(productNameStandard, 13F, person, null);
        });
        assertThat(exception.getMessage()).isEqualTo("You can't sell it cause You have not enough sort:Standard lack: 1.0");

        //WHEN
        saleController.addSale(productNameStandard, 12F, person, null);

        //should erase product
        ProductEntity product = productRepoPostgres.getByNameIgnoreCase("standard");
        assertThat(product.getTotalSortAmount()).isEqualTo(0);
        assertThat(product.getEraseDate()).isEqualTo(LocalDate.now());

    }

    @Test
    public void scenarioWithAddSaleIgnoringSurplus() {
        String productNameStandard = "Standard";
        String person = "Ada";
        personController.savePerson(person);
        personController.savePerson("person");

        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        saleController.addSale(productNameStandard, 12.86F, person, null);

        //quantity should not be rounded :
        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(87.14F);


        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(119.92f);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(480f);
        assertThat(saleRepoPostgres.getTotalCost()).isWithin(0.009f).of(360.08f);

    }

    @Test
    public void should_return_sale_after_sale_GET() throws Exception {
        //given
        mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                        "test", "10", "1", "Zamor", null));
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/sale/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto[] sales = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto[].class);

        //then
        Assertions.assertEquals("Zamor", sales[0].getPerson().getName());
        Assertions.assertEquals(1F, sales[0].getQuantity());
    }

    @Test
    public void should_return_proper_values_after_sale_POST() throws Exception {

        //when

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/person/{personName}",
                                "Zamor"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                "test", "10", "1", "Zamor", null))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        //then
        SaleDto sale = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto.class);
        Assertions.assertEquals("Zamor", sale.getPerson().getName());
        Assertions.assertEquals(1, sale.getQuantity());
        Assertions.assertEquals(10, sale.getProduct().getMyPrice());
    }

/*    @Test
    public void should_save_sale_to_file_E2E() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                "test",      "10",      "1",        "Zamor",    null))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto newSale = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto.class);

        //then
        List<Sale> salesFromFile = jsonFileManager.readSaleListFromFile();
        Assertions.assertEquals(salesFromFile.get(0).getPerson().getName(),newSale.getPerson().getName());
        Assertions.assertEquals(salesFromFile.get(0).getTransactionDate(),newSale.getTransactionDate());
    }*/

    @Test
    public void should_throw_sort_not_exist_exception() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                "test", "1", "1", "Zamor", null))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
        String actualMessage = mvcResult.getResolvedException().getMessage();

        //then
        Assertions.assertEquals("You can't add sale because Sort not exists: " + "test" + " price: " + "1.0", actualMessage);

    }

    @Test
    public void should_throw_sort_already_exists_exception() throws Exception {
        //given
        TreeMap<Float, Float> map = new TreeMap<>();
        map.put(1F, 5F);
        String json = objectMapper.writeValueAsString(map);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/product/{productName}/{myPrice}", "test", "10")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()))
                .andReturn();
        String actualMessage = mvcResult.getResolvedException().getMessage();

        //then
        Assertions.assertEquals("You can't add sort pricing because it already exists: " + "test" + " price: " + "10.0",
                actualMessage);

    }

}