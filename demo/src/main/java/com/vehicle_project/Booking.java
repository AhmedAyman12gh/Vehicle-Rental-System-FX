package com.vehicle_project;

import java.time.LocalDate;
public class Booking {
    private String bookingId;
    private Customer customer;
    private Rentable rentedItem; // Using the Interface type!
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double totalCost;
    private boolean isPaid;
    private BookingStatus status;
    public static int bookingCounter = 0; 



    public enum BookingStatus {
        PENDING,
        APPROVED,
        REJECTED,
        COMPLETED,
        CANCELLED
    }
    

    public Booking(Customer customer, Rentable rentedItem, LocalDate rentalDate, LocalDate returnDate) {
        
        // 1. Validate Customer
        if (customer == null) {
            throw new IllegalArgumentException("Error: Customer cannot be null.");
        }

        // 2. Validate Item
        if (rentedItem == null) {
            throw new IllegalArgumentException("Error: Rented item cannot be null.");
        }

        // 3. Validate Dates exist
        if (rentalDate == null || returnDate == null) {
            throw new IllegalArgumentException("Error: Dates cannot be null.");
        }

        // 4. Validate Date Logic (Time Travel Check)
        if (returnDate.isBefore(rentalDate)) {
            throw new IllegalArgumentException("Error: Return date cannot be before rental date.");
        }
        this.bookingId = "B" + bookingCounter;
        this.customer = customer;
        this.rentedItem = rentedItem;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.isPaid = false;
        this.status = BookingStatus.PENDING;
        calculateTotalCost();
        bookingCounter++;
    }

     private void calculateTotalCost() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(rentalDate, returnDate);
        this.totalCost = rentedItem.getRentalPrice((int) days);
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Rentable getRentedItem() {
        return rentedItem;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }   

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void makePayment() {
        this.isPaid = true;
    }
    public String getDetails() {
        return "Booking ID: " + bookingId + "\n" +
               "Customer: " + customer.getName() + "\n" +
               "Vehicle: " + rentedItem.getDescription() + "\n" +
               "Total Cost: $" + totalCost;
    }

    public void confirmBooking(Admin admin) {
        // Check 1: Only Admin can confirm
        if (admin == null) {
            throw new SecurityException("Only Admin can approve bookings.");
        }

        // Check 2: Must be Admin role
       if (!admin.hasRole(User.UserRole.ADMIN)) {
           throw new SecurityException("User does not have Admin privileges!");
       }

        // Check 3: Booking must be pending
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in pending state. Current status: " + status);
        }

        // Check 4: Check availability
        if (!rentedItem.isAvailable()) {
            throw new IllegalStateException("Vehicle is no longer available.");
        }

        // Approve the booking
        this.status = BookingStatus.APPROVED;
        this.isPaid = true;

        // Perform the rental
        rentedItem.rent(customer, (int) java.time.temporal.ChronoUnit.DAYS.between(rentalDate, returnDate), admin);
    }

    public BookingStatus getStatus() {
        return status;
    }


    
}



