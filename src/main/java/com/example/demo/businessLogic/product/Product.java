package com.example.demo.businessLogic.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.TreeMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    /** must be sorted*/
    private TreeMap<Float,Float> quantityPriceMap;
    private float myPrice;
    private float totalSortAmount;

    private LocalDate additionDate;
    private LocalDate eraseDate;


    public Product(String name, TreeMap<Float, Float> quantityPriceMap, float myPrice, float totalSortAmount) {
        this.name = name;
        this.quantityPriceMap = quantityPriceMap;
        this.myPrice = myPrice;
        this.totalSortAmount = totalSortAmount;
        this.additionDate = LocalDate.now();
    }

    public Product(String name, TreeMap<Float, Float> quantityPriceMap, float myPrice) {
        this.name = name;
        this.quantityPriceMap = quantityPriceMap;
        this.myPrice = myPrice;
    }

    //in case of paying debt
    public Product(String name, float debt) {
        TreeMap<Float, Float> payedDebt = new TreeMap<>();
        payedDebt.put(1f,debt);
        this.quantityPriceMap  = payedDebt;

        this.name = name;
        this.myPrice = 0f;
    }
}
