package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

@ToString
@Getter
@AllArgsConstructor
public class SortPricing {

    private Enum<Product> name;
    private HashMap<Integer,Float> quantityPriceMap;
    private Float myPrice;

}
