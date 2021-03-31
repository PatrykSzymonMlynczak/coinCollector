package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    SaleInMemoryManager saleInMemoryManager;

    @PostMapping({"/{weed}/{quantity}/{personName}/{discount}/{mySortPrice}", "/{weed}/{quantity}/{personName}/{mySortPrice}"})
    public ArrayList<Sale> addSale(@PathVariable Weed weed,
                                   @PathVariable Integer quantity,
                                   @PathVariable String personName,
                                   @PathVariable(required = false) Float discount,
                                   @PathVariable Float mySortPrice){
        if(discount == null){
            discount = 0F;
        }
        return saleInMemoryManager.saveSale(weed, quantity, personName, discount, mySortPrice);
    }

    @GetMapping("/income")
    public Float getIncome(){
        return saleInMemoryManager.getWholeIncome();
    }

    @GetMapping("/all")
    public List<Sale> getAllSales(){
        return saleInMemoryManager.getAllSales();
    }
    @GetMapping("/earnings")
    public Float getTotalEarnings(){
        return saleInMemoryManager.getTotalEarnings();
    }


}
