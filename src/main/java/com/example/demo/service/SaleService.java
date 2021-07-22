package com.example.demo.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.businessLogic.sale.SaleRepo;
import com.example.demo.entity.PersonEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.entity.SaleEntity;
import com.example.demo.mapper.pojoToEntity.PersonToEntityMapper;
import com.example.demo.mapper.pojoToEntity.ProductToEntityMapper;
import com.example.demo.mapper.pojoToEntity.SaleToEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
//@Primary
@Qualifier("postgres")
@AllArgsConstructor
public class SaleService implements SaleRepo {

    SaleRepoPostgres saleRepoPostgres;
    PersonRepoPostgres personRepoPostgres;
    ProductRepoPostgres productRepoPostgres;

    SaleToEntityMapper saleToEntityMapper;
    PersonToEntityMapper personToEntityMapper;
    ProductToEntityMapper productToEntityMapper;

    @Override
    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float mySortPrice, Float money) {
        //todo
        TreeMap<Float, Float> quantityPriceMap = new TreeMap<>();
        Product product = new Product(productName, quantityPriceMap, mySortPrice);
        ProductEntity productEntity = new ProductEntity(productName, quantityPriceMap, mySortPrice);
        //todo check if exists
        productRepoPostgres.save(productToEntityMapper.mapToEntity(product));
        PersonEntity personEntity = new PersonEntity(personName);
        //todo check if exists
        personRepoPostgres.save(personEntity);
        Person person = new Person(personName);
        Sale sale = new Sale(product,quantity,person,discount,money);
        SaleEntity saleEntity = saleToEntityMapper.mapToEntity(sale);
        saleEntity.setPerson(personRepoPostgres.findByName(personName));
        saleEntity.setProduct(productRepoPostgres.getByProductNameAndPrice(productName,mySortPrice));
        saleRepoPostgres.save(saleEntity);
        return sale;
    }

    @Override
    public Float getTotalEarnings() {
        return null;
    }

    @Override
    public Float getTotalCost() {
        return null;
    }

    @Override
    public Float getTotalIncome() {
        return null;
    }

    @Override
    public Float getEarnedMoneyByDay(String date) {
        return null;
    }

    @Override
    public Float getEarnedMoneyByWeek(String dateStart, String dateEnd) {
        return null;
    }


}
