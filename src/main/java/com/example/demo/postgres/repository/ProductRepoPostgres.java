package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoPostgres extends JpaRepository<ProductEntity, Long> {

    //todo -> native query is not checked by compiler -> integration tests
    //todo replace ?1 with @Param
    //todo -> selecting by * is bad practice ?
    @Query(value = " SELECT * FROM product " +
                    "WHERE UPPER(name) = UPPER(?1) " +
                    "AND my_price = ?2",
                    nativeQuery = true)
    ProductEntity getByNameAndPriceIgnoreCase(String productName, Float price);

    @Query(value ="SELECT CASE WHEN EXISTS " +
                    "(SELECT *"  +
                    "FROM product " +
                    "WHERE upper(name)=upper(?1) and my_price=?2) " +
                    "THEN CAST(1 AS BIT) " +
                    "ELSE CAST(0 AS BIT) " +
                    "END", nativeQuery = true)
    boolean existsByNameAndPriceIgnoreCase(String productName, Float price);

    void deleteByNameAndMyPrice(String name, Float price);

}
