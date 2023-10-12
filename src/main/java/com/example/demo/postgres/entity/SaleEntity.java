package com.example.demo.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import jakarta.persistence.Id;
import jakarta.persistence.Table;import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    private float sortAmountBefore;

    private Float quantity;

    @OneToOne
    @JoinColumn(name = "person_id")
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
