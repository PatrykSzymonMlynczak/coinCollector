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

    @PostMapping({"/{product}/{quantity}/{personName}/{discount}/{mySortPrice}", "/{product}/{quantity}/{personName}/{mySortPrice}"})
    public ArrayList<Sale> addSale(@PathVariable Product product,
                                   @PathVariable Integer quantity,
                                   @PathVariable String personName,
                                   @PathVariable(required = false) Float discount,
                                   @PathVariable Float mySortPrice){
        if(discount == null){
            discount = 0F;
        }
        return saleInMemoryManager.saveSale(product, quantity, personName, discount, mySortPrice);
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
