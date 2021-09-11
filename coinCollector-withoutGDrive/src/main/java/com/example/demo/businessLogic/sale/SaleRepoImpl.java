package com.example.demo.businessLogic.sale;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.ProductManager;
import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.person.PersonInMemoryManager;
import com.example.demo.fileManager.JsonFileManager;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.businessLogic.sale.exception.SortPricingNotExistException;
import com.example.demo.businessLogic.sale.exception.StartDateIsAfterEndDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class SaleRepoImpl implements SaleRepo, ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(SaleRepo.class);

    private final PersonInMemoryManager personInMemoryManager;
    private final ProductManager productManager;
    private final JsonFileManager jsonFileManager;

    private final ArrayList<Sale> saleArrayList = new ArrayList<>();

    @Autowired
    public SaleRepoImpl(PersonInMemoryManager personInMemoryManager,
                        JsonFileManager jsonFileManager,
                        ProductManager productManager) {
        this.personInMemoryManager = personInMemoryManager;
        this.productManager = productManager;
        this.jsonFileManager = jsonFileManager;
    }

    @Override
    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float mySortPrice,Float money) {

        Product product = getProductByNameAndPrice(mySortPrice,productName);
        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(product,quantity,person,discount,money);
        if( productManager.getSortPricingByProductAndMyPrice(sale.getProduct().getName(), sale.getMySortPrice()) != null){
            saleArrayList.add(sale);
            jsonFileManager.saveSaleToFileAsJson(sale);
        }else throw new SortPricingNotExistException(product.getName(),mySortPrice);

        return sale;
    }

    private Product getProductByNameAndPrice(Float mySortPrice, String productName){
        HashMap<Float, String> productKeyMap = new HashMap<>();
        productKeyMap.put(mySortPrice,productName);
        if(productManager.loadAllProducts().containsKey(productKeyMap)) {
            return productManager.loadAllProducts().get(productKeyMap);
        }else throw new ProductNotExistException(productName,mySortPrice);
    }

    @Override
    public ArrayList<Sale> loadAllSales() {
        saleArrayList.addAll(jsonFileManager.readSaleListFromFile());
        return this.saleArrayList;
    }

    public Float getTotalEarnings() {
        float totalEarnings = 0F;

        for (Sale sale: saleArrayList ) {
            totalEarnings += sale.getEarned();
        }logger.info("total earnings: "+totalEarnings);
        return totalEarnings;
    }

    public Float getTotalCost() {
        float totalCost = 0F;

        for (Sale sale: saleArrayList ) {
            totalCost += sale.getMySortPrice()* sale.getQuantity();
        }logger.info("total cost: "+totalCost);
        return totalCost;
    }

    public Float getTotalIncome() {
        return getTotalEarnings()+getTotalCost();
    }

    public Float getEarnedMoneyByDay(String date){
        LocalDate localDate = LocalDate.parse(date);
        Float totalSaleCost = 0F;

        for(Sale sale: saleArrayList){
            if( sale.getTransactionDate().toLocalDate().equals(localDate) ) {
                totalSaleCost += sale.getEarned();
            }
        }
        logger.info("  by day: "+": total = "+totalSaleCost);
        return totalSaleCost;
    }

    public Float getEarnedMoneyByWeek(String dateStart,String dateEnd){
        LocalDate localDateStart = LocalDate.parse(dateStart);
        LocalDate localDateEnd = LocalDate.parse(dateEnd);
        Float totalSaleCost = 0F;

        if(localDateEnd.isBefore(localDateStart)) throw new StartDateIsAfterEndDateException(dateStart,dateEnd);

        for(Sale sale: saleArrayList){
            if( sale.getTransactionDate().toLocalDate().isEqual(localDateStart)
                    || sale.getTransactionDate().toLocalDate().isEqual(localDateEnd)
                    || (    sale.getTransactionDate().toLocalDate().isAfter(localDateStart)
                            && sale.getTransactionDate().toLocalDate().isBefore(localDateEnd) )
            ) { totalSaleCost += sale.getEarned(); }
        }
        logger.info("  by day: "+": total = "+totalSaleCost);
        return totalSaleCost;
    }

    public List<Sale> clearAllSales(){
       return jsonFileManager.clearAllSales();
    }

    @Override
    public void run(ApplicationArguments args){
        loadAllSales();
    }
}
