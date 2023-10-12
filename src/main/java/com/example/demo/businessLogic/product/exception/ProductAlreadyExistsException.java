package com.example.demo.businessLogic.product.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = "You can't add product because it already exists: ";

    public ProductAlreadyExistsException(String productName, Float price) { super(MESSAGE + productName +" price: "+ price); }
}
