package com.example.demo.businessLogic.sale.exception;


public class ProductNotExistException extends RuntimeException {
    public static final String MESSAGE = "You can't add sale because product not exists: ";

    public ProductNotExistException(String product) { super(MESSAGE + product); }
}
