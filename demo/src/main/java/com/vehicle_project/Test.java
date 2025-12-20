package com.vehicle_project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Test {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("     VEHICLE RENTAL SYSTEM - MANUAL TEST SUITE");
        System.out.println("════════════════════════════════════════════════════════════════\n");
        
        // Test 1: Vehicle Creation and Basic Info
        test1_VehicleCreation();
        
        // Test 2: Sorting Test with Custom Comparator (CORE REQUIREMENT)
        test2_SortingWithGenericAlgorithm();
        
        // Test 3: Rental Workflow (Customer → Booking → Admin Approval)
        test3_RentalWorkflow();
        
        // Test 4: Inventory Management (Quantity tracking)
        test4_InventoryManagement();
        
        // Test 5: Exception Handling
        test5_ExceptionHandling();
        
        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("     ALL TESTS COMPLETED!");
        System.out.println("════════════════════════════════════════════════════════════════");
    }
    
    // ============================================================
    // TEST 1: Vehicle Creation and Basic Info
    // ============================================================
    private static void test1_VehicleCreation() {
        System.out.println("TEST 1: Vehicle Creation and Basic Info");
        System.out.println("-------------------");
        
        try {
            Vehicle car = new Car("C001", "Toyota", "Camry", 2023, 50.0, 5, "Sedan");
            System.out.println("Created Vehicle: " + car.getBrand() + " " + car.getModel());
            System.out.println(car.getVehicleInfo());
            System.out.println("Is Available: " + car.isAvailable());
            System.out.println("✓ TEST PASSED: Vehicle created successfully\n");
        } catch (Exception e) {
            System.out.println("✗ TEST FAILED: " + e.getMessage() + "\n");
        }
        
        System.out.println("-------------------\n");
    }
    
    // ============================================================
    // TEST 2: Sorting Test with Generic Algorithm (CORE REQUIREMENT)
    // ============================================================
    private static void test2_SortingWithGenericAlgorithm() {
        System.out.println("TEST 2: Sorting Test with Generic Algorithm");
        System.out.println("-------------------");
        
    try {
    // 1. Create vehicles with mixed prices and years
    Vehicle bike1 = new Bike("B001", "Yamaha", "YZF-R3", 2019, 100.0, 3, "Sport");
    Vehicle bike2 = new Bike("B002", "Honda", "CBR500R", 2021, 35.0, 2, "Sport");
    Vehicle car1 = new Car("C002", "Ford", "Mustang", 2018, 80.0, 4, "Coupe");
    Vehicle van1 = new Van("V001", "Mercedes", "Sprinter", 2022, 70.0, 2, "Cargo");
    Vehicle car2 = new Car("C003", "Tesla", "Model 3", 2024, 90.0, 3, "Sedan");
    
    // 2. Create ArrayList
    List<Vehicle> vehicles = new ArrayList<>();
    vehicles.add(bike1);
    vehicles.add(bike2);
    vehicles.add(car1);
    vehicles.add(van1);
    vehicles.add(car2);
    
    // 3. Display UNSORTED list
    System.out.println("UNSORTED Vehicles:");
    for (Vehicle v : vehicles) {
        System.out.println("  " + v.getBrand() + " " + v.getModel() + 
                           " | Year: " + v.getYear() + 
                           " | Price: $" + v.getPricePerDay());
    } 

    // 4. SORTING LOGIC
    // Because you implemented Comparable (compareTo) in the Vehicle class,
    // you don't need to pass a Comparator here.
    Collections.sort(vehicles); 

    // Display SORTED list by Price Per Day
    System.out.println("\nSORTED Vehicles by Price Per Day (Ascending):");
    for (Vehicle v : vehicles) {
        System.out.println("  " + v.getBrand() + " " + v.getModel() + 
                           " | Year: " + v.getYear() + 
                           " | Price: $" + v.getPricePerDay());
    }

} catch (Exception e) {
    System.out.println("✗ TEST FAILED: " + e.getMessage());
    e.printStackTrace(); // Helpful to see specific line numbers if it fails
}

System.out.println("-------------------\n");
    }
    
    // ============================================================
    // TEST 3: Rental Workflow (Customer → Booking → Admin Approval)
    // ============================================================
    private static void test3_RentalWorkflow() {
        System.out.println("TEST 3: Rental Workflow (Customer → Booking → Admin Approval)");
        System.out.println("-------------------");
        
        try {
            // Setup
            Admin admin = new Admin("Admin User", "admin@rental.com");
            Customer customer = new Customer("John Doe", "john@email.com");
            Car car = new Car("C004", "Honda", "Accord", 2023, 45.0, 3, "Sedan");
            
            System.out.println("Initial Setup:");
            System.out.println("  Vehicle: " + car.getDescription());
            System.out.println("  Initial Quantity: " + car.getQuantity());
            System.out.println();
            
            // Step 1: Customer creates booking request
            System.out.println("Step 1: Customer creates booking request...");
            LocalDate rentalDate = LocalDate.now();
            LocalDate returnDate = LocalDate.now().plusDays(3);
            Booking booking = customer.requestVehicle(car, rentalDate, returnDate);
            
            System.out.println("  Booking ID: " + booking.getBookingId());
            System.out.println("  Total Cost: $" + booking.getTotalCost());
            System.out.println("  Status: " + booking.getStatus());
            System.out.println("  Expected: PENDING");
            
            if (booking.getStatus() == Booking.BookingStatus.PENDING) {
                System.out.println("  ✓ Status is PENDING as expected");
            } else {
                System.out.println("  ✗ Status is NOT PENDING!");
            }
            System.out.println();
            
            // Step 2: Admin approves booking
            System.out.println("Step 2: Admin approves booking...");
            admin.approveBooking(booking);
            
            System.out.println("  Status After Approval: " + booking.getStatus());
            System.out.println("  Expected: APPROVED");
            System.out.println("  Paid: " + booking.isPaid());
            
            if (booking.getStatus() == Booking.BookingStatus.APPROVED && booking.isPaid()) {
                System.out.println("  ✓ Booking approved and marked as paid");
            } else {
                System.out.println("  ✗ Booking approval failed!");
            }
            System.out.println();
            
            // Step 3: Verify quantity decreased
            System.out.println("Step 3: Verify inventory updated...");
            System.out.println("  Quantity After Approval: " + car.getQuantity());
            System.out.println("  Expected: 2 (decreased from 3)");
            
            if (car.getQuantity() == 2) {
                System.out.println("  ✓ Quantity decreased correctly");
            } else {
                System.out.println("  ✗ Quantity did not decrease!");
            }
            
            System.out.println("\n✓ TEST PASSED: Rental workflow completed successfully\n");
            
        } catch (Exception e) {
            System.out.println("✗ TEST FAILED: " + e.getMessage() + "\n");
        }
        
        System.out.println("-------------------\n");
    }
    
    // ============================================================
    // TEST 4: Inventory Management (Quantity Tracking)
    // ============================================================
    private static void test4_InventoryManagement() {
        System.out.println("TEST 4: Inventory Management (Quantity Tracking)");
        System.out.println("-------------------");
        
        try {
            Customer customer = new Customer("Alice", "alice@email.com");
            Admin admin = new Admin("Bob", "bob@rental.com");
            Car car = new Car("C005", "Chevrolet", "Malibu", 2021, 60.0, 5, "Sedan");
            
            System.out.println("Before renting: " + car.getVehicleInfo());
            System.out.println("  Quantity: " + car.getQuantity());
            System.out.println("  Available: " + car.isAvailable());
            System.out.println();
            
            // Rent the vehicle
            System.out.println("Renting vehicle...");
            car.rent(customer, 3, admin);
            
            System.out.println("After renting: " + car.getVehicleInfo());
            System.out.println("  Quantity: " + car.getQuantity());
            System.out.println("  Expected: 4 (decreased from 5)");
            
            if (car.getQuantity() == 4) {
                System.out.println("  ✓ Quantity decreased correctly after rent");
            } else {
                System.out.println("  ✗ Quantity not updated after rent!");
            }
            System.out.println();
            
            // Return the vehicle
            System.out.println("Returning vehicle...");
            car.returnItem();
            
            System.out.println("After returning: " + car.getVehicleInfo());
            System.out.println("  Quantity: " + car.getQuantity());
            System.out.println("  Expected: 5 (increased back to original)");
            
            if (car.getQuantity() == 5) {
                System.out.println("  ✓ Quantity increased correctly after return");
            } else {
                System.out.println("  ✗ Quantity not updated after return!");
            }
            
            System.out.println("\n✓ TEST PASSED: Inventory management working correctly\n");
            
        } catch (Exception e) {
            System.out.println("✗ TEST FAILED: " + e.getMessage() + "\n");
        }
        
        System.out.println("-------------------\n");
    }
    
    // ============================================================
    // TEST 5: Exception Handling
    // ============================================================
    private static void test5_ExceptionHandling() {
        System.out.println("TEST 5: Exception Handling");
        System.out.println("-------------------");
        
        // Test 5.1: Invalid Vehicle Creation
        System.out.println("Test 5.1: Invalid Vehicle Creation (negative price)");
        try {
            Vehicle invalidCar = new Car("C999", "Toyota", "Corolla", 2023, -50.0, 5, "Sedan");
            System.out.println("✗ TEST FAILED: Should have thrown exception for negative price\n");
        } catch (IllegalArgumentException e) {
            System.out.println("  Exception caught: " + e.getMessage());
            System.out.println("  ✓ TEST PASSED: Exception handled correctly\n");
        }
        
        // Test 5.2: Customer trying to add vehicle quantity
        System.out.println("Test 5.2: Customer trying to add vehicle quantity (security check)");
        try {
            Customer customer = new Customer("Eve", "eve@email.com");
            Car car = new Car("C006", "Ford", "Focus", 2023, 40.0, 5, "Sedan");
            customer.attemptAddVehicle(car, 10);
            System.out.println("✗ TEST FAILED: Should have thrown SecurityException\n");
        } catch (SecurityException e) {
            System.out.println("  Exception caught: " + e.getMessage());
            System.out.println("  ✓ TEST PASSED: Security check working correctly\n");
        }
        
        // Test 5.3: Invalid booking dates
        System.out.println("Test 5.3: Invalid Booking Dates (return before rental)");
        try {
            Customer customer = new Customer("Charlie", "charlie@email.com");
            Car car = new Car("C007", "Nissan", "Altima", 2023, 55.0, 3, "Sedan");
            LocalDate rentalDate = LocalDate.now();
            LocalDate returnDate = LocalDate.now().minusDays(1); // Invalid: return before rental
            Booking booking = customer.requestVehicle(car, rentalDate, returnDate);
            System.out.println("✗ TEST FAILED: Should have thrown exception for invalid dates\n");
        } catch (IllegalArgumentException e) {
            System.out.println("  Exception caught: " + e.getMessage());
            System.out.println("  ✓ TEST PASSED: Date validation working correctly\n");
        }
        
        System.out.println("-------------------\n");
    }
}

