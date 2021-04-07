package com.example.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JSONtoFileSaver {

    public void saveToFileAsJson(Sale newSale){
        FileWriter fileWriter;
        List<Sale> saleList = readSaleListFromFile();
        saleList.add(newSale);
        String serialized = new Gson().toJson(saleList);

        try {
            fileWriter = new FileWriter("sale.json", false);
            fileWriter.write(serialized);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFileAsJson(SortPricing newSortPricing){

        FileWriter fileWriter;
        List<SortPricing> sortPricingList = readSortPricingListFromFile();
        sortPricingList.add(newSortPricing);
        String serialized = new Gson().toJson(sortPricingList);

        try {
            fileWriter = new FileWriter("sortPricing.json", false);

            fileWriter.write(serialized);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SortPricing> readSortPricingListFromFile(){
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

        return sortPricingList;
    }

    public List<Sale> readSaleListFromFile(){
        Gson gson = new Gson();
        List<Sale> salelist;

        Sale[] model = null;
        if(new File("sale.json").length() != 0){
            try {
                model = gson.fromJson(new FileReader("sale.json"), Sale[].class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            salelist = Arrays.stream(model).collect(Collectors.toList());
        }else salelist = new ArrayList<>();

        return salelist;

    }
}
