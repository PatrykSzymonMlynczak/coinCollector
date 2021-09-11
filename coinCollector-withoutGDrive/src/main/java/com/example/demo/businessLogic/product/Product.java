package com.example.demo.businessLogic.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.TreeMap;

@ToString
@Getter
@AllArgsConstructor
public class Product {

    private final String name;
    /** must be sorted*/
    private final TreeMap<Float,Float> quantityPriceMap;
    private final Float myPrice;

}
