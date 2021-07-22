package com.example.demo.postgres.repository;

import com.example.demo.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoPostgres extends JpaRepository<ProductEntity, Long> {

    @Query(value = " SELECT * FROM product " +
            "WHERE name = ?1 " +
            "AND my_price = ?2",
             nativeQuery = true)
    ProductEntity getByNameAndMyPrice(String productName, Float price);
    //could use Jpa Generated method "findBy.."
    //todo -> selecting by * is bad practice ?

    boolean existsByNameAndMyPrice(String productName, Float price);

    void deleteByNameAndMyPrice(String name, Float price);

}
//SELECT * FROM product WHERE name = 'Lemon Haze' AND my_price = 18