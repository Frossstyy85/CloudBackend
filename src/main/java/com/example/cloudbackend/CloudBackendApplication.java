package com.example.cloudbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudBackendApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv("hello"));
        SpringApplication.run(CloudBackendApplication.class, args);
    }

}
