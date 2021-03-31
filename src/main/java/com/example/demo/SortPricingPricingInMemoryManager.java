package com.example.demo;

import org.springframework.stereotype.Service;


import java.util.HashMap;

@Service
public class SortPricingPricingInMemoryManager implements SortPricingRepo {

    HashMap<HashMap<Float,Enum<Weed>>, SortPricing> inMemorySortMap = new HashMap<>();

    @Override
    public void save(SortPricing sortPricing) {

        HashMap<Float,Enum<Weed>> priceWeedMap = new HashMap<Float,Enum<Weed>>();
        priceWeedMap.put(sortPricing.getMyPrice(), sortPricing.getName());

        inMemorySortMap.put(priceWeedMap, sortPricing);
    }

    @Override
    public  HashMap<HashMap<Float,Enum<Weed> >, SortPricing> getAll() {
        return inMemorySortMap;
    }

    @Override
    public SortPricing getSortPricingByWeedAndMyPrice(Weed weed, Float myPrice) {
        HashMap<Float,Enum<Weed>> priceWeedMap = new HashMap<Float,Enum<Weed>>();
        priceWeedMap.put(myPrice,weed);
        SortPricing sortPricing = inMemorySortMap.get(priceWeedMap);
        return sortPricing;
    }


}
