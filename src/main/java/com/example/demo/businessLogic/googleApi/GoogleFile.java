package com.example.demo.businessLogic.googleApi;

public enum GoogleFile {

     SALE("sale"),
     PRODUCT("product"),
     PERSON("person");

    String type;

    GoogleFile(String whichType) {
        type = whichType;
    }
}
