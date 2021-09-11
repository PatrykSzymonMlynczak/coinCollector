package com.example.demo.exception;

public class JsonFileNotFoundException extends RuntimeException {
    public JsonFileNotFoundException(String fileName) {
        super("File: "+fileName+" not Found");
    }
}
