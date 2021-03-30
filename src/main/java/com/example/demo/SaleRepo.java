package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public interface SaleRepo {

    ArrayList<Sale> saveSale(Weed weed, Integer quantity, String personName, Float discount, Float mySortPrice);

    List<Sale> getAllSales();

    Float getWholeIncome();
}
