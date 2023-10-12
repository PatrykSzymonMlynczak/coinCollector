package com.example.demo.it;

import com.example.demo.businessLogic.person.exception.PersonAlreadyExistsException;
import com.example.demo.businessLogic.person.exception.PersonNotExistsException;
import com.example.demo.businessLogic.product.exception.NotEnoughSortException;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:database/fixCreatedSchema.sql")
@AutoConfigureMockMvc
public class CoinCollectorIntegrationTest {

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
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
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
        assertThat(exception.getMessage()).isEqualTo("You can't sell it cause You have not enough product:Standard lack: 1.0");

        //WHEN
        saleController.addSale(productNameStandard, 12F, person, null);

        //should erase product
        ProductEntity product = productRepoPostgres.getByNameIgnoreCase("standard");
        assertThat(product.getTotalSortAmount()).isEqualTo(0);
        assertThat(product.getEraseDate()).isEqualTo(LocalDate.now());

    }

    @Test
    public void scenarioWithAddSaleIgnoringSurplus() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
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
    public void scenarioWithAddSaleNOTIgnoringSurplus() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        saleController.addSaleAndNotIgnoreSurplus(productNameStandard, 12.86F, person, null);

        //quantity should not be rounded :
        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(87.14F);


        //should calculate properly earnings
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(154.32f);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(514.4f);
        assertThat(saleRepoPostgres.getTotalCost()).isWithin(0.009f).of(360.08f);

    }

    @Test
    public void shouldAddManualSale() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        saleController.addManualSale(productNameStandard, 12.86F, 480F, 480F, person, null);

        //quantity should not be rounded :
        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(87.14F);


        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(119.92f);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(480f);
        assertThat(saleRepoPostgres.getTotalCost()).isWithin(0.009f).of(360.08f);

    }

    @Test
    public void shouldReturnCorrectPriceAndNotAddSale() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        Float calculatedPrice = saleController.priceCheckout(productNameStandard, 12.86F);
        assertThat(calculatedPrice).isWithin(0.1f).of(514.4f);

        //quantity should not be rounded :
        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(100F);

        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(null);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(null);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(null);
    }

    @Test
    void shouldIncreaseDebtWhileGivenMoneyAreLess() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebt(productNameStandard, 5f, 150f, person, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-50);

        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(95f);

        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(10);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(150);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(140);
    }

    @Test
    void shouldIncreaseDebtAndCalculateDiscountWhileGivenMoneyAreLessAndDiscountIsGiven() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebtAndDiscount(productNameStandard, 5f, 150f, person, 10f, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-40);

        ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productNameStandard);
        assertThat(productEntity.getTotalSortAmount()).isEqualTo(95f);

        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(10);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(150);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(140);
    }

    @Test
    void shouldPayDebtAndIncreaseIncomeAndEarnings() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebt(productNameStandard, 5f, 160f, person, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-40);
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(20);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(160);


        personController.payDebt(30f, "ada");
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-10);
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(50);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(190);
    }

    @Test
    void testGetSalesAndDeleteSales() {
        String person = "Ada";
        personController.savePerson(person);

        personController.savePerson("person");

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        saleController.addSale(productNameStandard, 20F, "person", null);
        saleController.addSaleWithDebt(productNameStandard, 10F, 100F, person, null);
        personController.payDebt(100f, person);

        List<SaleDto> sales = saleController.getAllSales();
        assertThat(sales).hasSize(3);

        List<SaleDto> salesWithoutDebt = saleController.getSalesWithoutDebtPayments();
        assertThat(salesWithoutDebt).hasSize(2);

        List<SaleDto> salesByName = saleController.getByName("person");
        assertThat(salesByName).hasSize(1);

        assertThat(saleController.getLastSale().getProduct().getName()).isEqualTo("PAY_DEBT");
        assertThat(saleController.getLastSale().getEarned()).isEqualTo(100F);

        //when
        saleController.deleteLastSale();

        assertThat(saleController.getLastSale().getProduct().getName()).isEqualTo("Standard");
        assertThat(saleController.getLastSale().getEarned()).isEqualTo(-180f);

        System.out.println(saleController.getAllSales());


        //when
        saleController.deleteSaleById(saleController.getLastSale().getId());

        assertThat(saleController.getLastSale().getProduct().getName()).isEqualTo("Standard");
        assertThat(saleController.getLastSale().getEarned()).isEqualTo(240f);

        //when
        saleController.clearAllSales();

        assertThat(saleController.getAllSales()).hasSize(0);
    }

    @Test
    void testGetMethods() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);
        productController.addNewProduct("another", 28F, 100F, priceMapStandard);


        //WHEN
        saleController.addManualSale(productNameStandard, 12F, 480F, 480F, person, null);
        saleController.addSale(productNameStandard, 2F, person, null);
        saleController.addSale("another", 1F, person, null);


        assertThat(saleController.getIncome()).isEqualTo(580 + 50);
        assertThat(saleController.getTotalCost()).isEqualTo(392 + 28);
        assertThat(saleController.getEarnedMoneyByDay(null)).isEqualTo(188 + 22);
        assertThat(saleController.getTotalEarnings()).isEqualTo(188 + 22);

        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(188 + 22);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(580 + 50);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(392 + 28);

        assertThat(saleController.getEarningsWithoutSpecifiedProductName("another")).isEqualTo(188);
        assertThat(saleController.getEarningsWithSpecifiedProductName("another")).isEqualTo(22);
    }

    @Test
    void testEarningByPeriods() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 0F, 100F, priceMapStandard);

        //WHEN
        saleController.addSale(productNameStandard, 1F, person, "2023-09-01");
        saleController.addSale(productNameStandard, 1F, person, "2023-09-02");
        saleController.addSale(productNameStandard, 1F, person, "2023-09-03");
        saleController.addSale(productNameStandard, 1F, person, "2023-10-01");
        saleController.addSale(productNameStandard, 1F, person, "2023-10-02");

        assertThat(saleController.getEarnedMoneyByDay("2023-09-01")).isEqualTo(50);
        assertThat(saleController.getEarnedMoneyByWeek("2023-09-01", null)).isEqualTo(250);
        assertThat(saleController.getEarnedMoneyByWeek("1113-09-01", "4444-09-01")).isEqualTo(250);
        assertThat(saleController.getEarnedMoneyByWeek("2023-09-01", "2023-09-03")).isEqualTo(150);
    }

    @Test
    void tesSalesByPeriods() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 0F, 100F, priceMapStandard);

        //WHEN
        saleController.addSale(productNameStandard, 1F, person, "2023-09-01");
        saleController.addSale(productNameStandard, 1F, person, "2023-09-02");
        saleController.addSale(productNameStandard, 1F, person, "2023-09-03");
        saleController.addSale(productNameStandard, 1F, person, "2023-10-01");
        saleController.addSale(productNameStandard, 1F, person, "2023-10-02");

        assertThat(saleController.getSalesByPeriod("2023-09-01")).hasSize(1);
        assertThat(saleController.getSalesByPeriod("2023-09-01", null)).hasSize(5);
        assertThat(saleController.getSalesByPeriod("1113-09-01", "4444-09-01")).hasSize(5);
        assertThat(saleController.getSalesByPeriod("2023-09-01", "2023-09-03")).hasSize(3);
    }

    @Test
    void shouldCalculateDiscount() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDiscount(productNameStandard, 5f, person, 10f, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(0);

        //should calculate properly earnings taking care of "gratis rests"
        assertThat(saleRepoPostgres.getEarnedMoneyByDay(LocalDate.now())).isEqualTo(50);
        assertThat(saleRepoPostgres.getTotalIncome()).isEqualTo(190);
        assertThat(saleRepoPostgres.getTotalCost()).isEqualTo(140);
    }

    @Test
    void testProducts() {
        String productNameStandard = "Standard";
        String second = "second";
        String third = "third";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);
        productController.addNewProduct(second, 28F, 100F, priceMapStandard);
        productController.addNewProduct(third, 28F, 100F, priceMapStandard);

        assertThat(productController.getAllProducts()).hasSize(3);

        productController.eraseRestOfProduct(third);

        assertThat(productController.getTotalAmount(third)).isEqualTo(0);
        assertThat(productRepoPostgres.getTotalAmount(third)).isEqualTo(0);

        assertThat(productRepoPostgres.getByNameIgnoreCase(third).getEraseDate()).isEquivalentAccordingToCompareTo(LocalDate.now());
    }

    @Test
    void shouldThrowExceptionWhileDeletingSoldProduct() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        String second = "second";
        String third = "third";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);

        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);
        productController.addNewProduct(second, 28F, 100F, priceMapStandard);
        productController.addNewProduct(third, 28F, 100F, priceMapStandard);

        saleController.addSale(third, 10f, person, null);

        assertThrows(DataIntegrityViolationException.class, () -> productController.deleteProduct(third, LocalDate.now().toString()));
    }

    @Test
    void shouldPayDebtAndGatDebt() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebt(productNameStandard, 5f, 150f, person, null);
        saleController.addSaleWithDebt(productNameStandard, 5f, 150f, person, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-100);

        assertThat(personController.getDebts()).hasSize(1);
        assertThat(personController.getDebts().get(0).getDebt()).isEqualTo(-100);
        assertThat(personController.getDebts().get(0).getName()).isEqualTo(person);

        personController.payDebt(50f, person);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-50);

        personController.payDebt(100f, person);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(50);
    }

    @Test
    void shouldSetDebtWithoutChangingEarnings() {
        String person = "Ada";
        personController.savePerson(person);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebt(productNameStandard, 10f, 280f, person, null);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(-120);

        personController.setDebt(0f, person);
        assertThat(personRepoPostgres.findByNameIgnoreCase(person).getDebt()).isEqualTo(0);
        assertThat(saleController.getTotalEarnings()).isEqualTo(0);
        assertThat(saleController.getIncome()).isEqualTo(280);
        assertThat(saleController.getTotalCost()).isEqualTo(280);
    }

    @Test
    void shouldGetAllPersons() {
        String person = "Ada";
        personController.savePerson(person);
        String person2 = "Ada2";
        personController.savePerson(person2);
        String person3 = "Ada3";
        personController.savePerson(person3);

        assertThat(personController.getAllPersons()).hasSize(3);
    }


    @Test
    void shouldCollectDebt() {
        String person = "Ada";
        personController.savePerson(person);
        String person2 = "Ada2";
        personController.savePerson(person2);
        String person3 = "Ada3";
        personController.savePerson(person3);

        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);
        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        saleController.addSaleWithDebt(productNameStandard, 5f, 150f, person, null);
        saleController.addSaleWithDebt(productNameStandard, 5f, 100f, person2, null);
        saleController.addSaleWithDebt(productNameStandard, 5f, 50f, person3, null);

        assertThat(personController.collectDebt()).isEqualTo(-300);
    }


    @Test
    public void should_throw_product_not_exist_exception() {
        String person = "Ada";
        personController.savePerson(person);
        personController.savePerson("person");

        //WHEN
        Exception exception = assertThrows(ProductNotExistException.class, () ->
                saleController.addSale("productNameStandard", 12.86F, person, null)
        );
        assertThat(exception.getMessage()).isEqualTo("You can't add sale because product not exists: productNameStandard");
    }


    @Test
    public void should_throw_person_already_exist_exception() {
        String person = "Ada";
        personController.savePerson(person);

        //WHEN
        Exception exception = assertThrows(PersonAlreadyExistsException.class, () ->
                personController.savePerson(person)
        );
        assertThat(exception.getMessage()).isEqualTo("You can't add person because already exists: Ada");
    }

    @Test
    public void should_throw_person_not_exist_exception() {
        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);

        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        //WHEN
        Exception exception = assertThrows(PersonNotExistsException.class, () ->
                saleController.addSale("productNameStandard", 12.86F, "person", null)
        );

        assertThat(exception.getMessage()).isEqualTo("You can't add sale because person not exists: person");
    }

    @Test
    public void should_throw_product_already_exists_exception() throws Exception {
        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);

        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        assertThrows(ProductAlreadyExistsException.class, () ->
                productController.addNewProduct(productNameStandard, 30F, 100F, priceMapStandard)
        );
    }

    @Test
    public void shouldAddProductWithTheSameNameButDifferentDate() {
        String productNameStandard = "Standard";
        TreeMap<Float, Float> priceMapStandard = new TreeMap<>();
        priceMapStandard.put(1F, 50F);
        priceMapStandard.put(5F, 40F);
        priceMapStandard.put(10F, 40F);

        productRepoPostgres.save(
                new ProductEntity(null,
                        productNameStandard,
                        priceMapStandard,
                        28f,
                        100f,
                        LocalDate.of(2023, 10, 11),
                        null)
        );

        productController.addNewProduct(productNameStandard, 28F, 100F, priceMapStandard);

        assertThat(productRepoPostgres.findAll()).hasSize(2);
    }

}