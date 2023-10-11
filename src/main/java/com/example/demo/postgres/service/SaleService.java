package com.example.demo.postgres.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.postgres.entity.SaleEntity;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleService {

    private final SaleRepoPostgres saleRepoPostgres;
    private final PersonService personService;
    private final ProductService productService;
    private final SaleMapper saleMapper;

    public Sale saveManualSale(String productName, Float quantity, String personName, Float givenMoney, Float manualPrice, String date) {
        Person person = personService.getPerson(personName);

        productService.reduceTotalSortAmount(productName, quantity);
        Product product = productService.getProductByName(productName);

        Sale sale = Sale.createManualSale(product, quantity, person, manualPrice, givenMoney, date);

        personService.updateDebt(sale.getPerson().getDebt(), sale.getPerson().getName());

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }


    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float money, String date) {
        Person person = personService.getPerson(personName);

        productService.reduceTotalSortAmount(productName, quantity);
        Product product = productService.getProductByName(productName);

        Sale sale = new Sale(product, quantity, person, discount, money, date);

        personService.updateDebt(sale.getPerson().getDebt(), sale.getPerson().getName());

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }


    public Sale saveSaleNotIgnoringSurplus(String productName, Float quantity, String personName, String date) {
        Person person = personService.getPerson(personName);

        productService.reduceTotalSortAmount(productName, quantity);
        Product product = productService.getProductByName(productName);

        Sale sale = Sale.createSaleNotIgnoringSurplus(product, quantity, person, null, null, date);

        personService.updateDebt(sale.getPerson().getDebt(), sale.getPerson().getName());

        SaleEntity saleEntity = saleMapper.saleToEntity(sale);
        saleRepoPostgres.save(saleEntity);
        return sale;
    }


    public Float getEarningsWithoutSpecifiedProductName(String name) {
        return saleRepoPostgres.getEarningsWithoutSpecifiedProductName(name);
    }


    public Float getEarningsWithSpecifiedProductName(String name) {
        return saleRepoPostgres.getEarningsWithSpecifiedProductName(name);
    }


    public Float priceCheckout(String productName, Float quantity) {
        Product product = productService.getProductByName(productName);
        return Sale.priceCheckoutSale(product, quantity).getIncome();
    }


    public void deleteLastSale() {
        Float lastQuantity = saleRepoPostgres.getLastSale().getQuantity();
        String lastProductName = saleRepoPostgres.getLastSale().getProduct().getName();
        productService.revertTotalSortAmount(lastProductName, lastQuantity);

        saleRepoPostgres.deleteLastSale();
    }


    public void deleteById(Long id) {
        SaleEntity saleEntity = saleRepoPostgres.getById(id);
        Float quantity = saleEntity.getQuantity();
        String productName = saleEntity.getProduct().getName();
        productService.revertTotalSortAmount(productName, quantity);

        saleRepoPostgres.deleteById(id);
    }


    public Sale getLastSale() {
        return saleMapper.entityToSale(saleRepoPostgres.getLastSale());
    }


    public List<Sale> getSalesByPeriod(String dateStartString, String dateEndString) {
        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);
        return saleRepoPostgres.getSalesByPeriod(dateStart, dateEnd).stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
    }


    public List<Sale> getSalesByDay(String dateString) {
        LocalDate date = LocalDate.parse(dateString);

        return saleRepoPostgres.getSalesByDay(date).stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
    }


    public List<Sale> loadAllSales() {
        List<SaleEntity> saleEntities = saleRepoPostgres.findAll();
        return saleEntities.stream()
                .map(saleMapper::entityToSale)
                .collect(Collectors.toList());
    }


    public Float getTotalEarnings() {
        return saleRepoPostgres.getTotalEarnings();
    }


    public Float getTotalCost() {
        return saleRepoPostgres.getTotalCost();
    }


    public Float getTotalIncome() {
        return saleRepoPostgres.getTotalIncome();
    }


    public Float getEarnedMoneyByDay(String dateString) {
        LocalDate date;
        if (dateString == null || dateString.equals("undefined")) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(dateString);
        }
        return saleRepoPostgres.getEarnedMoneyByDay(date);
    }


    public Float getEarnedMoneyByPeriod(String dateStartString, String dateEndString) {
        LocalDate dateEnd;
        if (dateEndString == null || dateEndString.equals("undefined")) {
            dateEnd = LocalDate.now();
        } else {
            dateEnd = LocalDate.parse(dateEndString);
        }
        LocalDate dateStart = LocalDate.parse(dateStartString);
        return saleRepoPostgres.getEarnedMoneyByPeriod(dateStart, dateEnd);
    }


    public List<Sale> clearAllSales() {
        saleRepoPostgres.findAll().stream().forEach(sale -> {
            Float lastQuantity = sale.getQuantity();
            String lastProductName = sale.getProduct().getName();
            productService.revertTotalSortAmount(lastProductName, lastQuantity);
        });
        saleRepoPostgres.deleteAll();
        return new ArrayList<>();
    }
}
