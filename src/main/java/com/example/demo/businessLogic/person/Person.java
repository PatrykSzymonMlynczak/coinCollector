package com.example.demo.businessLogic.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * debt is stored as positive number
 * additional money will decrease debt or make surplus
 * */
@Data
@NoArgsConstructor
public class Person implements Serializable {

    private Long id;
    private String name;
    private Float debt;

    public Person(String name) {
        this.name = name;
        this.debt = 0F ;
    }

    public void increaseDebt(Float debt){
        this.debt += debt;
    }

    public Person reduceDebt(Float returnedMoney){
        this.debt -= returnedMoney;
        return this;
    }
}