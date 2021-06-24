package com.example.demo.businessLogic.sale.exception;

public class ProductNotExistException extends RuntimeException {
    public static final String MESSAGE = "You can't add sale because Sort not exists: ";

    public ProductNotExistException(String product, Float price) { super(MESSAGE + product +" price: "+ price); }
}
