package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.TreeMap;

@Entity
@Data
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private TreeMap<Float,Float> quantityPriceMap;
    private Float myPrice;

    public ProductEntity(String name, TreeMap<Float, Float> quantityPriceMap, Float myPrice) {
        this.name = name;
        this.quantityPriceMap = quantityPriceMap;
        this.myPrice = myPrice;
    }

    public ProductEntity() {
    }
}
