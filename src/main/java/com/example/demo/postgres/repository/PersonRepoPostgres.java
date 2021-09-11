package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonRepoPostgres extends JpaRepository<PersonEntity, Long> {

    @Query(value = " SELECT * FROM person " +
            "WHERE UPPER(name) = UPPER(:name)",
            nativeQuery = true)
    PersonEntity findByNameIgnoreCase(
            @Param("name")String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE person SET debt = :debt WHERE upper(name) = upper(:name)", nativeQuery = true)
    void updateDebt(
            @Param("debt") Float debt,
            @Param("name") String name);


    @Transactional
    @Modifying
    @Query(value = "UPDATE person SET debt = (debt - :payedMoney) WHERE upper(name) = upper(:name)", nativeQuery = true)
    void reduceDebt(
            @Param("payedMoney") Float payedMoney,
            @Param("name") String name);


    List<PersonEntity> findAll();

    @Query(value ="SELECT CASE WHEN EXISTS " +
            "(SELECT * "  +
            "FROM person " +
            "WHERE upper(name)=upper(:name) ) " +
            "THEN CAST(1 AS BIT) " +
            "ELSE CAST(0 AS BIT) " +
            "END", nativeQuery = true)
    boolean existsByNameIgnoreCase(
            @Param("name") String name);


}
