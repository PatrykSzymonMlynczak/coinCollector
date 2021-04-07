package com.example.demo.exceptions;

public class StartDateIsAfterEndDateException extends RuntimeException {

    public StartDateIsAfterEndDateException(String dateStart, String dateEnd) {
         super("Wrong date order. Start date : " + dateStart +" must be earlier then end date: "+ dateEnd);
    }
}
