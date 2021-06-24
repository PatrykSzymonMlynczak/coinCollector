package com.example.demo.fileManager;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsonFileManager {

    @Value("${file.sortPricing}")
    private String sortPricingFilename;
    @Value("${file.sale}")
    private String saleFilename;

    /// PRODUCT
    public void saveNewProductToFileAsJson(Product newProduct){

        List<Product> productList = readProductListFromFile();
        productList.add(newProduct);
        saveProductListToFile(productList);
    }

    public void updateProductFile(List<Product> productList){
        saveProductListToFile(productList);
    }

    private void saveProductListToFile(List<Product> productList){

        FileWriter fileWriter;
        String jsonProductList = new Gson().toJson(productList);

        try {
            fileWriter = new FileWriter(sortPricingFilename, false);
            fileWriter.write(jsonProductList);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> readProductListFromFile(){
        Gson gson = new Gson();
        List<Product> productList;

        Product[] model = null;
        if(new File(sortPricingFilename).length() != 0){
            try {
                model = gson.fromJson(new FileReader(sortPricingFilename), Product[].class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            productList = Arrays.stream(model).collect(Collectors.toList());
        }else productList = new ArrayList<>();

        return productList;
    }

    /// SALE
    public void saveSaleToFileAsJson(Sale newSale){
        List<Sale> saleList = readSaleListFromFile();
        saleList.add(newSale);

        FileWriter fileWriter;
        String jsonProductList = new Gson().toJson(saleList);

        try {
            fileWriter = new FileWriter(saleFilename, false);
            fileWriter.write(jsonProductList);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Sale> readSaleListFromFile(){
        Gson gson = new Gson();
        List<Sale> saleList;

        Sale[] model = null;
        if(new File(saleFilename).length() != 0){
            try {
                model = gson.fromJson(new FileReader(saleFilename), Sale[].class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            saleList = Arrays.stream(model).collect(Collectors.toList());
        }else saleList = new ArrayList<>();

        return saleList;

    }

    public List<Sale> clearAllSales() {
        FileWriter fwOb = null;
        try {
            fwOb = new FileWriter(saleFilename, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
