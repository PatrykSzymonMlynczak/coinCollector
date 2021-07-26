package com.example.demo.postgres.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.person.PersonAlreadyExistsException;
import com.example.demo.businessLogic.person.PersonNotExistsException;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.postgres.entity.PersonEntity;
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
        PersonEntity personEntity = personMapper.personToEntity(person);
        if(!personRepoPostgres.existsByNameIgnoreCase(person.getName())) {
            personRepoPostgres.save(personEntity);
            return person;
        }else throw new PersonAlreadyExistsException(person.getName());
    }

    @Override
    public Person getPerson(String name) {
        if(personRepoPostgres.existsByNameIgnoreCase(name)) {
            PersonEntity personEntity = personRepoPostgres.findByNameIgnoreCase(name);
            return personMapper.entityToPerson(personEntity);
        }else throw new PersonNotExistsException(name);
    }

    @Override
    public List<Person> getAllPerson() {
        return personRepoPostgres.findAll().stream().map(personMapper::entityToPerson).collect(Collectors.toList());
    }

    void updateDebt(Float debt, String name){
        personRepoPostgres.updateDebt(debt,name);
    }
}
