package com.example.demo.unitTest;


import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.truth.Truth.assertThat;

public class SaleTest {

    TreeMap<Float, Float> quantityPriceMap = new TreeMap<>();
    Product product;
    Person person;

    @Test
    public void check_calculation_when_given_money_are_less_than_price(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,0F,30F, "2021-09-09");

        //then
        Assertions.assertEquals(newSale.getIncome(),30F);
        Assertions.assertEquals(newSale.getEarned(),0F);
        Assertions.assertEquals(person.getDebt(),30F);
    }

    @Test
    public void check_calculation_when_given_money_are_less_than_price_and_there_is_discount(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,20F,30F, "2021-09-09");

        //then
        Assertions.assertEquals(newSale.getIncome(),30F);
        Assertions.assertEquals(newSale.getEarned(),0F);
        Assertions.assertEquals(person.getDebt(),10F);
    }

    @Test
    public void check_if_given_money_and_discount_can_be_null(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,null,null, "2021-09-09");

        //then
        Assertions.assertEquals(newSale.getIncome(),60F);
    }

    @Test
    public void check_calculation_when_is_discount_and_given_money_are_null(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F );
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,20F,null, "2021-09-09");

        //then
        Assertions.assertEquals(newSale.getIncome(),40F);
        Assertions.assertEquals(newSale.getEarned(),10F);
        Assertions.assertEquals(person.getDebt(),0F);
    }

    @Test
    public void check_if_debt_is_cumulating(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,0F,30F, "2021-09-09");
        Sale newSale2 = new Sale(product,3F, person,0F,30F, "2021-09-09");

        //then
        Assertions.assertEquals(person.getDebt(),60F);
    }

    @Test
    public void check_more_given_money_increase_income_and_earnings(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,0F,70F, "2021-09-09");

        //then
        Assertions.assertEquals(newSale.getIncome(),70F);
        Assertions.assertEquals(newSale.getEarned(),40F);
    }

    @Test
    public void check_if_debt_decreases_when_given_money_are_more(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when - then
        Sale newSale = new Sale(product,3F, person,0F,30F, "2021-09-09");
        Assertions.assertEquals(person.getDebt(),30);

        //when - then
        Sale newSale2 = new Sale(product,3F, person,0F,90F, "2021-09-09"); //debt payed
        Assertions.assertEquals(person.getDebt(),0);
    }

    @Test
    public void should_not_decrease_debt_when_price_is_increased(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,-10F,null, "2021-09-09");

        //then
        Assertions.assertEquals(person.getDebt(),0);
    }

    @Test
    public void should_decrease_price_when_bought_more(){
        //given
        quantityPriceMap.put(1F,20F);
        quantityPriceMap.put(10F,10F);
        product = new Product("STANDARD",quantityPriceMap, 6F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,10F, person,null,null, "2021-09-09");
        //then
        Assertions.assertEquals(newSale.getIncome(), 100F);
    }

    @Test
    public void should_take_proper_price_and_not_understate_price(){
        //given
        quantityPriceMap.put(1F,20F);
        quantityPriceMap.put(10F,10F);
        quantityPriceMap.put(20F,8F);

        product = new Product("STANDARD",quantityPriceMap, 8F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,11F, person,null,null, "2021-09-09");
        //then
        Assertions.assertEquals(newSale.getIncome(), 110F);
    }

    @Test
    public void should_take_proper_price_and_not_understate_price_with_wrong_sortPricing_order(){
        //given
        quantityPriceMap.put(1F,20F);
        quantityPriceMap.put(20F,8F);
        quantityPriceMap.put(10F,10F);

        product = new Product("STANDARD",quantityPriceMap, 8F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,11F, person,null,null, "2021-09-09");
        //then
        Assertions.assertEquals(newSale.getIncome(), 110F);
    }

    @Test
    public void testGettingQuantityByPrice(){
        quantityPriceMap.put(1F,50F);
        quantityPriceMap.put(5F,40F);
        quantityPriceMap.put(10F,30F);

        float x = Sale.getQuantityByPrice(new Product("xx", quantityPriceMap, 20), 200f);
        assertThat(x).isEqualTo(5);

        float y = Sale.getQuantityByPrice(new Product("xx", quantityPriceMap, 20), 100f);
        assertThat(y).isEqualTo(2);

        float z = Sale.getQuantityByPrice(new Product("xx", quantityPriceMap, 20), 1000f);
        assertThat(z).isEqualTo(33.33f);
    }

}
