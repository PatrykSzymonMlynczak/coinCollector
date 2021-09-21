package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.sale.Sale;

import java.util.ArrayList;
import java.util.List;

public interface SaleRepo {

    Sale saveSale(String product, Float quantity, String personName, Float discount, Float money, String date);

    Sale saveSaleIgnoringSurplus(String productName, Float quantity, String personName, String date);

    Float getEarningsWithoutSpecifiedProductName(String name);

    Float getEarningsWithSpecifiedProductName(String name);

    Float getTotalEarnings();

    Float getTotalCost();

    Float getTotalIncome();

    Float getEarnedMoneyByDay(String date);

    Float getEarnedMoneyByPeriod(String dateStart, String dateEnd);

    List<Sale> loadAllSales();

    Float priceCheckout(String productName, Float quantity);

    void deleteLastSale();

    void deleteById(Long id);

    Sale getLastSale();

    List<Sale> getSalesByPeriod(String dateStart, String dateEnd);

    List<Sale> getSalesByDay(String date);

    default List<Sale> clearAllSales() {
        return new ArrayList<>();
    }

}
