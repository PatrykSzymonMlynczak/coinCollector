package com.example.demo;

 import com.google.gson.Gson;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 import java.io.*;
 import java.util.*;
 import java.util.stream.Collectors;

@Service
public class SaleInMemoryManager implements SaleRepo {

    private final Logger logger = LoggerFactory.getLogger(SaleInMemoryManager.class);

    private final PersonInMemoryManager personInMemoryManager;
    private final SortPricingPricingInMemoryManager sortPricingInMemoryManager;

    private ArrayList<Sale> saleArrayList = new ArrayList<>();


    @Autowired
    public SaleInMemoryManager(PersonInMemoryManager personInMemoryManager, SortPricingPricingInMemoryManager sortPricingInMemoryManager) {
        this.personInMemoryManager = personInMemoryManager;
        this.sortPricingInMemoryManager = sortPricingInMemoryManager;
    }

    public List<Sale> readSaleListFromFileAndAddNewSale(Sale newSale){
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

        salelist.add(newSale);
        return salelist;

    }


    public void saveToFileAsJson(Sale newSale){
        FileWriter fileWriter;
        String serialized = new Gson().toJson(readSaleListFromFileAndAddNewSale(newSale));

        try {
            fileWriter = new FileWriter("sale.json", false);

            fileWriter.write(serialized);
            fileWriter.flush();
            fileWriter.close();
            logger.info("Successfully Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Sale> saveSale(String product, Integer quantity, String personName, Float discount, Float mySortPrice) {
        Person person = personInMemoryManager.getAllPerson().stream().filter(p -> p.getName().equals(personName)).findAny().get();
        Sale sale = new Sale(product,quantity,person,discount,mySortPrice);

        if( sortPricingInMemoryManager.getSortPricingByProductAndMyPrice(sale.getProductName(), sale.getMySortPrice()) != null){
            saleArrayList.add(sale);
        }else throw new SortPricingNotExistException(product,mySortPrice);

        saveToFileAsJson(sale);

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
            float pricePerSale;
            SortPricing sortPricing = sortPricingInMemoryManager.getSortPricingByProductAndMyPrice(sale.getProductName(), sale.getMySortPrice());
            HashMap<Integer, Float> sortPricingMap = sortPricing.getQuantityPriceMap();

                //Checking if sales quantity is standardized and if there is, multiply quantity by price assigned to it
            if(sortPricingMap.keySet().stream().anyMatch(k -> k.equals(sale.getQuantity()))){
                pricePerSale = getStandardQuantityIncome(sale,sortPricingMap);
                logger.info("precised quantity : "+pricePerSale);
            }else{ //If quantity is not standardized take last bigger value
                pricePerSale = getCustomQuantityIncome(sale,sortPricingMap);
                logger.info("not precised quantity: "+pricePerSale);
            }
            totalPrice += pricePerSale;
        }logger.info("total income: "+totalPrice);
        return totalPrice;
    }

    public Float getEarnedMoneyByDay(){

        return null;
    }

    public Float getStandardQuantityIncome(Sale sale, HashMap<Integer,Float> sortPricingMap){
        Integer priceOverride = getPriceOverrideForStandard(sale);

        return sale.getQuantity() * ( sortPricingMap.get(sale.getQuantity()) + priceOverride); //get price by quantity key
    }

    public Float getCustomQuantityIncome(Sale sale, HashMap<Integer, Float> sortPricingMap ){
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
    public Integer getPriceOverrideForStandard(Sale sale){
        if (sale.getProductName().equals("STANDARD")){
            Integer priceOverride = sale.getPerson().getPricePerGramOverride();

            logger.info("price per gram override :" + priceOverride);
            return priceOverride;

        }else return 0;
    }

    public Float getTotalEarnings() {
        Float totalSaleCost = 0F;
        for(Sale sale: saleArrayList){
            totalSaleCost += sale.getMySortPrice()*sale.getQuantity();
        }logger.info("total earnings: " + (getWholeIncome() - totalSaleCost));
        return getWholeIncome() - totalSaleCost;
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



      private void saveSaleAsJSON(Sale sale){
        FileWriter fileWriter;
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




        try {
            fileWriter = new FileWriter("sale.json", true);

            fileWriter.write(jsonSaleObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
            logger.info("Successfully Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void readJSOnFile(){
        // parsing file "JSONExample.json"
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader("sale.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        // getting firstName and lastName
        String product = (String) jo.get("product");

        logger.info(product);

    }

*/

}
