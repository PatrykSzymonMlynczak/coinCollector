package com.example.demo.controller;

import com.example.demo.businessLogic.StaticProducts;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.repositoryContract.SaleRepo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private SaleMapper saleToDtoMapper;

    //todo -> consider to make payload model and use @RequestBody

    @ApiOperation(value = "Endpoint allowing to add new Sale with precised Given Money")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/debt/{productName}/{quantity}/{givenMoney}/{personName}/{date}"})
    public SaleDto addSaleWithDebt(@ApiParam(value = "Product Name, case insensitive")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                                @PathVariable Float quantity,
                           @ApiParam(value = "In case of making debt")
                                @PathVariable Float givenMoney,
                           @ApiParam(value = "Person Name")
                                @PathVariable String personName,
                           @ApiParam(value = "Set date of transaction")
                                @PathVariable(required = false) String date) {
    Sale sale = saleRepo
                .saveSale(productName, quantity, personName, null, givenMoney, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale with precised Given Money, and Discount")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/debtAndDiscount/{productName}/{quantity}/{givenMoney}/{personName}/{discount}/{date}"})
    public SaleDto addSaleWithDebtAndDiscount(@ApiParam(value = "Product Name, case insensitive")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                                @PathVariable Float quantity,
                           @ApiParam(value = "In case of making debt")
                                @PathVariable Float givenMoney,
                           @ApiParam(value = "Person Name")
                                @PathVariable String personName,
                           @ApiParam(value = "Discount can also can increase price, but must be negative)")
                                @PathVariable Float discount,
                           @ApiParam(value = "Discount can also can increase price, but must be negative)")
                                @PathVariable(required = false) String date) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, givenMoney, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ne Sale with precised Discount"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/discount/{productName}/{quantity}/{personName}/{discount}/{date}"})
    public SaleDto addSaleWithDiscount(@ApiParam(value = "Product Name, case insensitive")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                                @PathVariable Float quantity,
                           @ApiParam(value = "Person Name")
                                @PathVariable String personName,
                           @ApiParam(value = "Discount can also can increase price, but must be negative)")
                                @PathVariable Float discount,
                           @ApiParam(value = "Set date of transaction format : yyyy-mm-dd")
                                @PathVariable(required = false) String date) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, null, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/{productName}/{quantity}/{personName}/{date}"})
    public SaleDto addSale(@ApiParam(value = "Product Name, case insensitive")
                                @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                                @PathVariable Float quantity,
                           @ApiParam(value = "Person Name")
                                @PathVariable String personName,
                           @ApiParam(value = "Set date of transaction format : yyyy-mm-dd")
                               @PathVariable(required = false) String date) {
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, null, null, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing to add new Sale ignoring surplus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"ignoreSurplus/{productName}/{quantity}/{personName}/{date}"})
    public SaleDto addSaleIgnoreSurplus(@ApiParam(value = "Product Name, case insensitive")
                           @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                           @PathVariable Float quantity,
                           @ApiParam(value = "Person Name")
                           @PathVariable String personName,
                           @ApiParam(value = "Set date of transaction format : yyyy-mm-dd")
                           @PathVariable(required = false) String date) {
        Sale sale = saleRepo
                .saveSaleIgnoringSurplus(productName, quantity, personName, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @ApiOperation(value = "Endpoint allowing check price for given quantity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Sale"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"priceCheckout/{productName}/{quantity}"})
    public Float priceCheckout(@ApiParam(value = "Product Name, case insensitive")
                           @PathVariable String productName,
                           @ApiParam(value = "Quantity (grams)")
                           @PathVariable Float quantity) {
        return saleRepo.priceCheckout(productName, quantity);
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

    @ApiOperation(value = "Endpoint allowing to get Sales without debt payments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales")})
    @GetMapping("/withoutDebtPayments")
    public List<SaleDto> getSalesWithoutDebtPayments(){
        return saleRepo.loadAllSales().stream()
                .filter(sale -> !sale.getProduct().getName().equals(StaticProducts.PAY_DEBT.name()))
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

    @GetMapping("/cleanEarnings")
    public Float getEarningsWithoutSpecifiedProductName(String name) {
        return saleRepo.getEarningsWithoutSpecifiedProductName(name);
    }

    @GetMapping("/earningsByProduct")
    public Float getEarningsWithSpecifiedProductName(String name) {
        return saleRepo.getEarningsWithSpecifiedProductName(name);
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

    @ApiOperation(value = "Endpoint allowing to get earnings by day")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales by given day")})
    @GetMapping("/earnings/date/{date}")
    public Float getEarnedMoneyByDay(@ApiParam(value = "format : yyyy-mm-dd" )
                                         @PathVariable(required = false) String date){
        return saleRepo.getEarnedMoneyByDay(date);
    }

    @ApiOperation(value = "Endpoint allowing to get earnings from given time period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales in given time period")})
    @GetMapping("/earnings/period/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(
                                      @ApiParam(value = "format : yyyy-mm-dd")
                                            @PathVariable(required = false) String dateEnd,
                                      @ApiParam(value = "format : yyyy-mm-dd")
                                            @PathVariable String dateStart){
        return  saleRepo.getEarnedMoneyByPeriod(dateStart,dateEnd);
    }

    @ApiOperation(value = "Endpoint allowing to delete last sale")
    @DeleteMapping("/last")
    public SaleDto deleteLastSale(){
        saleRepo.deleteLastSale();
        return saleToDtoMapper.saleToDto(saleRepo.getLastSale());
    }

    @GetMapping("/last")
    public SaleDto getLastSale(){
        return saleToDtoMapper.saleToDto(saleRepo.getLastSale());
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales from given time period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales in given time period")})
    @GetMapping("/period/{dateStart}/{dateEnd}")
    public List<SaleDto> getSalesByPeriod(@PathVariable String dateStart, @PathVariable String dateEnd){
        return saleRepo.getSalesByPeriod(dateStart, dateEnd).stream().map(saleToDtoMapper::saleToDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Endpoint allowing to get all Sales in precised day")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Sales in given time period")})
    @GetMapping("/date/{date}")
    public List<SaleDto> getSalesByPeriod(@PathVariable String date){
        return saleRepo.getSalesByDay(date).stream().map(saleToDtoMapper::saleToDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted all Sales")})
    @DeleteMapping("/all")
    public List<SaleDto> clearAllSales(){
        saleRepo.clearAllSales();
        return new ArrayList<>();
    }
    @ApiOperation(value = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted all Sales")})
    @DeleteMapping("/byId/{id}")
    public List<SaleDto> deleteSaleById(@PathVariable Long id){
        saleRepo.deleteById(id);
        return new ArrayList<>();
    }
}
