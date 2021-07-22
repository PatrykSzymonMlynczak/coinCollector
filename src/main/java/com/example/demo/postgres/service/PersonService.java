package com.example.demo.postgres.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.entity.PersonEntity;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.repositoryContract.PersonRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("postgres")
@AllArgsConstructor
public class PersonService implements PersonRepo {

    private final PersonRepoPostgres personRepoPostgres;
    private final PersonMapper personMapper;


    @Override
    public Person savePerson(Person person) {
        personRepoPostgres.save(personMapper.personToEntity(person));
        return person;
    }

    @Override
    public Person getPerson(String name) {
        PersonEntity personEntity = personRepoPostgres.findByName(name);
        return personMapper.entityToPerson(personEntity);
    }

    @Override
    public List<Person> getAllPerson() {
        return personRepoPostgres.findAll().stream().map(personMapper::entityToPerson).collect(Collectors.toList());
    }
}
