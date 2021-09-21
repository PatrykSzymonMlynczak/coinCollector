package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepoPostgres extends JpaRepository<SaleEntity, Long> {

    void deleteById(Long id);

    @Query(value = "select SUM(earned) from sale", nativeQuery = true)
    Float getTotalEarnings();

    @Query(value = "select SUM(earned) from sale where product_id != (select id from product where upper(name) = upper(:name))", nativeQuery = true)
    Float getEarningsWithoutSpecifiedProductName(@Param("name") String name);

    @Query(value = "select SUM(earned) from sale where product_id = (select id from product where upper(name) = upper(:name))", nativeQuery = true)
    Float getEarningsWithSpecifiedProductName(@Param("name") String name);

    @Query(value = "select SUM(income)-SUM(earned) from sale", nativeQuery = true)
    Float getTotalCost();

    @Query(value = "select SUM(income) from sale", nativeQuery = true)
    Float getTotalIncome();

    @Query(value = "select SUM(earned) from sale where day = :day", nativeQuery = true)
    Float getEarnedMoneyByDay(
            @Param("day") LocalDate day);

    @Query(value = "select * from sale where day = :day", nativeQuery = true)
    List<SaleEntity> getSalesByDay(
            @Param("day") LocalDate day);


    @Query(value = "select SUM(earned) from sale where day between :dayStart and :dayEnd", nativeQuery = true)
    Float getEarnedMoneyByPeriod(
            @Param("dayStart")LocalDate dateStart,
            @Param("dayEnd")LocalDate dateEnd);

    @Query(value = "select * from sale where day between :dayStart and :dayEnd", nativeQuery = true)
    List<SaleEntity> getSalesByPeriod(
            @Param("dayStart")LocalDate dateStart,
            @Param("dayEnd")LocalDate dateEnd);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sale WHERE ID=(SELECT MAX(id) FROM sale)", nativeQuery = true)
    void deleteLastSale();

    @Query(value = "SELECT * FROM sale ORDER BY ID DESC LIMIT 1", nativeQuery = true)
    SaleEntity getLastSale();


}
