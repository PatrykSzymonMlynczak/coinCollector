package com.example.demo.businessLogic.person.exception;

public class PersonAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = "You can't add person because already exists: ";

    public PersonAlreadyExistsException(String person) { super(MESSAGE + person); }
}
