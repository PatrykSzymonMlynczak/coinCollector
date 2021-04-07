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

    @PostMapping({"/{product}/{quantity}/{personName}/{discount}/{mySortPrice}", "/{product}/{quantity}/{personName}/{mySortPrice}"})
    public ArrayList<Sale> addSale(@PathVariable String product,
                                   @PathVariable Integer quantity,
                                   @PathVariable String personName,
                                   @PathVariable(required = false) Float discount,
                                   @PathVariable Float mySortPrice){
        if(discount == null){
            discount = 0F;
        }
        return saleManager.saveSale(product, quantity, personName, discount, mySortPrice);
    }

    @GetMapping("/income")
    public Float getIncome(){
        return saleManager.getWholeIncome();
    }

    @GetMapping("/all")
    public List<Sale> getAllSales(){
        return saleManager.loadAllSales();
    }
    @GetMapping("/earnings")
    public Float getTotalEarnings(){
        return saleManager.getTotalEarnings();
    }


}
