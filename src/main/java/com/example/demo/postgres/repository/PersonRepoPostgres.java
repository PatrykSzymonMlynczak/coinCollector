package com.example.demo.postgres.repository;

import com.example.demo.postgres.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepoPostgres extends JpaRepository<PersonEntity, Long> {

    PersonEntity findByName(String name);
    List<PersonEntity> findAll();
    boolean existsByName(String name);
}
