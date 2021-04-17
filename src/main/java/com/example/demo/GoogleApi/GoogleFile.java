package com.example.demo.GoogleApi;

public enum GoogleFile {

     SALE("sale"),
     PRODUCT("product"),
     PERSON("person");

    String type;

    GoogleFile(String whichType) {
        type = whichType;
    }
}
