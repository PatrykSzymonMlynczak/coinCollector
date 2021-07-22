package com.example.demo.businessLogic.product;

import com.example.demo.fileManager.JsonFileManager;
import com.example.demo.businessLogic.product.exception.SortPricingAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Qualifier("jsonFile")
public class ProductRepoImpl implements ProductRepo, ApplicationRunner {

    private final HashMap<HashMap<Float,String>, Product> inMemoryProductMap = new HashMap<>();
    private final JsonFileManager jsonToFileManager;

    @Autowired
    public ProductRepoImpl(JsonFileManager jsonToFileManager) {
        this.jsonToFileManager = jsonToFileManager;
    }

    @Override
    public Product saveProduct(Product product)  {

        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(product.getMyPrice(), product.getName());

        if(!inMemoryProductMap.containsKey(priceProductKeyMap)) {
            jsonToFileManager.saveNewProductToFileAsJson(product);
            inMemoryProductMap.put(priceProductKeyMap, product);
        } else throw new SortPricingAlreadyExistsException(product.getName(), product.getMyPrice());

        return product;
    }

    @Override
    public HashMap<HashMap<Float,String >, Product> loadAllProducts() {
        for (Product product : jsonToFileManager.readProductListFromFile()) {
            HashMap<Float,String> priceProductKeyMap = new HashMap<>();
            priceProductKeyMap.put(product.getMyPrice(), product.getName());
            inMemoryProductMap.put(priceProductKeyMap, product);
        }
        return inMemoryProductMap;
    }

    @Override
    public Product getSortPricingByProductAndMyPrice(String product, Float myPrice) {
        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(myPrice, product);
        return inMemoryProductMap.get(priceProductKeyMap);
    }

    @Override
    public void deleteProduct(String product, Float myPrice){
        HashMap<Float,String> priceProductKeyMap = new HashMap<>();
        priceProductKeyMap.put(myPrice, product);

        inMemoryProductMap.remove(priceProductKeyMap);
        jsonToFileManager.updateProductFile(new ArrayList<>(inMemoryProductMap.values()));
    }

    @Override
    public void run(ApplicationArguments args) {
        loadAllProducts();
    }
}
