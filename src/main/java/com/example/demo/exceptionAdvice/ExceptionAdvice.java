package com.example.demo.exceptionAdvice;

import com.example.demo.businessLogic.person.exception.PersonAlreadyExistsException;
import com.example.demo.businessLogic.person.exception.PersonNotExistsException;
import com.example.demo.businessLogic.product.exception.NotEnoughSortException;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.businessLogic.sale.exception.StartDateIsAfterEndDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({
             ProductNotExistException.class,
            PersonNotExistsException.class,
            NotEnoughSortException.class})
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException exception){
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({
            ProductAlreadyExistsException.class,
            StartDateIsAfterEndDateException.class,
            PersonAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleException(RuntimeException exception){
        return buildResponseEntity(HttpStatus.CONFLICT, exception);
    }

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, Throwable exception) {
        ApiError apiError = new ApiError(status, exception);
        return new ResponseEntity<>(apiError, status);
    }
}
