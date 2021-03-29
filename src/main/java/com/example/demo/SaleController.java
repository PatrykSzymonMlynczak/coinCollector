package com.example.demo;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    SaleInMemoryManager saleInMemoryManager;

    @PostMapping("/{weed}/{quantity}/{personName}")
    public void addSale(@PathVariable Weed weed, @PathVariable Integer quantity, @PathVariable String personName){

        saleInMemoryManager.saveSale(weed, quantity, personName);
    }

    @GetMapping()
    public Float getEarnings(){
        return saleInMemoryManager.getAllEarnedMoney();
    }

    @GetMapping("/all")
    public List<Sale> getAllSales(){
        return saleInMemoryManager.getAllSales();
    }


}
