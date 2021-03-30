package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Sale {

    private final Weed weed;
    private final Integer quantity;
    private final Person person;
    private final LocalDate transactionDate;
    private final Float discount;
    @JsonIgnore
    private final Float mySortPrice;

    public Sale(Weed weed, Integer quantity, Person person, Float discount, Float mySortPrice) {
        this.weed = weed;
        this.quantity = quantity;
        this.person = person;
        this.transactionDate = LocalDate.now();
        this.discount = discount;
        this.mySortPrice = mySortPrice;
    }
}
