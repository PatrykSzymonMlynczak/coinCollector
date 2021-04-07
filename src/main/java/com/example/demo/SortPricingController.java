package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class SortPricingController {

    SortPricingRepo sortPricingInMemoryManager;

    @GetMapping("/weed")
    public HashMap<HashMap<Float,String>, SortPricing> getWeed(){
        return sortPricingInMemoryManager.loadAllProductsToMemory();
    }

    @PostMapping("/{product}/{myPrice}")
    public SortPricing addNewWeed(@PathVariable String product, @PathVariable Float myPrice,
                           @RequestBody HashMap<Integer,Float> priceQuantityMap)  {
        SortPricing sortPricing = new SortPricing(product, priceQuantityMap, myPrice);

        System.out.println(sortPricing.toString());
        return sortPricingInMemoryManager.saveProduct(sortPricing);

    }

}
