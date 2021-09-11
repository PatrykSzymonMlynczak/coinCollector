package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepoPostgres extends JpaRepository<ProductEntity, Long> {

    //todo -> native query is not checked by compiler -> integration tests
    //todo replace ?1 with @Param
    //todo -> selecting by * is bad practice ?
    @Query(value = " SELECT * FROM product " +
            "WHERE UPPER(name) = UPPER(?1) ",
            nativeQuery = true)
    ProductEntity getByNameIgnoreCase(String productName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE PRODUCT SET total_sort_amount = (total_sort_amount - :boughtQuantity) WHERE upper(name) = upper(:productName)", nativeQuery = true)
    void reduceTotalSortAmount(@Param("productName") String productName, @Param("boughtQuantity") Float boughtQuantity);

    @Query(value = " SELECT total_sort_amount FROM product " +
            "WHERE UPPER(name) = UPPER(?1) ",
            nativeQuery = true)
    Float getTotalSortAmount(String productName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE PRODUCT SET total_sort_amount = 0 WHERE upper(name) = upper(:productName)", nativeQuery = true)
    void eraseRestOfProduct(@Param("productName") String productName);


        @Query(value = "SELECT CASE WHEN EXISTS " +
            "(SELECT *" +
            "FROM product " +
            "WHERE upper(name)=upper(:productName)) " +
            "THEN CAST(1 AS BIT) " +
            "ELSE CAST(0 AS BIT) " +
            "END", nativeQuery = true)
    boolean existsByNameIgnoreCase(@Param("productName") String productName);

    @Transactional
    void deleteByName(String name);

}
