package com.example.demo.businessLogic.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    /** must be sorted*/
    private TreeMap<Float,Float> quantityPriceMap;
    private Float myPrice;

    public Product(String name, TreeMap<Float, Float> quantityPriceMap, Float myPrice) {
        this.name = name;
        this.quantityPriceMap = quantityPriceMap;
        this.myPrice = myPrice;
    }
}
