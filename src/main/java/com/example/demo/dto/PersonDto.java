package com.example.demo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {

    private String name;
    private Float debt;

    public PersonDto(String name) {
        this.name = name;
        this.debt = 0F ;
    }

}
