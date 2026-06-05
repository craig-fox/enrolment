package com.example.enrolment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class EnrolmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrolmentApplication.class, args);
    }

    @GetMapping("/")
    public String home() {

        return "app running";

    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

}
