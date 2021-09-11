package com.example.demo.businessLogic.sale.exception;

public class StartDateIsAfterEndDateException extends RuntimeException {

    public StartDateIsAfterEndDateException(String dateStart, String dateEnd) {
         super("Wrong date order. Start date : " + dateStart +" must be earlier than end date: "+ dateEnd);
    }
}
