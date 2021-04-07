package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({SortPricingNotExistException.class,
            SortPricingAlreadyExistsException.class})
    public ResponseEntity<String> handleException(RuntimeException exception){
        return buildResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> buildResponseEntity( String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}
