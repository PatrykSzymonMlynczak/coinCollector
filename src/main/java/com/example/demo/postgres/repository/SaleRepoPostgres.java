package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SaleRepoPostgres extends JpaRepository<SaleEntity, Long> {

    @Query(value = "select SUM(earned) from sale", nativeQuery = true)
    Float getTotalEarnings();

    @Query(value = "select SUM(income)-SUM(earned) from sale", nativeQuery = true)
    Float getTotalCost();

    @Query(value = "select SUM(income) from sale", nativeQuery = true)
    Float getTotalIncome();

    @Query(value = "select SUM(earned) from sale where day = :day", nativeQuery = true)
    Float getEarnedMoneyByDay(
            @Param("day") LocalDate day);

    @Query(value = "select SUM(earned) from sale where day between :dayStart and :dayEnd", nativeQuery = true)
    Float getEarnedMoneyByWeek(
            @Param("dayStart")LocalDate dateStart,
            @Param("dayEnd")LocalDate dateEnd);

}
