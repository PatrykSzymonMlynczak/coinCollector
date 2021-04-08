package com.example.demo;

import com.example.demo.exceptions.ProductNotExistException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
public class Sale implements Serializable {

    private final Product product;
    private final Float quantity;
    private final Person person;
    private final LocalDateTime transactionDate;
    private final Float discount;
    private final Float earned;

    @JsonIgnore
    private final Float mySortPrice;

    public Sale(Product product, Float quantity, Person person, Float discount, Float mySortPrice) {
        this.product = product;
        this.quantity = quantity;
        this.person = person;
        this.transactionDate = LocalDateTime.now();
        this.discount = discount;
        this.mySortPrice = mySortPrice;
        this.earned = getIncomeByProductPricing(product.getQuantityPriceMap()) - (mySortPrice*quantity);
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
