package com.example.demo.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.person.PersonRepo;
import com.example.demo.mapper.pojoToEntity.PersonToEntityMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PersonService implements PersonRepo {

    PersonRepoPostgres personRepoPostgres;
    PersonToEntityMapper personToEntityMapper;

    public PersonService(PersonRepoPostgres personRepoPostgres, PersonToEntityMapper personToEntityMapper) {
        this.personRepoPostgres = personRepoPostgres;
        this.personToEntityMapper = personToEntityMapper;
    }

    @Override
    public Person savePerson(Person person) {
        personRepoPostgres.save(personToEntityMapper.mapToEntity(person));
        return person;
    }

    @Override
    public Person getPerson(String name) {
        return null;
    }

    @Override
    public List<Person> getAllPerson() {
        return null;
    }
}
