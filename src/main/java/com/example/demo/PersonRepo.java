package com.example.demo;

import java.util.List;


public interface PersonRepo {

    void savePerson();

    Person getPerson(String name);

    List<Person> getAllPerson();
}
