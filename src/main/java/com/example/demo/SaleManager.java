package com.example.demo;

import com.example.demo.exceptions.SortPricingNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class SaleManager implements SaleRepo, ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(SaleManager.class);

    private final PersonInMemoryManager personInMemoryManager;
    private final ProductManager sortPricingInMemoryManager;
    private final JsonFileManager jsonFileManager;

    private ArrayList<Sale> saleArrayList = new ArrayList<>();


    @Autowired
    public SaleManager(PersonInMemoryManager personInMemoryManager,
                       JsonFileManager jsonFileManager,
                       ProductManager sortPricingInMemoryManager) {
        this.personInMemoryManager = personInMemoryManager;
        this.sortPricingInMemoryManager = sortPricingInMemoryManager;
        this.jsonFileManager = jsonFileManager;
    }

    @Override
    public ArrayList<Sale> saveSale(String product, Integer quantity, String personName, Float discount, Float mySortPrice) {
        saleArrayList = (ArrayList<Sale>) jsonFileManager.readSaleListFromFile();

        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(product,quantity,person,discount,mySortPrice);

        if( sortPricingInMemoryManager.getSortPricingByProductAndMyPrice(sale.getProductName(), sale.getMySortPrice()) != null){
            saleArrayList.add(sale);
        }else throw new SortPricingNotExistException(product,mySortPrice);

        jsonFileManager.saveSaleToFileAsJson(sale);

        return saleArrayList;
    }

    @Override
    public ArrayList<Sale> loadAllSales() {
        saleArrayList = (ArrayList<Sale>) jsonFileManager.readSaleListFromFile();
        return saleArrayList;
    }

    @Override
    public Float getWholeIncome() {
        float totalPrice = 0F;

        //For each sale find price for particular sort and quantity
        for (Sale sale: saleArrayList ) {
            float pricePerSale;
            Product product = sortPricingInMemoryManager.getSortPricingByProductAndMyPrice(sale.getProductName(), sale.getMySortPrice());
            HashMap<Integer, Float> sortPricingMap = product.getQuantityPriceMap();

            //Checking if sales quantity is standardized and if there is, multiplication quantity by price assigned to it
            if(sortPricingMap.keySet().stream().anyMatch(k -> k.equals(sale.getQuantity()))){
                pricePerSale = getStandardQuantityIncome(sale,sortPricingMap);
                logger.info("precised quantity : "+pricePerSale);
            }else{
                pricePerSale = getCustomQuantityIncome(sale,sortPricingMap);
                logger.info("not precised quantity: "+pricePerSale);
            }
            totalPrice += pricePerSale;
        }logger.info("total income: "+totalPrice);
        return totalPrice;
    }

    public Float getTotalEarnings() {
        Float totalSaleCost = 0F;
        for(Sale sale: saleArrayList){
            totalSaleCost += sale.getMySortPrice()*sale.getQuantity();
        }logger.info("total earnings: " + (getWholeIncome() - totalSaleCost));
        getEarnedMoneyByDay();
        return getWholeIncome() - totalSaleCost;
    }

    public void getEarnedMoneyByDay(){
        Float totalSaleCost = 0F;

        LocalDate localDate = LocalDate.now();
        for(Sale sale: saleArrayList){
            if( sale.getTransactionDate().toLocalDate().equals(localDate) ) {
                totalSaleCost += sale.getMySortPrice() * sale.getQuantity();
            }
        }
        logger.info("  by day: "+": total = "+totalSaleCost);
    }

    private Float getStandardQuantityIncome(Sale sale, HashMap<Integer,Float> sortPricingMap){
        Integer priceOverride = getPriceOverrideForStandard(sale);

        return sale.getQuantity() * ( sortPricingMap.get(sale.getQuantity()) + priceOverride); //get price by quantity key
    }

    //If quantity is not standardized take last bigger value
    private Float getCustomQuantityIncome(Sale sale, HashMap<Integer, Float> sortPricingMap ){
        Integer priceOverride = getPriceOverrideForStandard(sale);
        Float pricePerSale = 0F;

        Integer previousQuantity= 0;
        for (Integer quantityFromMap: sortPricingMap.keySet()) {
            if(previousQuantity > sale.getQuantity()) break;

            if (sale.getQuantity() > quantityFromMap ) {
                previousQuantity = quantityFromMap;
                pricePerSale = sale.getQuantity() * ( sortPricingMap.get(quantityFromMap) + priceOverride);
            }
        }
        return pricePerSale;
    }

    //todo -> in real time overriding
    private Integer getPriceOverrideForStandard(Sale sale){
        if (sale.getProductName().equals("STANDARD")){
            Integer priceOverride = sale.getPerson().getPricePerGramOverride();

            logger.info("price per gram override :" + priceOverride);
            return priceOverride;

        }else return 0;
    }

    @Override
    public void run(ApplicationArguments args){
        loadAllSales();
    }
}
