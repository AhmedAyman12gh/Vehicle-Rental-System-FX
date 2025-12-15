package com.vehicle_project;

import java.time.LocalDate;
public class Customer extends User {

    public Customer(String name, String email) {
        super(name, email, UserRole.CUSTOMER);
    }

    public Booking requestVehicle(Rentable vehicle, LocalDate rentalDate, LocalDate returnDate) {
        Booking request = new Booking(this, vehicle, rentalDate, returnDate);
        System.out.println("Request submitted by " + getName() + " for " + vehicle.getDescription());
        return request;
    }

    // Customers CANNOT add vehicles - this will throw exception
    public void attemptAddVehicle(Vehicle vehicle, int quantity) {
        throw new SecurityException("Customers do not have permission to add vehicles!");
    }

    // Customers CANNOT approve bookings
    public void attemptApproveBooking(Booking booking) {
        throw new SecurityException("Customers do not have permission to approve bookings!");
    }
}