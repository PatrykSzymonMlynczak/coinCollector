package com.example.demo.repositoryContract;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.dto.SaleDto;

import java.util.List;

public interface PersonRepo {

    Person savePerson(Person person);

    Person getPerson(String name);

    List<Person> getAllPerson();

    void updateDebt(Float debt, String name);

    SaleDto payDebt(Float payedDebt, String name);

}
