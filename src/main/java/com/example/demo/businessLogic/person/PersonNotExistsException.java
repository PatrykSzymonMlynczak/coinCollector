package com.example.demo.businessLogic.person;


public class PersonNotExistsException extends RuntimeException {
    public static final String MESSAGE = "You can't add sale because Person not exists: ";

    public PersonNotExistsException(String personName) { super(MESSAGE + personName); }
}
