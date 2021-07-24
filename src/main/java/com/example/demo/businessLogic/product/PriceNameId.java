package com.example.demo.businessLogic.product;

import lombok.Data;

@Data
public class PriceNameId {

    //todo can/should be primitive ?
    private float price;
    private String name;

    public PriceNameId(float price, String name) {
        this.price = price;
        this.name = name.toLowerCase();
    }
}
