package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.product.Product;

import java.util.List;

@Deprecated
public interface ProductRepo {

    Product saveProduct(Product product);
    Product getProductByName(String productName);
    void deleteProduct(String product);
    List<Product> loadAllProducts();
    void reduceTotalSortAmount(String productName, Float boughtQuantity);
    Float getTotalAmount(String productName);
    void eraseRestOfProduct(String productName);

}
