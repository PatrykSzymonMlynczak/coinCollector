package com.example.demo.businessLogic.product;

import java.util.HashMap;

public interface ProductRepo{

    Product saveProduct(Product product);

    Product getSortPricingByProductAndMyPrice(String product, Float myPrice);

    void deleteProduct(String product, Float myPrice);

    default HashMap<HashMap<Float,String >, Product> loadAllProducts(){
        return new HashMap<>();
    }

}
