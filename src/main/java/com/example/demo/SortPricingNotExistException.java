package com.example.demo;

public class SortPricingNotExistException extends RuntimeException {

    public static final String MESSAGE = "You can't add sale because Sort not exists: ";

    public SortPricingNotExistException(Product product, Float price) { super(MESSAGE + product.name() +" price: "+ price); }

}
