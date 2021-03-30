package com.example.demo;

import java.util.HashMap;

public interface SortPricingRepo {

    void save(SortPricing sortPricing);

    HashMap<HashMap<Float,Enum<Weed> >, SortPricing>  getAll();

    SortPricing getSortPricingByWeedAndMyPrice(Weed weed, Float myPrice);


}
