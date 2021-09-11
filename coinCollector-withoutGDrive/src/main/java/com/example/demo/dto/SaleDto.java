package com.example.demo.dto;

import com.example.demo.businessLogic.person.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    private ProductDto product;
    private Float quantity;
    private Person person;
    private Float discount;
    private Float earned;
    private LocalDateTime transactionDate;
    private Float income;

}
