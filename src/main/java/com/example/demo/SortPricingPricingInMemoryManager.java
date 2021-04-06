package com.example.demo;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SortPricingPricingInMemoryManager implements SortPricingRepo {

    HashMap<HashMap<Float,String>, SortPricing> inMemorySortMap = new HashMap<>();
    ArrayList<SortPricing> sortPricingArrayList = new ArrayList<>();



    @Override
    public SortPricing saveProduct(SortPricing sortPricing) {

        HashMap<Float,String> priceWeedMap = new HashMap<>();
        priceWeedMap.put(sortPricing.getMyPrice(), sortPricing.getName());

         inMemorySortMap.put(priceWeedMap, sortPricing);
         return sortPricing;
    }

    @Override
    public  HashMap<HashMap<Float,String >, SortPricing> getAllProducts() {
        return inMemorySortMap;
    }

    @Override
    public SortPricing getSortPricingByProductAndMyPrice(String product, Float myPrice) {
        HashMap<Float,String> priceProductMap = new HashMap<>();
        priceProductMap.put(myPrice, product);
        return inMemorySortMap.get(priceProductMap);
    }


}
