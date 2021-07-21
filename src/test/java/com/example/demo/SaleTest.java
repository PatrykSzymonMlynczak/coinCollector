package com.example.demo;


import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

//todo proper test names - should !
public class SaleTest {

    TreeMap<Float, Float> quantityPriceMap = new TreeMap<>();
    Product product;
    Person person;

    @Test
    public void ccc(){
        String str = "amoaaoma";

        int right=str.length()-1;
        int left=0;
        boolean isPalindrom = true;
        do{
            if (str.charAt(left) != str.charAt(right)) {
                System.out.println(left + " != "+ right );
                isPalindrom = false;
                break;
            }else {left++; right--;}
        }while(left<=right);
        System.out.println("Is "+str+" palindrome ? : "+isPalindrom);

      //paindrom petla for:

/*        int right=str.length()-1;
        int left=0;
        for (left =0; left <= right; left++){
            if (str.charAt(left) != str.charAt(right)) {
                System.out.println("to nie palindrom  -> " + str.charAt(left) + " != "+ str.charAt(right) );
                break;
            }else right--;
        }
        if (left>=right) System.out.println("Paindrom");*/
    }

    @Test
    public void cccc(){
        List<Integer> tab = Arrays.asList(3,4,51,2,3,4,5);
        for (int i: tab ) {
            System.out.print(i +",");
        }
        tab.sort(Comparator.naturalOrder());
        System.out.println("two biggest numbers: " +tab.get(tab.size()-1)+" and "+ tab.get(tab.size()-2));



/*        Integer tab2[] = {3,4,51,2,3,4,5};
        Arrays.sort(tab2);

        System.out.println("Sorted:");
        for (int i: tab ) {
            System.out.print(i +",");
        }
        System.out.println("two biggest numbers: " +tab2[tab2.length-1]+" and "+ tab2[tab2.length-2]);*/
    }

    @Test
    public void anagram(){

        char[] first = "bebak".toCharArray();
        char[] second = "kebab".toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        if(Arrays.equals(first,second)) System.out.println("equal");
        else System.out.println("not equal");


/*        for(char e : second.toCharArray()){
            if(map.containsKey(e))
                map.get(e)
        }*/

    }

    public void fibonacci() {

    }

        @Test
    public void check_calculation_when_given_money_are_less_than_price(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");


        //when
        Sale newSale = new Sale(product,3F, person,0F,30F);

        //then
        Assertions.assertEquals(newSale.getIncome(),30F);
        Assertions.assertEquals(newSale.getEarned(),0F);
        Assertions.assertEquals(person.getDebt(),-30F);
    }

    @Test
    public void check_calculation_when_given_money_are_less_than_price_and_there_is_discount(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,20F,30F);

        //then
        Assertions.assertEquals(newSale.getIncome(),30F);
        Assertions.assertEquals(newSale.getEarned(),0F);
        Assertions.assertEquals(person.getDebt(),-10F);
    }

    @Test
    public void check_if_given_money_and_discount_can_be_null(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,null,null);

        //then
        Assertions.assertEquals(newSale.getIncome(),60F);
    }

    @Test
    public void check_calculation_when_is_discount_and_given_money_are_null(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,20F,null);

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
        Sale newSale = new Sale(product,3F, person,0F,30F);
        Sale newSale2 = new Sale(product,3F, person,0F,30F);

        //then
        Assertions.assertEquals(person.getDebt(),-60F);
    }

    @Test
    public void check_more_given_money_increase_income_and_earnings(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,0F,70F);

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
        Sale newSale = new Sale(product,3F, person,0F,30F);
        Assertions.assertEquals(person.getDebt(),-30);

        //when - then
        Sale newSale2 = new Sale(product,3F, person,0F,90F); //debt payed
        Assertions.assertEquals(person.getDebt(),0);
    }

    @Test
    public void should_not_decrease_debt_when_price_is_increased(){
        //given
        quantityPriceMap.put(1F,20F);
        product = new Product("STANDARD",quantityPriceMap, 10F);
        person = new Person("Zamor");

        //when
        Sale newSale = new Sale(product,3F, person,-10F,null);

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
        Sale newSale = new Sale(product,10F, person,null,null);
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
        Sale newSale = new Sale(product,11F, person,null,null);
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
        Sale newSale = new Sale(product,11F, person,null,null);
        //then
        Assertions.assertEquals(newSale.getIncome(), 110F);
    }
}
