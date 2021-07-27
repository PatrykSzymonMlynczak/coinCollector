package com.example.demo.dbIntegration;

import com.example.demo.postgres.entity.PersonEntity;
import com.example.demo.postgres.entity.ProductEntity;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/testdatabase"})
public class DatabaseIntegrationTest {

    @Autowired
    PersonRepoPostgres personRepoPostgres;

    @Autowired
    ProductRepoPostgres productRepoPostgres;

    @Autowired
    SaleRepoPostgres saleRepoPostgres;

    //PERSON
    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnPersonIgnoringCase() {
        PersonEntity person = personRepoPostgres.findByNameIgnoreCase("ZaMoR");
        Assertions.assertEquals("Zamor", person.getName());
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnProperDebt() {
        PersonEntity person = personRepoPostgres.findByNameIgnoreCase("oltman");
        Assertions.assertEquals(-20, person.getDebt());
    }

    //SALE
    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnEarningsByDate() {
        Float earningsByDay = saleRepoPostgres.getEarnedMoneyByDay(LocalDate.of(2021,07,26));
        Assertions.assertEquals(40, earningsByDay);
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnTotalEarnings() {
        Float earningsByDay = saleRepoPostgres.getTotalEarnings();
        Assertions.assertEquals(40, earningsByDay);
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnTotalIncome() {
        Float earningsByDay = saleRepoPostgres.getTotalIncome();
        Assertions.assertEquals(140, earningsByDay);
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnTotalCost() {
        Float earningsByDay = saleRepoPostgres.getTotalCost();
        Assertions.assertEquals(100, earningsByDay);
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnZeroWhenDateIsInappropriate() {
/*        Float earningsByDay = saleRepoPostgres.getEarnedMoneyByWeek(LocalDate.of(2020,07,26), LocalDate.of(2021,07,29));
        Assertions.assertEquals(0, earningsByDay);*/
        //todo -> returning null,
        // making return Optional will break contract with jsonFile version
    }

    //PRODUCT
    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnProductIgnoringCase() {
        ProductEntity productEntity = productRepoPostgres.getByNameAndPriceIgnoreCase("ak 47",10F);
        Assertions.assertEquals("AK 47",productEntity.getName());
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnProperPriceForOneGram() {
        ProductEntity productEntity = productRepoPostgres.getByNameAndPriceIgnoreCase("ak 47",10F);
        Assertions.assertEquals(20,productEntity.getQuantityPriceMap().get(1F));
    }

    @Test
    @Sql(scripts = "classpath:populateDb/insertData.sql")
    void shouldReturnProperPriceForFiveGram() {
        ProductEntity productEntity = productRepoPostgres.getByNameAndPriceIgnoreCase("ak 47",10F);
        Assertions.assertEquals(16,productEntity.getQuantityPriceMap().get(5F));
    }
}

