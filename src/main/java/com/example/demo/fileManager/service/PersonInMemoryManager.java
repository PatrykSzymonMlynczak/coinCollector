package com.example.demo.fileManager.service;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.repositoryContract.PersonRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Qualifier("jsonFile")
@AllArgsConstructor
public class PersonInMemoryManager implements PersonRepo {


    private final List<Person> personArrayList = Arrays.asList(
            new Person("Ada"),
            new Person("Qn"),
            new Person("Marek"),
            new Person("Han"),
            new Person("Oltek"),
            new Person("Zamor"),
            new Person("mlodyZamor"),
            new Person("Roksa"),
            new Person("Bleid"),
            new Person("Andrzej"),
            new Person("Kris"),
            new Person("Bany"),
            new Person("Tomasz"),
            new Person("Bartek"),
            new Person("Kamil")
    );

    @Override
    public Person savePerson(Person person) {
        return person;
    }

    @Override
    public Person getPerson(String name) {
        return personArrayList.stream().filter(p -> p.getName().equals(name)).findAny().get();
    }

    @Override
    public List<Person> getAllPerson() {
        return personArrayList;
    }
}
