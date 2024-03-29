package com.example.demo.controller;

import com.example.demo.businessLogic.StaticProducts;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleMapper saleToDtoMapper;


    @Operation(summary = "Endpoint allowing to add manual Sale, precising given money enables  ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added ne Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"/manual/{price}/{productName}/{quantity}/{personName}/given/{givenMoney}/date/{date}"})
    public SaleDto addManualSale(@Parameter(description = "Product Name, case insensitive")
                                 @PathVariable String productName,
                                 @Parameter(description = "Quantity (grams)")
                                 @PathVariable Float quantity,
                                 @Parameter(description = "Manual price")
                                 @PathVariable Float price,
                                 @Parameter(description = "In case of making debt", required = false)
                                 @PathVariable(required = false) Float givenMoney,
                                 @Parameter(description = "Person Name")
                                 @PathVariable String personName,
                                 @Parameter(description = "Set date of transaction", required = false)
                                 @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveManualSale(productName, quantity, personName, givenMoney, price, date);
        return saleToDtoMapper.saleToDto(sale);
    }


    @Operation(summary = "Endpoint allowing to add new Sale with precised Given Money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added ne Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"/debt/{productName}/{quantity}/{givenMoney}/{personName}/{date}"})
    public SaleDto addSaleWithDebt(@Parameter(description = "Product Name, case insensitive")
                                   @PathVariable String productName,
                                   @Parameter(description = "Quantity (grams)")
                                   @PathVariable Float quantity,
                                   @Parameter(description = "In case of making debt")
                                   @PathVariable Float givenMoney,
                                   @Parameter(description = "Person Name")
                                   @PathVariable String personName,
                                   @Parameter(description = "Set date of transaction")
                                   @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveSale(productName, quantity, personName, null, givenMoney, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @Operation(summary = "Endpoint allowing to add new Sale with precised Given Money, and Discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added ne Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"/debtAndDiscount/{productName}/{quantity}/{givenMoney}/{personName}/{discount}/{date}"})
    public SaleDto addSaleWithDebtAndDiscount(@Parameter(description = "Product Name, case insensitive")
                                              @PathVariable String productName,
                                              @Parameter(description = "Quantity (grams)")
                                              @PathVariable Float quantity,
                                              @Parameter(description = "In case of making debt")
                                              @PathVariable Float givenMoney,
                                              @Parameter(description = "Person Name")
                                              @PathVariable String personName,
                                              @Parameter(description = "Discount can also can increase price, but must be negative)")
                                              @PathVariable Float discount,
                                              @Parameter(description = "Discount can also can increase price, but must be negative)")
                                              @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveSale(productName, quantity, personName, discount, givenMoney, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @Operation(summary = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added ne Sale with precised Discount"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"/discount/{productName}/{quantity}/{personName}/{discount}/{date}"})
    public SaleDto addSaleWithDiscount(@Parameter(description = "Product Name, case insensitive")
                                       @PathVariable String productName,
                                       @Parameter(description = "Quantity (grams)")
                                       @PathVariable Float quantity,
                                       @Parameter(description = "Person Name")
                                       @PathVariable String personName,
                                       @Parameter(description = "Discount can also can increase price, but must be negative)")
                                       @PathVariable Float discount,
                                       @Parameter(description = "Set date of transaction format : yyyy-mm-dd")
                                       @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveSale(productName, quantity, personName, discount, null, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @Operation(summary = "Endpoint allowing to add new Sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added new Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"/{productName}/{quantity}/{personName}/{date}"})
    public SaleDto addSale(@Parameter(description = "Product Name, case insensitive")
                           @PathVariable String productName,
                           @Parameter(description = "Quantity (grams)")
                           @PathVariable Float quantity,
                           @Parameter(description = "Person Name")
                           @PathVariable String personName,
                           @Parameter(description = "Set date of transaction format : yyyy-mm-dd")
                           @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveSale(productName, quantity, personName, null, null, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @Operation(summary = "Endpoint allowing to add new Sale ignoring surplus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added new Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping({"notIgnoreSurplus/{productName}/{quantity}/{personName}/{date}"})
    public SaleDto addSaleAndNotIgnoreSurplus(@Parameter(description = "Product Name, case insensitive") @PathVariable String productName,
                                              @Parameter(description = "Quantity (grams)") @PathVariable Float quantity,
                                              @Parameter(description = "Person Name") @PathVariable String personName,
                                              @Parameter(description = "Set date of transaction format : yyyy-mm-dd", required = false) @PathVariable(required = false) String date) {
        Sale sale = saleService
                .saveSaleNotIgnoringSurplus(productName, quantity, personName, date);
        return saleToDtoMapper.saleToDto(sale);
    }

    @Operation(summary = "Endpoint allowing check price for given quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added new Sale"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping({"priceCheckout/{productName}/{quantity}"})
    public Float priceCheckout(@Parameter(description = "Product Name, case insensitive")
                               @PathVariable String productName,
                               @Parameter(description = "Quantity (grams)")
                               @PathVariable Float quantity) {
        return saleService.priceCheckout(productName, quantity);
    }

    @Operation(summary = "Endpoint allowing to get all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales")})
    @GetMapping("/all")
    public List<SaleDto> getAllSales() {
        return saleService.loadAllSales().stream()
                .map(saleToDtoMapper::saleToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Endpoint allowing to get Sales without debt payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales")})
    @GetMapping("/withoutDebtPayments")
    public List<SaleDto> getSalesWithoutDebtPayments() {
        return saleService.loadAllSales().stream()
                .filter(sale -> !sale.getProduct().getName().equals(StaticProducts.PAY_DEBT.name()))
                .map(saleToDtoMapper::saleToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Endpoint allowing to get Sales by person name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales")})
    @GetMapping("/byPersonName/{name}")
    public List<SaleDto> getByName(@PathVariable String name) {
        return saleService.loadAllSales().stream()
                .filter(sale -> sale.getPerson().getName().equals(name))
                .map(saleToDtoMapper::saleToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Endpoint allowing to get income from all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received whole income")})
    @GetMapping("/income")
    public Float getIncome() {
        return saleService.getTotalIncome();
    }


    @Operation(summary = "Endpoint allowing to get earnings without specific product")
    @GetMapping("/earnings/without/{product}")
    public Float getEarningsWithoutSpecifiedProductName(@PathVariable String product) {
        return saleService.getEarningsWithoutSpecifiedProductName(product);
    }

    @Operation(summary = "Endpoint allowing to get earnings by specific product")
    @GetMapping("/earnings/{product}")
    public Float getEarningsWithSpecifiedProductName(@PathVariable String product) {
        return saleService.getEarningsWithSpecifiedProductName(product);
    }


    @Operation(summary = "Endpoint allowing to get earned money from all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all earned money")})
    @GetMapping("/earnings")
    public Float getTotalEarnings() {
        return saleService.getTotalEarnings();
    }

    @Operation(summary = 49651 + 919 + "Endpoint allowing to get cost form all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received cost of all Sales")})
    @GetMapping("/cost")
    public Float getTotalCost() {
        return saleService.getTotalCost();
    }

    @Operation(summary = "Endpoint allowing to get earnings by day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales by given day")})
    @GetMapping("/earnings/date/{date}")
    public Float getEarnedMoneyByDay(@Parameter(description = "format : yyyy-mm-dd")
                                     @PathVariable(required = false) String date) {
        return saleService.getEarnedMoneyByDay(setNowDateIfNotExist(date));
    }

    @Operation(summary = "Endpoint allowing to get earnings from given time period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales in given time period")})
    @GetMapping("/earnings/period/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(
            @Parameter(description = "format : yyyy-mm-dd")
            @PathVariable String dateStart,
            @Parameter(description = "format : yyyy-mm-dd")
            @PathVariable(required = false) String dateEnd) {
        return saleService.getEarnedMoneyByPeriod(dateStart, setNowDateIfNotExist(dateEnd));
    }

    @Operation(summary = "Endpoint allowing to delete last sale")
    @DeleteMapping("/last")
    public SaleDto deleteLastSale() {
        saleService.deleteLastSale();
        return saleToDtoMapper.saleToDto(saleService.getLastSale());
    }

    @GetMapping("/last")
    public SaleDto getLastSale() {
        return saleToDtoMapper.saleToDto(saleService.getLastSale());
    }

    @Operation(summary = "Endpoint allowing to get all Sales from given time period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales in given time period")})
    @GetMapping("/period/{dateStart}/{dateEnd}")
    public List<SaleDto> getSalesByPeriod(@PathVariable String dateStart,
                                          @Parameter(description = "Set date of transaction ") @PathVariable(required = false) String dateEnd) {


        return saleService.getSalesByPeriod(dateStart, setNowDateIfNotExist(dateEnd)).stream().map(saleToDtoMapper::saleToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Endpoint allowing to get all Sales in precised day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all Sales in given time period")})
    @GetMapping("/date/{date}")
    public List<SaleDto> getSalesByPeriod(@Parameter(description = "Set date of transaction format : yyyy-mm-dd") @PathVariable String date) {
        return saleService.getSalesByDay(setNowDateIfNotExist(date)).stream().map(saleToDtoMapper::saleToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all Sales")})
    @DeleteMapping("/all")
    public List<SaleDto> clearAllSales() {
        saleService.clearAllSales();
        return new ArrayList<>();
    }

    @Operation(summary = "Endpoint allowing to delete all Sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all Sales")})
    @DeleteMapping("/byId/{id}")
    public List<SaleDto> deleteSaleById(@PathVariable Long id) {
        saleService.deleteById(id);
        return new ArrayList<>();
    }

    private String setNowDateIfNotExist(String date) {
        if (date == null || date.equals("undefined")) {
            return LocalDate.now().toString();

        } else {
            return date;
        }
    }
}
