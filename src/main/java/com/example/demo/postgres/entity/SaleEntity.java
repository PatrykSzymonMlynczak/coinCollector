package com.example.demo.postgres.entity;

import com.example.demo.businessLogic.product.Product;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ProductEntity product;
    private float sortAmountBefore;

    private Float quantity;

    @OneToOne
    private PersonEntity person;

    private Float discount;

    @Column(name = "day")
    private LocalDate transactionDate;
    @Column(name = "moment")
    private LocalTime transactionTime;
    private Float mySortPrice;
    private Float earned;
    private Float income;
    private Float loss;

    public SaleEntity() {}

}
