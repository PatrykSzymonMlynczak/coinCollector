package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CoinCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinCollectorApplication.class, args);
    }

}
