package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String name;
    private Float myPrice;
    private Float totalSortAmount;
/*
    private LocalDate additionDate;
    private LocalDate endDate;
*/

}
