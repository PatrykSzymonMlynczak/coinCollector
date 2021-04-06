package com.example.demo;

import java.util.HashMap;

public interface SortPricingRepo {

    SortPricing saveProduct(SortPricing sortPricing);

    HashMap<HashMap<Float,Enum<Product> >, SortPricing> getAllProducts();

    SortPricing getSortPricingByProductAndMyPrice(Product product, Float myPrice);


}
