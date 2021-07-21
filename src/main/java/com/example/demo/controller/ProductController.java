package com.example.demo.controller;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.ProductRepo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.TreeMap;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductRepo sortPricingInMemoryManager;

    public ProductController(ProductRepo sortPricingInMemoryManager) {
        this.sortPricingInMemoryManager = sortPricingInMemoryManager;
    }

    @ApiOperation(value = "Endpoint allowing get all Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Products"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping
    public HashMap<HashMap<Float,String>, Product> getAllProducts(){
        return sortPricingInMemoryManager.loadAllProducts();
    }
    @ApiOperation(value = "Endpoint allowing to add new Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Product"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping("/{productName}/{myPrice}")
    public Product addNewProduct(@PathVariable String productName,
                                 @ApiParam(value = "Price per gram")
                                     @PathVariable Float myPrice,
                                 @ApiParam(value = "quantity as a key, and price per gram as a value")
                                     @RequestBody TreeMap<Float,Float> priceQuantityMap)  {
        Product sortPricing = new Product(productName, priceQuantityMap, myPrice);
        return sortPricingInMemoryManager.saveProduct(sortPricing);
    }

    @ApiOperation(value = "Endpoint allowing to delete particular Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted product"),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping("/{productName}/{myPrice}")
    public void deleteProduct(@PathVariable String productName,
                              @ApiParam(value = "Price per gram")
                                 @PathVariable Float myPrice)  {
        sortPricingInMemoryManager.deleteProduct(productName,myPrice);
        //todo return value and handle exception
    }




}
