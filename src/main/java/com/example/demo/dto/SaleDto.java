package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    private ProductDto product;
    private Float quantity;
    private PersonDto person;
    private Float discount;
    private Float earned;
    private LocalDateTime transactionDate;
    private Float income;

}
