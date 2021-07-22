package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.product.Product;

import java.util.HashMap;

public interface ProductRepo{

    Product saveProduct(Product product);

    Product getProductByNameAndMyPrice(String productName, Float myPrice);

    void deleteProduct(String product, Float myPrice);

    default HashMap<HashMap<Float,String >, Product> loadAllProducts(){
        return new HashMap<>();
    }

}
