package com.example.demo.businessLogic.product.exception;

public class NotEnoughSortException extends RuntimeException {

    public static final String MESSAGE = "You can't sell it cause You have not enough sort:";

    public NotEnoughSortException(String productName, Float lackQuantity) { super(MESSAGE + productName +" lack: "+ lackQuantity); }
}
