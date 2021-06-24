package com.example.demo.businessLogic.sale;

import com.example.demo.dto.SaleDto;

import java.util.List;

public interface SaleRepo {

    Sale saveSale(String product, Float quantity, String personName, Float discount, Float mySortPrice, Float money);

    List<Sale> loadAllSales();

    Float getTotalEarnings();

    Float getTotalCost();

    Float getTotalIncome();

    Float getEarnedMoneyByDay(String date);

    Float getEarnedMoneyByWeek(String dateStart,String dateEnd);

    List<Sale> clearAllSales();

}
