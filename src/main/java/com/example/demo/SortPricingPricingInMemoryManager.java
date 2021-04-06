package com.example.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortPricingPricingInMemoryManager implements SortPricingRepo {

    HashMap<HashMap<Float,Enum<Product>>, SortPricing> inMemorySortMap = new HashMap<>();
    ArrayList<SortPricing> sortPricingArrayList = new ArrayList<>();


    @Override
    public SortPricing saveProduct(SortPricing sortPricing) {

        HashMap<Float,Enum<Product>> priceWeedMap = new HashMap<>();
        priceWeedMap.put(sortPricing.getMyPrice(), sortPricing.getName());

        inMemorySortMap.put(priceWeedMap, sortPricing);
        saveToFileAsJson(sortPricing);
        return sortPricing;
    }

    @Override
    public  HashMap<HashMap<Float,Enum<Product> >, SortPricing> getAllProducts() {
        return inMemorySortMap;
    }

    @Override
    public SortPricing getSortPricingByProductAndMyPrice(Product product, Float myPrice) {
        HashMap<Float,Enum<Product>> priceProductMap = new HashMap<>();
        priceProductMap.put(myPrice, product);
        return inMemorySortMap.get(priceProductMap);
    }

    private void saveToFileAsJson(SortPricing newSortPricing){

        FileWriter fileWriter;
        String serialized = new Gson().toJson(readSaleListFromFileAndAddNewSale(newSortPricing));

        try {
            fileWriter = new FileWriter("sortPricing.json", false);

            fileWriter.write(serialized);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<SortPricing> readSaleListFromFileAndAddNewSale(SortPricing newSortPricing){
        Gson gson = new Gson();
        List<SortPricing> sortPricingList;

        SortPricing[] model = null;
        if(new File("sortPricing.json").length() != 0){
            try {
                model = gson.fromJson(new FileReader("sortPricing.json"), SortPricing[].class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sortPricingList = Arrays.stream(model).collect(Collectors.toList());
        }else sortPricingList = new ArrayList<>();

        sortPricingList.add(newSortPricing);
        return sortPricingList;

    }
/*
        public void saveAllAsJSON(ArrayList<Sale> saleArrayList){
            FileWriter fileWriter;



            JSONArray jsonArray = new JSONArray();
            for (Sale sale: saleArrayList) {
                JSONObject jsonSaleObject = new JSONObject();
                JSONObject jsonPersonObject = new JSONObject();
                jsonSaleObject.put("product", sale.getProduct().name());
                jsonSaleObject.put("quantity", sale.getQuantity().toString());
                jsonPersonObject.put("name", sale.getPerson().getName());
                jsonPersonObject.put("pricePerGramOverride", sale.getPerson().getPricePerGramOverride().toString());
                jsonSaleObject.put("person", jsonPersonObject);
                jsonSaleObject.put("transactionDate", sale.getTransactionDate().toString());
                jsonSaleObject.put("discount", sale.getDiscount().toString());
                jsonSaleObject.put("mySortPrice", sale.getMySortPrice().toString());

                jsonArray.add(jsonSaleObject);

            }

            try {
                fileWriter = new FileWriter("sale.txt", true);
                fileWriter.write("[");
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.write("]");
                fileWriter.flush();
                fileWriter.close();
                logger.info("Successfully Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/




}
