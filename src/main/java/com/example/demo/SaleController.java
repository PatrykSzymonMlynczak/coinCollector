package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    SaleManager saleManager;

    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}", "/{productName}/{mySortPrice}/{quantity}/{personName}"})
    public ArrayList<Sale> addSale(@PathVariable String productName,
                                   @PathVariable Float quantity,
                                   @PathVariable String personName,
                                   @PathVariable(required = false) Float discount,
                                   @PathVariable Float mySortPrice,
                                   @RequestParam(required = false) Float money){
        if(discount == null) discount = 0F;
        return saleManager.saveSale(productName, quantity, personName, discount, mySortPrice, money);
    }

    @GetMapping("/income")
    public Float getIncome(){
        return saleManager.getTotalIncome();
    }

    @GetMapping("/all")
    public List<Sale> getAllSales(){
        return saleManager.loadAllSales();
    }

    @GetMapping("/earnings")
    public Float getTotalEarnings(){
        return saleManager.getTotalEarnings();
    }

    @GetMapping("/cost")
    public Float getTotalCost(){
        return saleManager.getTotalCost();
    }

    @GetMapping("/date/{date}")
    public Float getEarnedMoneyByDay(@PathVariable String date){
        return saleManager.getEarnedMoneyByDay(date);
    }

    @GetMapping("/week/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(@PathVariable String dateStart,
                                      @PathVariable String dateEnd){
        return  saleManager.getEarnedMoneyByWeek(dateStart,dateEnd);
    }

    @DeleteMapping("/all")
    public List<Sale> clearAllSales(){
        return saleManager.clearAllSales();
    }

}
