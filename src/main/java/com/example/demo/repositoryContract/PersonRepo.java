package com.example.demo.repositoryContract;
import com.example.demo.businessLogic.person.Person;

import java.util.List;

public interface PersonRepo {

    Person savePerson(Person person);
    Person getPerson(String name);
    List<Person> getAllPerson();
}
