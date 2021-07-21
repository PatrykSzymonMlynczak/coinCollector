package com.example.demo.service;

import com.example.demo.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoPostgres extends JpaRepository<ProductEntity, Long> {

    @Query(value = " SELECT * FROM product_entity " +
            "WHERE name = ?1 " +
            "AND my_price = ?2",
             nativeQuery = true)
    ProductEntity getByProductNameAndPrice(String productName, Float price);
}
