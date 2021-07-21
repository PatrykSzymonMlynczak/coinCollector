package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private ProductEntity product;
    private Float quantity;

    @OneToOne
    private PersonEntity person;

    private Float discount;

    private LocalDateTime transactionDate;
    private Float mySortPrice;
    private Float earned;
    private Float income;

    public SaleEntity() {}

    @Builder
    public SaleEntity(ProductEntity product, Float quantity, PersonEntity person, Float discount, LocalDateTime transactionDate, Float mySortPrice, Float earned, Float income) {
        this.product = product;
        this.quantity = quantity;
        this.person = person;
        this.discount = discount;
        this.transactionDate = transactionDate;
        this.mySortPrice = mySortPrice;
        this.earned = earned;
        this.income = income;
    }
}
