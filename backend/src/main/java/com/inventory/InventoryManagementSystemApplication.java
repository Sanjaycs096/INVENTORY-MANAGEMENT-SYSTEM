package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Inventory Management System
 */
@SpringBootApplication
public class InventoryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementSystemApplication.class, args);
        System.out.println("==============================================");
        System.out.println("Inventory Management System Started");
        System.out.println("Server running at: http://localhost:8080");
        System.out.println("API Documentation available soon!");
        System.out.println("==============================================");
    }
}
