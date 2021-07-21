package com.example.demo.service;

import com.example.demo.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepoPostgres extends JpaRepository<SaleEntity, Long> {
}
