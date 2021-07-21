package com.example.demo.service;

import com.example.demo.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepoPostgres extends JpaRepository<PersonEntity, Long> {

    PersonEntity findByName(String name);
}
