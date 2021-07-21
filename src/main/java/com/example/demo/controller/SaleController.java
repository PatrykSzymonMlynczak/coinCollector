package com.example.demo.controller;

import com.example.demo.businessLogic.sale.SaleRepo;
import com.example.demo.dto.SaleDto;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.mapper.pojoToDto.SaleToDtoMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    SaleRepo saleRepo;
    SaleToDtoMapper saleToDtoMapper;

    @ApiOperation(value = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
            "/{productName}/{mySortPrice}/{quantity}/{personName}",
            "/{productName}/{mySortPrice}/{quantity}/{givenMoney}/{personName}/{discount}",
            "/{productName}/{mySortPrice}/{quantity}/{givenMoney}/{personName}"
    })
    public SaleDto addSale(@PathVariable String productName,
                                        @PathVariable Float quantity,
                                   @ApiParam(value = "In case of making debtn")
                                        @PathVariable(required = false) Float givenMoney,
                                        @PathVariable String personName,
                                   @ApiParam(value = "Discount can also can increase price, but must be negative)")
                                        @PathVariable(required = false) Float discount,
                                   @ApiParam(value = "Price per gram")
                                        @PathVariable Float mySortPrice)
                                   {/** Pricing for the same sort could be different,
                                    * so there is need to distinguish it by price.*/
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, mySortPrice, givenMoney);
        return saleToDtoMapper.mapToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales")})
    @GetMapping("/all")
    public List<SaleDto> getAllSales(){
        return saleRepo.loadAllSales().stream()
                .map(saleToDtoMapper::mapToDto)
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
    public Float getEarnedMoneyByDay(@ApiParam(value = "format : yyyy-mm-dd")
                                         @PathVariable String date){
        return saleRepo.getEarnedMoneyByDay(date);
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales from given time period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales in given time period")})
    @GetMapping("/period/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(@ApiParam(value = "format : yyyy-mm-dd")
                                          @PathVariable String dateStart,
                                      @ApiParam(value = "format : yyyy-mm-dd")
                                          @PathVariable String dateEnd){
        return  saleRepo.getEarnedMoneyByWeek(dateStart,dateEnd);
    }

    @ApiOperation(value = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted all Sales")})
    @DeleteMapping("/all")
    public List<SaleDto> clearAllSales(){
        return saleRepo.clearAllSales().stream()
                .map(saleToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // todo get earnings in time period
}
