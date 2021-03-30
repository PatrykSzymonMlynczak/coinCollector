package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@AllArgsConstructor
public class SortPricingController {

    SortPricingPricingInMemoryManager sortPricingInMemoryManager;

    @GetMapping("/weed")
    public String getWeed(){
        return sortPricingInMemoryManager.getAll().toString();
    }

    @PostMapping("/{weed}/{myPrice}")
    public void addNewWeed(@PathVariable Weed weed, @PathVariable Float myPrice,
                           @RequestBody HashMap<Integer,Float> priceQuantityMap) throws IOException {
        SortPricing sortPricing = new SortPricing(weed, priceQuantityMap, myPrice);

   /*     for(int i=0; i<=100; i++){
            if(priceCapacityMap.containsKey())
        }*/
        System.out.println(sortPricing.toString());
        sortPricingInMemoryManager.save(sortPricing);

/*      Save to file

        FileOutputStream outputStream = new FileOutputStream("weed.txt");
        byte[] strToBytes = sort.toString().getBytes();
        outputStream.write(strToBytes);

        outputStream.close();*/
    }

}
