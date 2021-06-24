package com.example.demo.exceptionAdvice;

import com.example.demo.businessLogic.product.exception.SortPricingAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.businessLogic.sale.exception.SortPricingNotExistException;
import com.example.demo.businessLogic.sale.exception.StartDateIsAfterEndDateException;
import com.example.demo.fileManager.exception.JsonFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({SortPricingNotExistException.class,
             SortPricingAlreadyExistsException.class,
             ProductNotExistException.class,
             StartDateIsAfterEndDateException.class,
            JsonFileNotFoundException.class,
             MalformedURLException.class})

    public ResponseEntity<String> handleException(RuntimeException exception){
        return buildResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> buildResponseEntity( String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}
