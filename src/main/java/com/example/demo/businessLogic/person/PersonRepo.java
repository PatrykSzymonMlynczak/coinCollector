package com.example.demo.businessLogic.person;
import java.util.List;


public interface PersonRepo {

    Person savePerson(Person person);

    Person getPerson(String name);

    List<Person> getAllPerson();
}
