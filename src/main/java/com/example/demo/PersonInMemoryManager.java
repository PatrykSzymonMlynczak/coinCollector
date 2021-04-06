package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PersonInMemoryManager implements PersonRepo {

    List<Person> personArrayList = Arrays.asList(
            new Person("Ada",2),
            new Person("Qń",0),
            new Person("Marek",0),
            new Person("Han",0),
            new Person("Oltek",0),
            new Person("Zamor",0),
            new Person("mlodyZamor",0),
            new Person("Roksa",0),
            new Person("Bleid",0),
            new Person("Andrzej",0),
            new Person("Kris",0),
            new Person("Bany",0)
    );

    @Override
    public void savePerson() {

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
