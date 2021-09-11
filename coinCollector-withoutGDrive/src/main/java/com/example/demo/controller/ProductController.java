package com.example.demo.controller;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.TreeMap;

@RestController
@AllArgsConstructor
public class ProductController {

    ProductRepo sortPricingInMemoryManager;

    @GetMapping("/product")
    public HashMap<HashMap<Float,String>, Product> getWeed(){
        return sortPricingInMemoryManager.loadAllProducts();
    }

    @PostMapping("/{product}/{myPrice}")
    public Product addNewProduct(@PathVariable String product,
                                 @PathVariable Float myPrice,
                                 @RequestBody TreeMap<Float,Float> priceQuantityMap)  {
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
