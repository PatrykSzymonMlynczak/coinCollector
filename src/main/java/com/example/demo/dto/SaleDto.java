package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    private Long id;
    private ProductDto product;
    private float sortAmountBefore;
    private Float quantity;
    private PersonDto person;
    private Float discount;
    private Float earned;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private Float income;


}
