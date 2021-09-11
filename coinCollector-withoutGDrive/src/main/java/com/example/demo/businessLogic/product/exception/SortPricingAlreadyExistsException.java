package com.example.demo.businessLogic.product.exception;

public class SortPricingAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = "You can't add sort pricing because it already exists: ";

    public SortPricingAlreadyExistsException(String product, Float price) { super(MESSAGE + product +" price: "+ price); }
}
