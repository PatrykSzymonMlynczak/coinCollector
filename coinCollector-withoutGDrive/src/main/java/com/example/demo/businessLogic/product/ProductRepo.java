package com.example.demo.businessLogic.product;

import java.util.HashMap;

public interface ProductRepo {

    Product saveProduct(Product product);

    HashMap<HashMap<Float,String >, Product> loadAllProducts();

    Product getSortPricingByProductAndMyPrice(String product, Float myPrice);

    void deleteProduct(String product, Float myPrice);


}
