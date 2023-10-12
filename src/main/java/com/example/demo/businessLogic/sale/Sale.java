package com.example.demo.businessLogic.sale;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeMap;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale implements Serializable {

    private Long id;
    //given data
    private Product product;
    private float sortAmountBefore;

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

    /**
     * when money not given - not earned
     * */
    public static Sale createManualSale(Product product, Float quantity, Person person, Float manualPrice, Float givenMoney, String date){
        float income = manualPrice;
        float mySortPrice = product.getMyPrice();
        if(givenMoney != null) {
            /** when given money are smaller than price,
             * decrease income and increase debt
             * when given money will be bigger, additional money will decrease debt or make surplus*/
            Float debt = manualPrice - givenMoney;
            person.increaseDebt(debt);
            income -= debt;
        }

        return Sale.builder()
                .product(product)
                .sortAmountBefore(product.getTotalSortAmount()+quantity)
                .quantity(quantity)
                .person(person)
                .transactionDate(date == null || date.equals("undefined") ? LocalDate.now() : LocalDate.parse(date))
                .mySortPrice(mySortPrice)
                .income(income)
                .loss(0f)
                .earned(roundFloatToTwoDecimalPlaces(income - (mySortPrice * quantity)))
                .sortAmountBefore(product.getTotalSortAmount()+quantity)
                .build();
    }

    public static Sale createSaleNotIgnoringSurplus(Product product, Float quantity, Person person, Float discount, Float givenMoney, String date) {
        discount = (discount == null) ? 0F : discount;
        float income = roundFloatToTwoDecimalPlaces(getIncomeByProductPricingNotIgnoreSurplus(product.getQuantityPriceMap(), quantity, discount));
        float mySortPrice = product.getMyPrice();
        if(givenMoney != null) {
            /** when given money are smaller than price,
             * decrease income and increase debt
             * when given money will be bigger, additional money will decrease debt or make surplus*/
            Float debt = income - givenMoney;
            person.increaseDebt(debt);
            income -= debt;
        }
        return Sale.builder()
                .product(product)
                .sortAmountBefore(product.getTotalSortAmount()+quantity)
                .quantity(quantity)
                .person(person)
                .transactionDate(date == null || date.equals("undefined") ? LocalDate.now() : LocalDate.parse(date))
                .mySortPrice(product.getMyPrice())
                .discount(discount)
                .income(income)
                .mySortPrice(mySortPrice)
                .loss(0f)
                .earned(roundFloatToTwoDecimalPlaces(income - (mySortPrice * quantity)))
                .sortAmountBefore(product.getTotalSortAmount()+quantity)
                .build();
    }

    private void setNowDateIfNotExist(boolean date, String date1) {
        if(date){
            this.transactionDate = LocalDate.now();
            this.transactionTime = LocalTime.now();

        }else {
            this.transactionDate = LocalDate.parse(date1);
            this.transactionTime = LocalTime.of(0,0,0);
        }
    }

    public Sale(Product product, Float quantity, Person person, Float discount, Float givenMoney, String date){

        this.product = product;
        this.sortAmountBefore = product.getTotalSortAmount()+quantity;
        this.quantity = quantity;
        this.person = person;

        setNowDateIfNotExist(date == null || date.equals("undefined"), date);
        this.mySortPrice = product.getMyPrice();
        this.discount = (discount == null) ? 0F : discount;
        this.income = getIncomeByProductPricingIgnoreSurplus(product.getQuantityPriceMap(), (int) Math.floor(quantity));
        if(givenMoney != null) {
            /** when given money are smaller than price,
             * decrease income and increase debt
             * when given money will be bigger, additional money will decrease debt or make surplus*/
            Float debt = income - givenMoney;
            this.person.increaseDebt(debt);
            this.income -= debt;
        }
        this.loss = getLoss(quantity);
        this.earned = roundFloatToTwoDecimalPlaces(income - loss - (mySortPrice * (quantity - (quantity%1))));

    }

    private float getLoss(Float quantity) {
        float fraction = quantity % 1;
        float loss = fraction * mySortPrice;
        return roundFloatToTwoDecimalPlaces(loss);
    }

    //for price checkout
    public static Sale priceCheckoutSale(Product product, Float quantity) {
        return Sale.builder()
                .income(getIncomeByProductPricingNotIgnoreSurplus(product.getQuantityPriceMap(), quantity, 0f))
                .build();
    }

    //In case of payed debt
    public Sale(Product product,Person person,Float payedMoney) {

        this.product = product;
        this.quantity = 0f;
        this.person = person;

        this.transactionTime = LocalTime.now();
        this.transactionDate = LocalDate.now();
        this.mySortPrice = 0f;
        this.discount = 0F;
        this.person.reduceDebt(payedMoney);
        this.income = payedMoney;
        this.earned = payedMoney;
    }

    private static Float getIncomeByProductPricingNotIgnoreSurplus(TreeMap<Float, Float> sortPricingMap, float quantity, float discount){
        Float pricePerSale = 0F;

        Float previousQuantity= 0F;
        //If quantity is not standardized take last bigger value
        for (Float quantityFromMap: sortPricingMap.keySet()) {
            if(previousQuantity > quantity) break;

            if (quantity >= quantityFromMap ) {
                previousQuantity = quantityFromMap;
                pricePerSale = quantity * ( sortPricingMap.get(quantityFromMap));
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

    private static Float roundFloatToTwoDecimalPlaces(Float f) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }
}
