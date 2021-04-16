package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
public class Sale implements Serializable {

    //given data
    private final Product product;
    private final Float quantity;
    private final Person person;
    private final Float discount;
    private final Float givenMoney;
    @JsonIgnore
    private final Float mySortPrice;

    // calculated data
    private final Float earned;
    private final Float income;
    private final LocalDateTime transactionDate;


    public Sale(Product product, Float quantity, Person person, Float discount, Float mySortPrice, Float givenMoney) {

        this.product = product;
        this.quantity = quantity;
        this.person = person;
        this.transactionDate = LocalDateTime.now();
        this.discount = discount;
        this.mySortPrice = mySortPrice;
        this.income = getIncomeByProductPricing(product.getQuantityPriceMap());

        if(givenMoney == null) { // if givenMoney are not given, we assume that they are equal to income
            this.givenMoney = income;
            this.earned = income - (mySortPrice * quantity);
        }else { // when given money are smaller then price, decrease earnings
            Float debt = income - givenMoney;

            this.givenMoney = givenMoney;
            this.earned = income - (mySortPrice * quantity) - debt;
            this.person.increaseDebt(debt);
        }
    }

    private Float getIncomeByProductPricing(HashMap<Float, Float> sortPricingMap ){
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
}
