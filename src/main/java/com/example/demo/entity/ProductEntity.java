package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "quantityPriceMap",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "quantity")
    @Column(name = "price")
    private Map<Float,Float> quantityPriceMap;
    private Float myPrice;

    public ProductEntity() {
    }
}
