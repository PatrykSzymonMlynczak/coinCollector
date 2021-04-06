package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter

public class Sale implements Serializable {

    private final String productName;
    private final Integer quantity;
    private final Person person;
    private final LocalDateTime transactionDate;
    private final Float discount;

    @JsonIgnore
    private final Float mySortPrice;

    public Sale(String productName, Integer quantity, Person person, Float discount, Float mySortPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.person = person;
        this.transactionDate = LocalDateTime.now();
        this.discount = discount;
        this.mySortPrice = mySortPrice;
    }
}
