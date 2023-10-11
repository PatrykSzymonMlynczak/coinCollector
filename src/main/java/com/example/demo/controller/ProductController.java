package com.example.demo.controller;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.postgres.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productRepo;

    @Autowired
    private ProductMapper productMapper;

    //todo api response for exceptions

    @Operation(summary  = "Endpoint allowing get all Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Successfully received all Products"),
            @ApiResponse(responseCode  = "400", description = "Bad request")})
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productRepo.loadAllProducts()
                .stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary  = "Endpoint allowing to add new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Successfully added new Product"),
            @ApiResponse(responseCode  = "400", description = "Bad request")})
    @PostMapping("/{productName}/{myPrice}/{totalSortAmount}")
    public ProductDto addNewProduct(
            @Parameter(description = "Product Name", example = "Lemon Haze")
            @PathVariable String productName,
            @Parameter(description = "Price per gram", example = "10")
            @PathVariable Float myPrice,
            @Parameter(description = "Total sort amount", example = "10")
            @PathVariable Float totalSortAmount,
            @Parameter(description = "quantity as a key, and price per gram as a value: " +
                    "\n example = {\"1\":20, \"5\":16}",
                    example = "{\"1\":20, \"5\":16}")
            @RequestBody TreeMap<Float, Float> priceQuantityMap) {
        Product product = new Product(productName, priceQuantityMap, myPrice, totalSortAmount);
        return productMapper.productToDto(productRepo.saveProduct(product));
    }

    @Operation(summary  = "Endpoint erasing remaining sort amount")
    @DeleteMapping("/erase/{productName}")
    public void eraseRestOfProduct(@Parameter(description = "Product Name", example = "Lemon Haze")
                                   @PathVariable String productName){
        productRepo.eraseRestOfProduct(productName);
    }

    @Operation(summary  = "Endpoint retrieving amount of rest sort")
    @GetMapping("/totalAmount")
    public Float getTotalAmount(String productName){
        return productRepo.getTotalAmount(productName);
    }


    @Operation(summary  = "Endpoint allowing to delete particular Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Successfully deleted product"),
            @ApiResponse(responseCode  = "400", description = "Bad request")})
    @DeleteMapping("/{productName}")
    public void deleteProduct(@Parameter(description = "Product Name", example = "Lemon Haze")
                              @PathVariable String productName) {
        productRepo.deleteProduct(productName);
        //todo return value and handle exception
    }

}
