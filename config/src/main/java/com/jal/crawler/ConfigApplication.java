package com.jal.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ConfigApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
