package com.example.demo.postgres.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.postgres.entity.SaleEntity;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import com.example.demo.repositoryContract.SaleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleService implements SaleRepo {

    private final SaleRepoPostgres saleRepoPostgres;
    private final PersonService personService;
    private final ProductService productService;
    private final SaleMapper saleMapper;

    @Override
    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float money, String date) {
        Person person = personService.getPerson(personName);

        productService.reduceTotalSortAmount(productName, quantity);
        Product product = productService.getProductByName(productName);

        Sale sale = new Sale(product,quantity,person,discount,money, date);

        personService.updateDebt(sale.getPerson().getDebt(), sale.getPerson().getName());

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }

    @Override
    public Sale saveSaleIgnoringSurplus(String productName, Float quantity, String personName, String date) {
        Person person = personService.getPerson(personName);

        productService.reduceTotalSortAmount(productName, quantity);
        Product product = productService.getProductByName(productName);

        Sale sale = new Sale(product,quantity,person, date);

        personService.updateDebt(sale.getPerson().getDebt(), sale.getPerson().getName());

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }

    @Override
    public Float getEarningsWithoutSpecifiedProductName(String name) {
        return saleRepoPostgres.getEarningsWithoutSpecifiedProductName(name);
    }

    @Override
    public Float getEarningsWithSpecifiedProductName(String name) {
        return saleRepoPostgres.getEarningsWithSpecifiedProductName(name);
    }

    @Override
    public Float priceCheckout(String productName, Float quantity) {
        Product product = productService.getProductByName(productName);
        return new Sale(product,quantity).getIncome();
    }

    @Override
    public void deleteLastSale() {
        Float lastQuantity = saleRepoPostgres.getLastSale().getQuantity();
        String  lastProductName = saleRepoPostgres.getLastSale().getProduct().getName();
        productService.revertTotalSortAmount(lastProductName, lastQuantity);

        saleRepoPostgres.deleteLastSale();
    }

    @Override
    public Sale getLastSale() {
        return saleMapper.entityToSale(saleRepoPostgres.getLastSale());
    }

    @Override
    public List<Sale> getSalesByPeriod(String dateStartString, String dateEndString) {
        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);
        return saleRepoPostgres.getSalesByPeriod(dateStart, dateEnd).stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sale> getSalesByDay(String dateString) {
        LocalDate date = LocalDate.parse(dateString);

        return saleRepoPostgres.getSalesByDay(date).stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sale> loadAllSales() {
        List<SaleEntity> saleEntities = saleRepoPostgres.findAll();
        return saleEntities.stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
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
    public Float getEarnedMoneyByPeriod(String dateStartString, String dateEndString) {
        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);
        return saleRepoPostgres.getEarnedMoneyByPeriod(dateStart,dateEnd);
    }

    @Override
    public List<Sale> clearAllSales() {
        saleRepoPostgres.findAll().stream().forEach(sale -> {
            Float lastQuantity = sale.getQuantity();
            String  lastProductName = sale.getProduct().getName();
            productService.revertTotalSortAmount(lastProductName, lastQuantity);
        });
        saleRepoPostgres.deleteAll();
        return new ArrayList<>();
    }
}
