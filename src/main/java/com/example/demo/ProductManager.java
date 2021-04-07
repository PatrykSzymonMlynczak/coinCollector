package com.example.demo;

import com.example.demo.exceptions.SortPricingAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ProductManager implements ProductRepo, ApplicationRunner {

    //todo ? immutable single key-val pair
    HashMap<HashMap<Float,String>, Product> inMemorySortMap = new HashMap<>();
    JsonFileManager jsonToFileManager;

    @Autowired
    public ProductManager(JsonFileManager jsonToFileManager) {
        this.jsonToFileManager = jsonToFileManager;
    }

    @Override
    public Product saveProduct(Product product)  {

        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(product.getMyPrice(), product.getName());

        if(!inMemorySortMap.containsKey(priceProductKeyMap)) {
            jsonToFileManager.saveNewProductToFileAsJson(product);
            inMemorySortMap.put(priceProductKeyMap, product);
        } else throw new SortPricingAlreadyExistsException(product.getName(), product.getMyPrice());

        return product;
    }

    @Override
    public HashMap<HashMap<Float,String >, Product> loadAllProducts() {
        for (Product product : jsonToFileManager.readProductListFromFile()) {
            HashMap<Float,String> priceProductKeyMap = new HashMap<>();
            priceProductKeyMap.put(product.getMyPrice(), product.getName());
            inMemorySortMap.put(priceProductKeyMap, product);
        }
        return inMemorySortMap;
    }

    @Override
    public Product getSortPricingByProductAndMyPrice(String product, Float myPrice) {
        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(myPrice, product);
        return inMemorySortMap.get(priceProductKeyMap);
    }

    @Override
    public void deleteProduct(String product, Float myPrice){
        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(myPrice, product);

        inMemorySortMap.remove(priceProductKeyMap);
        jsonToFileManager.updateProductFile(new ArrayList<>(inMemorySortMap.values()));
    }

    @Override
    public void run(ApplicationArguments args) {
        loadAllProducts();
    }
}
