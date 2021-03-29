package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private ArrayList<Sale> saleArrayList;


    @Autowired
    public SaleInMemoryManager(PersonInMemoryManager personInMemoryManager, SortPricingPricingInMemoryManager sortPricingInMemoryManager, ArrayList<Sale> saleArrayList) {
        this.personInMemoryManager = personInMemoryManager;
        this.sortPricingInMemoryManager = sortPricingInMemoryManager;
        this.saleArrayList = saleArrayList;
    }


    @Override
    public void saveSale(Weed weed, Integer quantity, String personName) {
        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(weed,quantity,person);
        saleArrayList.add(sale);
    }

    @Override
    public List<Sale> getAllSales() {
        return saleArrayList;
    }

    @Override
    public Float getAllEarnedMoney() {
        Float totalPrice = 0F;

        //For each sale find  price for particular sort and quantity
        for (Sale sale: saleArrayList ) {
            SortPricing sortPricing  = sortPricingInMemoryManager.getSortPricingByWeed(sale.getWeed());
            HashMap<Integer, Float> sortPricingMap =  sortPricing.getQuantityPriceMap();
            //Find price for  quantity
            Float pricePerSale = 0F;
            Integer backQuantity = 0;
            //For each quantity in sort pricing map
            for (Integer quantityFromMap: sortPricingMap.keySet()) {
                //chek if price is strictly precised just multiple values
                if (quantityFromMap == sale.getQuantity()) {
                    pricePerSale = quantityFromMap*sortPricingMap.get(sale.getQuantity()); //get price by quantity key
                }else if(quantityFromMap < sale.getQuantity()){
                    backQuantity = quantityFromMap;
                     //  logger.info("\n form sortPriceMap: "+quantityFromMap +" +realQuantity : " +sale.getQuantity());
                    //continue;
                }
                //get price by backQuantity key and multiply by sale quantity
                logger.info("\n form sortPriceMap: "+quantityFromMap +" +realQuantity : " +sale.getQuantity()+"back quantity : "+ backQuantity);
                pricePerSale = sale.getQuantity()*sortPricingMap.get(backQuantity);




                /*else{
                    if(quantityFromMap<sale.getQuantity()) {
                        backQuantity = quantityFromMap;
                        continue;
                    }else{
                        pricePerSale = sale.getQuantity()*sortPricingMap.get(backQuantity); //get price by quantity key

                    }
                  }*/ //todo -> different quantities


            }
            totalPrice+=pricePerSale;

        }
        System.out.println(totalPrice);

        //saleArrayList.forEach(sale -> sale.getWeed().name());

        //.stream().filter(sale -> sale.getWeed().name().equals(sortInMemoryManager.inMemorySortList)).
        //sortInMemoryManager.getSortByWeed(weed)
        return null;
    }
}
