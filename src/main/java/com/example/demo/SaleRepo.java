package com.example.demo;

import java.util.List;

public interface SaleRepo {

    void saveSale(Weed weed, Integer quantity, String personName);

    List<Sale> getAllSales();

    Float getAllEarnedMoney();
}
