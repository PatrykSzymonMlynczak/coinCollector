package com.example.demo.exceptions;

public class JsonFileNotFoundException extends RuntimeException {
    public JsonFileNotFoundException(String fileName) {
        super("File: "+fileName+" not Found");
    }
}
