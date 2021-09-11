package com.example.demo.businessLogic.sale;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeMap;


@Data
@NoArgsConstructor
public class Sale implements Serializable {

    private Long id;
    //given data
    private Product product;
    private float quantity;
    private Person person;

    //optional
    private Float discount;

    // calculated data
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private float mySortPrice;
    private float earned;
    private float income;
    private float loss;

    public Sale(Product product, Float quantity, Person person, Float discount, Float givenMoney/*optional*/, String date) {

        this.product = product;
        this.quantity = quantity;
        this.person = person;

        if(date == null){
            this.transactionDate = LocalDate.now();
            this.transactionTime = LocalTime.now();

        }else {
            this.transactionDate = LocalDate.parse(date);
            this.transactionTime = LocalTime.of(0,0,0);

        }
        this.mySortPrice = product.getMyPrice();
        this.discount = (discount == null) ? 0F : discount;
        this.income = roundFloatToTwoDecimalPlaces(getIncomeByProductPricing(product.getQuantityPriceMap()));
        if(givenMoney != null) {
            /** when given money are smaller then price,
              * decrease income and increase debt
              * when given money will be bigger, additional money will decrease debt or make surplus*/
            Float debt = income - givenMoney;
            this.person.increaseDebt(debt);
            this.income -= debt;
        }
        this.loss = 0f;
        this.earned = roundFloatToTwoDecimalPlaces(income - (mySortPrice * quantity));
    }

    //Ignore surplus
    public Sale(Product product, Float quantity, Person person, String date) {

        this.product = product;
        this.quantity = quantity;
        this.person = person;

        if(date == null){
            this.transactionDate = LocalDate.now();
            this.transactionTime = LocalTime.now();

        }else {
            this.transactionDate = LocalDate.parse(date);
            this.transactionTime = LocalTime.of(0,0,0);

        }
        this.mySortPrice = product.getMyPrice();
        this.discount = (discount == null) ? 0F : discount;
        this.income = getIncomeByProductPricingIgnoreSurplus(product.getQuantityPriceMap(), Math.round(quantity));
        this.loss = getLoss(quantity);
        this.earned = roundFloatToTwoDecimalPlaces(income - loss - (mySortPrice * (quantity - (quantity%1))));

    }

    private float getLoss(Float quantity) {
        float fraction = quantity % 1;
        float loss = fraction * mySortPrice;
        return roundFloatToTwoDecimalPlaces(loss);
    }

    //for price checkout
    public Sale(Product product, Float quantity) {
        this.discount = 0f;
        this.product = product;
        this.quantity = quantity;
        this.income = getIncomeByProductPricing(product.getQuantityPriceMap());
        this.mySortPrice = product.getMyPrice();
        this.earned = income - (mySortPrice * quantity);
    }

    //In case of payed debt
    public Sale(Product product,Person person,Float payedMoney) {

        this.product = product;
        this.quantity = 0f;
        this.person = person;

        this.transactionTime = LocalTime.now();
        this.transactionDate = LocalDate.now();
        this.mySortPrice = 0f;
        this.discount = (discount == null) ? 0F : discount;
        this.income = getIncomeByProductPricing(product.getQuantityPriceMap());
        this.person.reduceDebt(payedMoney);
        this.income = payedMoney;
        this.earned = payedMoney;
    }

    private Float getIncomeByProductPricing(TreeMap<Float, Float> sortPricingMap ){
        Float pricePerSale = 0F;

        Float previousQuantity= 0F;
        //If quantity is not standardized take last bigger value
        for (Float quantityFromMap: sortPricingMap.keySet()) {
            if(previousQuantity > this.quantity) break;

            if (this.quantity >= quantityFromMap ) {
                previousQuantity = quantityFromMap;
                pricePerSale = this.quantity * ( sortPricingMap.get(quantityFromMap));
            }
        }
        pricePerSale -= discount;
        return pricePerSale;
    }

    private Float getIncomeByProductPricingIgnoreSurplus(TreeMap<Float, Float> sortPricingMap, Integer roundedQuantity ){
        Float pricePerSale = 0F;

        Float previousQuantity= 0F;
        //If quantity is not standardized take last bigger value
        for (Float quantityFromMap: sortPricingMap.keySet()) {
            if(previousQuantity > Math.round(roundedQuantity)) break;

            if (roundedQuantity >= quantityFromMap ) {
                previousQuantity = quantityFromMap;
                pricePerSale = roundedQuantity * ( sortPricingMap.get(quantityFromMap));
            }
        }
        pricePerSale -= discount;
        return pricePerSale;
    }

    private Float roundFloatToTwoDecimalPlaces(Float f) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }
}
