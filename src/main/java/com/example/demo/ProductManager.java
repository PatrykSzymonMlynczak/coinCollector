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
    JsonFileManager jsoNtoFileManager;

    @Autowired
    public ProductManager(JsonFileManager jsoNtoFileManager) {
        this.jsoNtoFileManager = jsoNtoFileManager;
    }

    @Override
    public Product saveProduct(Product product)  {
        inMemorySortMap = loadAllProductsToMemory();

        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(product.getMyPrice(), product.getName());

        if(!inMemorySortMap.containsKey(priceProductKeyMap)) {
            jsoNtoFileManager.saveNewProductToFileAsJson(product);
            inMemorySortMap.put(priceProductKeyMap, product);
        } else throw new SortPricingAlreadyExistsException(product.getName(), product.getMyPrice());

        return product;
    }

    @Override
    public HashMap<HashMap<Float,String >, Product> loadAllProductsToMemory() {
        for (Product product : jsoNtoFileManager.readProductListFromFile()) {
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
        jsoNtoFileManager.updateProductFile(new ArrayList<>(inMemorySortMap.values()));
    }

    @Override
    public void run(ApplicationArguments args) {
        loadAllProductsToMemory();
    }
}
