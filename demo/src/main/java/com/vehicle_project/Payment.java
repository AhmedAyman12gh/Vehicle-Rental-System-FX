package com.vehicle_project;

import java.time.LocalDate;

public class Payment {
    private String paymentId;
    private String bookingId; 
    private Customer customer;
    private double amount;
    private LocalDate paymentDate;
    private static int paymentCounter = 0;

    // Updated Constructor to include bookingId
    public Payment(String bookingId, Customer customer, double amount, LocalDate paymentDate) {
        
        // Validation: Booking ID
        if (bookingId == null || bookingId.isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be empty!");
        }
        
        // Validation: Customer
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null!");
        }

        // Validation: Amount
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }

        // Validation: Date
        if (paymentDate == null) {
            throw new IllegalArgumentException("Payment date cannot be null!");
        }
        if (paymentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Payment date cannot be in the future!");
        }

        paymentCounter++;
        this.paymentId = "P" + paymentCounter;
        this.bookingId = bookingId; 
        this.customer = customer;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // --- Getters ---
    public String getPaymentId() {
        return paymentId;
    }

    // NEW: Getter for bookingId
    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    // --- Setters ---
    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }
        this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null) {
            throw new IllegalArgumentException("Payment date cannot be null!");
        }
        
        if (paymentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Payment date cannot be in the future!");
        }
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return String.format(
            "Payment ID: %s | Booking ID: %s | Customer: %s | Amount: $%.2f | Date: %s",
            paymentId, bookingId, customer.getName(), amount, paymentDate
        );
    }
}