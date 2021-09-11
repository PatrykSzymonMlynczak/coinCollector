package com.example.demo.controller;

import com.example.demo.businessLogic.sale.SaleRepo;
import com.example.demo.dto.SaleDto;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.mapper.SaleMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    SaleRepo saleRepo;
    SaleMapper saleMapper;

    @PostMapping({"/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
            "/{productName}/{mySortPrice}/{quantity}/{personName}",
            "/{productName}/{mySortPrice}/{quantity}/{money}/{personName}/{discount}",
            "/{productName}/{mySortPrice}/{quantity}/{money}/{personName}"
    })
    public SaleDto addSale(@PathVariable String productName,
                                   @PathVariable Float quantity,
                                   @PathVariable(required = false) Float money,
                                   @PathVariable String personName,
                                   @PathVariable(required = false) Float discount,
                                   @PathVariable Float mySortPrice)
                                   {/** Pricing for the same sort could be different,
                                    * so there is need to distinguish it by price.*/
        Sale sale = saleRepo
                .saveSale(productName, quantity, personName, discount, mySortPrice, money);
        return saleMapper.mapToDto(sale);
    }

    @GetMapping("/income")
    public Float getIncome(){
        return saleRepo.getTotalIncome();
    }

    @GetMapping("/all")
    public List<SaleDto> getAllSales(){
        return saleRepo.loadAllSales().stream()
                .map(saleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/earnings")
    public Float getTotalEarnings(){
        return saleRepo.getTotalEarnings();
    }

    @GetMapping("/cost")
    public Float getTotalCost(){
        return saleRepo.getTotalCost();
    }

    @GetMapping("/date/{date}")
    public Float getEarnedMoneyByDay(@PathVariable String date){
        return saleRepo.getEarnedMoneyByDay(date);
    }

    @GetMapping("/period/{dateStart}/{dateEnd}")
    public Float getEarnedMoneyByWeek(@PathVariable String dateStart,
                                      @PathVariable String dateEnd){
        return  saleRepo.getEarnedMoneyByWeek(dateStart,dateEnd);
    }

    @DeleteMapping("/all")
    public List<Sale> clearAllSales(){

        return saleRepo.clearAllSales();
    }
}
