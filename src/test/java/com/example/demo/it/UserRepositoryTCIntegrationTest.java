package com.example.demo.it;

import com.example.demo.dto.SaleDto;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.TreeMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
//contextConfiguration as alternative or dynamic
@Sql(scripts = "classpath:populateDb/insertData.sql")
@AutoConfigureMockMvc
public class UserRepositoryTCIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> String.format("jdbc:tc:postgresql://localhost:%s/%s", postgreSQLContainer.getFirstMappedPort(), postgreSQLContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
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


    @BeforeEach
    public void setupProduct() throws Exception {

        postgreSQLContainer.start();

        TreeMap<Float, Float> map = new TreeMap<>();
        map.put(1F, 5F);
        String json = objectMapper.writeValueAsString(map);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/product/{productName}/{myPrice}", "test", "10")
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @AfterEach
    public void clearDB() {
        personRepoPostgres.deleteAll();
        productRepoPostgres.deleteAll();
        saleRepoPostgres.deleteAll();
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