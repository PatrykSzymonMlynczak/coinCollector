package com.example.demo.businessLogic.sale;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeMap;


@Data
@NoArgsConstructor
public class Sale implements Serializable {

    //given data
    private Product product;
    private Float quantity;
    private Person person;

    //optional
    private Float discount;

    // calculated data
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private Float mySortPrice;
    private Float earned;
    private Float income;

    public Sale(Product product, Float quantity, Person person, Float discount, Float givenMoney/*optional*/) {

        this.product = product;
        this.quantity = quantity;
        this.person = person;

        this.transactionTime = LocalTime.now();
        this.transactionDate = LocalDate.now();
        this.mySortPrice = product.getMyPrice();
        this.discount = (discount == null) ? 0F : discount;
        this.income = getIncomeByProductPricing(product.getQuantityPriceMap());
        if(givenMoney != null) {
            /** when given money are smaller then price,
              * decrease income and increase debt
              * when given money will be bigger, additional money will decrease debt or make surplus*/
            Float debt = income - givenMoney;
            this.person.increaseDebt(debt);
            this.income -= debt;
        }
        this.earned = income - (mySortPrice * quantity);
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
}
