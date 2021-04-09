package com.example.demo;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Person implements Serializable {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    private Float debt;
}
