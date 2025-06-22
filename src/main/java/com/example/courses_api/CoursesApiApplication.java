package com.example.courses_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.courses_api.repository")
@EntityScan(basePackages = "com.example.courses_api.entity")
public class CoursesApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoursesApiApplication.class, args);
    }
}
