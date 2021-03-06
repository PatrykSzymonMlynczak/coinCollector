package com.example.demo.controller;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.dto.ProductDto;
import com.example.demo.repositoryContract.ProductRepo;
import com.example.demo.mapper.ProductMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    @Qualifier("${data.service}")
    private ProductRepo sortPricingInMemoryManager;

    @Autowired
    private ProductMapper productMapper;

    //todo method for retrieving single product
    //todo api response for exceptions

    @ApiOperation(value = "Endpoint allowing get all Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Products"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping
    public List<ProductDto> getAllProducts(){
        return sortPricingInMemoryManager.loadAllProducts().values()
                .stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }
    @ApiOperation(value = "Endpoint allowing to add new Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Product"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping("/{productName}/{myPrice}")
    public ProductDto addNewProduct(
                                    @ApiParam(value = "Product Name", example = "Lemon Haze")
                                            @PathVariable String productName,
                                    @ApiParam(value = "Price per gram", example = "10")
                                            @PathVariable Float myPrice,
                                    @ApiParam(value = "quantity as a key, and price per gram as a value: " +
                                            "\n example = {\"1\":20, \"5\":16}",
                                            example = "{\"1\":20, \"5\":16}")
                                            @RequestBody TreeMap<Float,Float> priceQuantityMap)  {
        Product product = new Product(productName, priceQuantityMap, myPrice);
        return productMapper.productToDto(sortPricingInMemoryManager.saveProduct(product));
    }

    @ApiOperation(value = "Endpoint allowing to delete particular Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted product"),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping("/{productName}/{myPrice}")
    public void deleteProduct(@ApiParam(value = "Product Name", example = "Lemon Haze")
                                    @PathVariable String productName,
                              @ApiParam(value = "Price per gram",example = "10")
                                    @PathVariable Float myPrice)  {
        sortPricingInMemoryManager.deleteProduct(productName,myPrice);
        //todo return value and handle exception
    }




}
