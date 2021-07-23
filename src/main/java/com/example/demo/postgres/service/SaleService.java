package com.example.demo.postgres.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.entity.SaleEntity;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import com.example.demo.repositoryContract.SaleRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("postgres")
@AllArgsConstructor
public class SaleService implements SaleRepo {

    private final SaleRepoPostgres saleRepoPostgres;
    private final PersonService personService;
    private final ProductService productService;
    private final SaleMapper saleMapper;

    @Override
    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float mySortPrice, Float money) {
        Product product = productService.getProductByNameAndMyPrice(productName,mySortPrice);
        Person person = personService.getPerson(personName);
        Sale sale = new Sale(product,quantity,person,discount,money);

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }

    @Override
    public List<Sale> loadAllSales() {
        List<SaleEntity> saleEntities = saleRepoPostgres.findAll();
        return saleEntities.stream().map(saleMapper::entityToSale).collect(Collectors.toList());
    }

    @Override
    public Float getTotalEarnings() {
        return saleRepoPostgres.getTotalEarnings();
    }

    @Override
    public Float getTotalCost() {
        return saleRepoPostgres.getTotalCost();
    }

    @Override
    public Float getTotalIncome() {
        return saleRepoPostgres.getTotalIncome();
    }

    @Override
    public Float getEarnedMoneyByDay(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return saleRepoPostgres.getEarnedMoneyByDay(date);
    }

    @Override
    public Float getEarnedMoneyByWeek(String dateStartString, String dateEndString) {
        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);
        return saleRepoPostgres.getEarnedMoneyByWeek(dateStart,dateEnd);
    }


}
