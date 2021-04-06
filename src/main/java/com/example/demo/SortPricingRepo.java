package com.example.demo;

import java.util.HashMap;

public interface SortPricingRepo {

    SortPricing saveProduct(SortPricing sortPricing);

    HashMap<HashMap<Float,String >, SortPricing> getAllProducts();

    SortPricing getSortPricingByProductAndMyPrice(String product, Float myPrice);


}
