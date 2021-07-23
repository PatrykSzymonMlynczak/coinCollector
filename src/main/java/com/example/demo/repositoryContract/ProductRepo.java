package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.product.PriceNameId;
import com.example.demo.businessLogic.product.Product;

import java.util.HashMap;

public interface ProductRepo{

    Product saveProduct(Product product);
    Product getProductByNameAndMyPrice(String productName, Float myPrice);
    void deleteProduct(String product, Float myPrice);
    HashMap<PriceNameId, Product> loadAllProducts();

}
