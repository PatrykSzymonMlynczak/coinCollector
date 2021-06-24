package com.example.demo.businessLogic.person;
import java.util.List;


public interface PersonRepo {

    void savePerson();

    Person getPerson(String name);

    List<Person> getAllPerson();
}
