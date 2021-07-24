package com.example.demo.controller;

import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.repositoryContract.SaleRepo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    @Qualifier("${data.service}")
    private SaleRepo saleRepo;

    @Autowired
    private SaleMapper saleToDtoMapper;

    //todo separate endpoints

    @ApiOperation(value = "Endpoint allowing to add new Sale with precised Given Money")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{givenMoney}/{personName}"
    })
    public SaleDto addSaleWithDebt(@ApiParam(value = "Product Name, case insensitive", example = "Lemon Haze")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)", example = "5")
                                @PathVariable Float quantity,
                           @ApiParam(value = "In case of making debt", example = "50")
                                @PathVariable Float givenMoney,
                           @ApiParam(value = "Person Name", example = "Zamor")
                                @PathVariable String personName,
                           @ApiParam(value = "Price per gram", example = "10")
                                @PathVariable Float mySortPrice) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, null, mySortPrice, givenMoney);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale with precised Given Money, and Discount")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{givenMoney}/{personName}/{discount}"
    })
    public SaleDto addSaleWithDebtAndDiscount(@ApiParam(value = "Product Name, case insensitive", example = "Lemon Haze")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)", example = "5")
                                @PathVariable Float quantity,
                           @ApiParam(value = "In case of making debt", example = "50")
                                @PathVariable Float givenMoney,
                           @ApiParam(value = "Person Name", example = "Zamor")
                                @PathVariable String personName,
                           @ApiParam(value = "Discount can also can increase price, but must be negative)", example = "-10")
                                @PathVariable Float discount,
                           @ApiParam(value = "Price per gram", example = "10")
                                @PathVariable Float mySortPrice) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, mySortPrice, givenMoney);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale with precised Discount"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}"})
    public SaleDto addSaleWithDiscount(@ApiParam(value = "Product Name, case insensitive", example = "Lemon Haze")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)", example = "5")
                                @PathVariable Float quantity,
                           @ApiParam(value = "Person Name", example = "Zamor")
                                @PathVariable String personName,
                           @ApiParam(value = "Discount can also can increase price, but must be negative)", example = "-10")
                                @PathVariable Float discount,
                           @ApiParam(value = "Price per gram", example = "10")
                                @PathVariable Float mySortPrice) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, mySortPrice, null);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{personName}"})
    public SaleDto addSale(@ApiParam(value = "Product Name, case insensitive", example = "Lemon Haze")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)", example = "5")
                                @PathVariable Float quantity,
                           @ApiParam(value = "Price per gram", example = "10")
                                @PathVariable Float mySortPrice,
                           @ApiParam(value = "Person Name", example = "Zamor")
                                @PathVariable String personName) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, null, mySortPrice, null);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales")})
    @GetMapping("/all")
    public List<SaleDto> getAllSales(){
        return saleRepo.loadAllSales().stream()
                .map(saleToDtoMapper::saleToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Endpoint allowing to get income from all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received whole income")})
    @GetMapping("/income")
    public Float getIncome(){
        return saleRepo.getTotalIncome();
    }

    @ApiOperation(value = "Endpoint allowing to get earned money from all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all earned money")})
    @GetMapping("/earnings")
    public Float getTotalEarnings(){
        return saleRepo.getTotalEarnings();
    }

    @ApiOperation(value = "Endpoint allowing to get cost form all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received cost of all Sales")})
    @GetMapping("/cost")
    public Float getTotalCost(){
        return saleRepo.getTotalCost();
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales by day")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales by given day")})
    @GetMapping("/date/{date}")
    public Float getEarnedMoneyByDay(@ApiParam(value = "format : yyyy-mm-dd" , example = "2021-07-25")
                                         @PathVariable String date){
        return saleRepo.getEarnedMoneyByDay(date);
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales from given time period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales in given time period")})
    @GetMapping("/period/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(
                                      @ApiParam(value = "format : yyyy-mm-dd", example = "2022-07-07")
                                            @PathVariable String dateEnd,
                                      @ApiParam(value = "format : yyyy-mm-dd", example = "2021-07-07")
                                            @PathVariable String dateStart){
        return  saleRepo.getEarnedMoneyByWeek(dateStart,dateEnd);
    }

    @ApiOperation(value = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted all Sales")})
    @DeleteMapping("/all")
    public List<SaleDto> clearAllSales(){
        return saleRepo.clearAllSales().stream()
                .map(saleToDtoMapper::saleToDto)
                .collect(Collectors.toList());
    }

}
