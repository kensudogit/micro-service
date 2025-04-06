package com.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// This annotation marks the class as a Spring Boot application
// and enables auto-configuration.
@SpringBootApplication
// This annotation allows the service to register with a discovery server
// such as Eureka for service discovery.
@EnableDiscoveryClient
public class ProductServiceApplication {
    // The main method serves as the entry point for the Spring Boot application.
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
} 