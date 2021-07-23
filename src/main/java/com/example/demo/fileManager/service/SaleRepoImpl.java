package com.example.demo.fileManager.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.PriceNameId;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.businessLogic.sale.exception.StartDateIsAfterEndDateException;
import com.example.demo.fileManager.JsonFileManager;
import com.example.demo.repositoryContract.ProductRepo;
import com.example.demo.repositoryContract.SaleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("jsonFile")
public class SaleRepoImpl implements SaleRepo, ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(SaleRepo.class);

    private final PersonInMemoryManager personInMemoryManager;
    private final ProductRepo productRepoImpl;
    private final JsonFileManager jsonFileManager;

    private ArrayList<Sale> saleArrayList = new ArrayList<>();

    @Autowired
    public SaleRepoImpl(PersonInMemoryManager personInMemoryManager,
                        JsonFileManager jsonFileManager,
                        ProductRepo productRepoImpl) {
        this.personInMemoryManager = personInMemoryManager;
        this.productRepoImpl = productRepoImpl;
        this.jsonFileManager = jsonFileManager;
    }

    @Override
    public Sale saveSale(String productName, Float quantity, String personName, Float discount, Float mySortPrice,Float money) {

        Product product = getProductByNameAndPrice(mySortPrice,productName);
        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(product,quantity,person,discount,money);
        if( productRepoImpl.getProductByNameAndMyPrice(sale.getProduct().getName(), sale.getMySortPrice()) != null){
            saleArrayList.add(sale);
            jsonFileManager.saveSaleToFileAsJson(sale);
        }else throw new ProductNotExistException(product.getName(),mySortPrice);

        return sale;
    }

    private Product getProductByNameAndPrice(Float mySortPrice, String productName){
        PriceNameId priceNameId = new PriceNameId(mySortPrice,productName);
        if(productRepoImpl.loadAllProducts().containsKey(priceNameId)) {
            return productRepoImpl.loadAllProducts().get(priceNameId);
        }else throw new ProductNotExistException(productName,mySortPrice);
    }

    @Override
    public ArrayList<Sale> loadAllSales() {
        return saleArrayList = (ArrayList<Sale>) jsonFileManager.readSaleListFromFile();
    }

    @Override
    public Float getTotalEarnings() {
        float totalEarnings = 0F;

        for (Sale sale: saleArrayList ) {
            totalEarnings += sale.getEarned();
        }logger.info("total earnings: "+totalEarnings);
        return totalEarnings;
    }

    @Override
    public Float getTotalCost() {
        float totalCost = 0F;

        for (Sale sale: saleArrayList ) {
            totalCost += sale.getMySortPrice()* sale.getQuantity();
        }logger.info("total cost: "+totalCost);
        return totalCost;
    }

    @Override
    public Float getTotalIncome() {
        return getTotalEarnings()+getTotalCost();
    }

    @Override
    public Float getEarnedMoneyByDay(String date){
        LocalDate localDate = LocalDate.parse(date);
        Float totalSaleCost = 0F;

        for(Sale sale: saleArrayList){
            if( sale.getTransactionDate().equals(localDate) ) {
                totalSaleCost += sale.getEarned();
            }
        }
        logger.info("  by day: "+": total = "+totalSaleCost);
        return totalSaleCost;
    }

    @Override
    public Float getEarnedMoneyByWeek(String dateStart,String dateEnd){
        LocalDate localDateStart = LocalDate.parse(dateStart);
        LocalDate localDateEnd = LocalDate.parse(dateEnd);
        Float totalSaleCost = 0F;

        if(localDateEnd.isBefore(localDateStart)) throw new StartDateIsAfterEndDateException(dateStart,dateEnd);

        for(Sale sale: saleArrayList){
            if( sale.getTransactionDate().isEqual(localDateStart)
                    || sale.getTransactionDate().isEqual(localDateEnd)
                    || (    sale.getTransactionDate().isAfter(localDateStart)
                            && sale.getTransactionDate().isBefore(localDateEnd) )
            ) { totalSaleCost += sale.getEarned(); }
        }
        logger.info("  by day: "+": total = "+totalSaleCost);
        return totalSaleCost;
    }

    @Override
    public List<Sale> clearAllSales(){
       return jsonFileManager.clearAllSales();
    }

    @Override
    public void run(ApplicationArguments args){
        loadAllSales();
    }
}
