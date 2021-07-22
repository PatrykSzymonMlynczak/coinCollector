package com.example.demo.exceptionAdvice;

import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.businessLogic.sale.exception.StartDateIsAfterEndDateException;
import com.example.demo.fileManager.exception.JsonFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({
             ProductNotExistException.class,
            JsonFileNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException exception){
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({
            ProductAlreadyExistsException.class,
            StartDateIsAfterEndDateException.class})

    public ResponseEntity<ApiError> handleException(RuntimeException exception){
        return buildResponseEntity(HttpStatus.CONFLICT, exception);
    }

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, Throwable exception) {
        ApiError apiError = new ApiError(status, exception);
        return new ResponseEntity<>(apiError, status);
    }
}
