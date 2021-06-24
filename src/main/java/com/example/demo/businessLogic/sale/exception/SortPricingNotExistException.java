package com.example.demo.businessLogic.sale.exception;

public class SortPricingNotExistException extends RuntimeException {

    public static final String MESSAGE = "You can't add sale because Sort not exists: ";

    public SortPricingNotExistException(String product, Float price) { super(MESSAGE + product +" price: "+ price); }

}



