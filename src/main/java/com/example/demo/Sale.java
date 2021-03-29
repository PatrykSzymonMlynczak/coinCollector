package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Sale {

    private Weed weed;
    private Integer quantity;
    private Person person;
}
