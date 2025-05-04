package com.starly.starlybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StarlyBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarlyBeApplication.class, args);
    }

}
