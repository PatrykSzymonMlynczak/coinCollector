package com.example.demo.postgres.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)//todo
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quantityPriceMap",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "quantity")
    @Column(name = "price")
    private Map<Float, Float> quantityPriceMap;
    private Float myPrice;
    private Float totalSortAmount;
    private LocalDate additionDate;
    private LocalDate eraseDate;

    public ProductEntity() {
    }
}
