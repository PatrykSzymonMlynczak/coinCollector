package com.example.demo;

import com.example.demo.exceptions.SortPricingAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SortPricingPricingInMemoryManager implements SortPricingRepo, ApplicationRunner {

    //todo immutable single key-val pair
    HashMap<HashMap<Float,String>, SortPricing> inMemorySortMap = new HashMap<>();
    JSONtoFileSaver jsoNtoFileSaver;

    @Autowired
    public SortPricingPricingInMemoryManager(JSONtoFileSaver jsoNtoFileSaver) {
        this.jsoNtoFileSaver = jsoNtoFileSaver;
    }

    @Override
    public SortPricing saveProduct(SortPricing sortPricing)  {
        inMemorySortMap = loadAllProductsToMemory();

        HashMap<Float,String> priceWeedMap = new HashMap<>();
        priceWeedMap.put(sortPricing.getMyPrice(), sortPricing.getName());

        if(!inMemorySortMap.containsKey(priceWeedMap)) {
            jsoNtoFileSaver.saveToFileAsJson(sortPricing);
            inMemorySortMap.put(priceWeedMap, sortPricing);
        } else throw new SortPricingAlreadyExistsException(sortPricing.getName(), sortPricing.getMyPrice());

        return sortPricing;
    }

    @Override
    public HashMap<HashMap<Float,String >, SortPricing> loadAllProductsToMemory() {
        for (SortPricing sortPricing: jsoNtoFileSaver.readSortPricingListFromFile()) {
            HashMap<Float,String> priceWeedMap = new HashMap<>();
            priceWeedMap.put(sortPricing.getMyPrice(), sortPricing.getName());
            inMemorySortMap.put(priceWeedMap, sortPricing);
        }
        return inMemorySortMap;
    }

    @Override
    public SortPricing getSortPricingByProductAndMyPrice(String product, Float myPrice) {
        HashMap<Float,String> priceProductMap = new HashMap<>();
        priceProductMap.put(myPrice, product);
        return inMemorySortMap.get(priceProductMap);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadAllProductsToMemory();
    }
}
