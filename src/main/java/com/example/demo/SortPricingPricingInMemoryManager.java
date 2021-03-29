package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class SortPricingPricingInMemoryManager implements SortPricingRepo {

    HashMap<Enum<Weed>, SortPricing> inMemorySortList = new HashMap<>();

    @Override
    public void save(SortPricing sortPricing) {
        inMemorySortList.put(sortPricing.getName(), sortPricing);
    }

    @Override
    public  HashMap<Enum<Weed>, SortPricing> getAll() {
        return inMemorySortList;
    }

    @Override
    public SortPricing getSortPricingByWeed(Weed weed) {
        Weed soldWeed = Arrays.stream(Weed.values()).filter(w -> w.name().equals(weed.name())).findAny().get();
        return inMemorySortList.get(soldWeed);
    }


}
