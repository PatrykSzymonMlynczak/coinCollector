package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public interface SaleRepo {

    ArrayList<Sale> saveSale(String product, Integer quantity, String personName, Float discount, Float mySortPrice);

    List<Sale> loadAllSales();

}
