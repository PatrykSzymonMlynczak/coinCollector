package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.sale.Sale;

import java.util.ArrayList;
import java.util.List;

public interface SaleRepo {

    Sale saveSale(String product, Float quantity, String personName, Float discount, Float mySortPrice, Float money);

    Float getTotalEarnings();

    Float getTotalCost();

    Float getTotalIncome();

    Float getEarnedMoneyByDay(String date);

    Float getEarnedMoneyByWeek(String dateStart,String dateEnd);

    default List<Sale> clearAllSales(){
        return new ArrayList<>();
    }

    default List<Sale> loadAllSales(){
        return new ArrayList<>();
    }

}
