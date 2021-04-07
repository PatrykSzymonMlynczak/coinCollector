package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class ProductController {

    ProductRepo sortPricingInMemoryManager;

    @GetMapping("/weed")
    public HashMap<HashMap<Float,String>, Product> getWeed(){
        return sortPricingInMemoryManager.loadAllProducts();
    }

    @PostMapping("/{product}/{myPrice}")
    public Product addNewProduct(@PathVariable String product,
                                 @PathVariable Float myPrice,
                                 @RequestBody HashMap<Integer,Float> priceQuantityMap)  {
        Product sortPricing = new Product(product, priceQuantityMap, myPrice);
        return sortPricingInMemoryManager.saveProduct(sortPricing);
    }

    @DeleteMapping("/{product}/{myPrice}")
    public void deleteProduct(@PathVariable String product,
                              @PathVariable Float myPrice)  {
        sortPricingInMemoryManager.deleteProduct(product,myPrice);
        //todo return value and handle exception
    }




}
