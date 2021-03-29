package com.example.demo;

import java.util.HashMap;

public interface SortPricingRepo {

    void save(SortPricing sortPricing);

    HashMap<Enum<Weed>, SortPricing> getAll();

    SortPricing getSortPricingByWeed(Weed weed);


}
