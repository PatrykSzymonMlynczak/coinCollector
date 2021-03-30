package com.example.demo;

 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SaleInMemoryManager implements SaleRepo {

    private final Logger logger = LoggerFactory.getLogger(SaleInMemoryManager.class);

    private PersonInMemoryManager personInMemoryManager;
    private SortPricingPricingInMemoryManager sortPricingInMemoryManager;
    private ArrayList<Sale> saleArrayList = new ArrayList<>();


    @Autowired
    public SaleInMemoryManager(PersonInMemoryManager personInMemoryManager, SortPricingPricingInMemoryManager sortPricingInMemoryManager) {
        this.personInMemoryManager = personInMemoryManager;
        this.sortPricingInMemoryManager = sortPricingInMemoryManager;
    }


    @Override
    public ArrayList<Sale> saveSale(Weed weed, Integer quantity, String personName, Float discount,Float mySortPrice) {
        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(weed,quantity,person,discount,mySortPrice);
        saleArrayList.add(sale);
        return saleArrayList;
    }

    @Override
    public List<Sale> getAllSales() {
        return saleArrayList;
    }

    @Override
    public Float getWholeIncome() {
        float totalPrice = 0F;


        //For each sale find price for particular sort and quantity
        for (Sale sale: saleArrayList ) {
            float pricePerSale = 0F;
            SortPricing sortPricing  = sortPricingInMemoryManager.getSortPricingByWeedAndMyPrice(sale.getWeed(), sale.getMySortPrice());
            HashMap<Integer, Float> sortPricingMap =  sortPricing.getQuantityPriceMap();

                //Checking if sales quantity is standardized and if there is, multiply quantity by price assigned to it
            if(sortPricingMap.keySet().stream().anyMatch(k -> k.equals(sale.getQuantity()))){
                pricePerSale = sale.getQuantity() * sortPricingMap.get(sale.getQuantity()); //get price by quantity key
                logger.info("precised quantity : "+pricePerSale);
            }else{ //If quantity is not standardized take last bigger value
                Integer previousQuantity= 0;
                for (Integer quantityFromMap: sortPricingMap.keySet()) {
                    if(previousQuantity > sale.getQuantity()) break;

                    if (sale.getQuantity() > quantityFromMap ) {
                        previousQuantity = quantityFromMap;
                        pricePerSale = sale.getQuantity()*sortPricingMap.get(quantityFromMap);
                    }

                }logger.info("not precised: "+pricePerSale);
            }
            totalPrice+=pricePerSale;
        }logger.info("total: "+totalPrice);
        return totalPrice;
    }

    private Float getStandardized(){
        return

    }

    public Float getAllEarnings(){
        for(Sale sale: saleArrayList){
            SortPricing sortPricing  = sortPricingInMemoryManager.getSortPricingByWeedAndMyPrice(sale.getWeed(), sale.getMySortPrice());

        }
        return null;
    }

    public Float getEarnedMoneyByDay(){

        return null;
    }
}
