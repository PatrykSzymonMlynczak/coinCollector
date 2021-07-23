package com.example.demo.fileManager.service;

import com.example.demo.businessLogic.product.PriceNameId;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.fileManager.JsonFileManager;
import com.example.demo.repositoryContract.ProductRepo;
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

    private final HashMap<PriceNameId, Product> inMemoryProductMap = new HashMap<>();
    private final JsonFileManager jsonToFileManager;


    @Autowired
    public ProductRepoImpl(JsonFileManager jsonToFileManager) {
        this.jsonToFileManager = jsonToFileManager;
    }

    @Override
    public Product saveProduct(Product product)  {

        PriceNameId priceNameId = new PriceNameId(product.getMyPrice(), product.getName());

        if(!inMemoryProductMap.containsKey(priceNameId)) {
            jsonToFileManager.saveNewProductToFileAsJson(product);
            inMemoryProductMap.put(priceNameId, product);
        } else throw new ProductAlreadyExistsException(product.getName(), product.getMyPrice());

        return product;
    }

    @Override
    public HashMap<PriceNameId, Product> loadAllProducts() {
        for (Product product : jsonToFileManager.readProductListFromFile()) {
            PriceNameId priceNameId = new PriceNameId(product.getMyPrice(), product.getName());
            inMemoryProductMap.put(priceNameId, product);
        }
        return inMemoryProductMap;
    }

    @Override
    public Product getProductByNameAndMyPrice(String productName, Float myPrice) {
        PriceNameId priceNameId = new PriceNameId(myPrice, productName);
        return inMemoryProductMap.get(priceNameId);
    }

    @Override
    public void deleteProduct(String productName, Float myPrice){
        PriceNameId priceNameId = new PriceNameId(myPrice, productName);

        inMemoryProductMap.remove(priceNameId);
        jsonToFileManager.updateProductFile(new ArrayList<>(inMemoryProductMap.values()));
    }

    @Override
    public void run(ApplicationArguments args) {
        loadAllProducts();
    }
}
