package com.example.demo.businessLogic.person;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Person implements Serializable {

    private final String name;
    /**debt is stored as negative number*/
    private Float debt;

    public Person(String name) {
        this.name = name;
        this.debt = 0F ;
    }

    public void increaseDebt(Float debt){
        this.debt -= debt;
    }

    //todo controller for this
    public Person reduceDebt(Float returnedMoney){
        this.debt += returnedMoney;
        return this;
    }
}