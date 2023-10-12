package com.example.demo.businessLogic.person.exception;


public class PersonNotExistsException extends RuntimeException {
    public static final String MESSAGE = "You can't add sale because person not exists: ";

    public PersonNotExistsException(String personName) { super(MESSAGE + personName); }
}
