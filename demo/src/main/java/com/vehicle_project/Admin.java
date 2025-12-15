package com.vehicle_project;

public class Admin extends User {

    public Admin(String name, String email) {
        super(name, email, UserRole.ADMIN);
    }

    // 1. Manage Inventory: Only Admin can add quantity
    public void addVehicleQuantity(Vehicle vehicle, int quantity) {
        if (vehicle == null) {
            System.out.println("Error: Vehicle cannot be null.");
            return;
        }
        try {
            vehicle.addQuantity(quantity, this);
            System.out.println("Successfully added " + quantity + " to " + vehicle.getBrand() + " " + vehicle.getModel());
        } catch (Exception e) {
            System.out.println("Failed to add quantity: " + e.getMessage());
        }
    }

    // 2. Manual Rent: Admin manually rents a vehicle to a customer
    public void rentVehicle(Rentable vehicle, Customer customer, int days) {
        if (vehicle == null || customer == null) {
            System.out.println("Error: Vehicle or Customer cannot be null.");
            return;
        }
        try {
            vehicle.rent(customer, days, this);
        } catch (Exception e) {
            System.out.println("Rent failed: " + e.getMessage());
        }
    }

    // 3. Approve Request: Admin approves a booking created by a Customer
    public void approveBooking(Booking booking) {
        if (booking == null) {
            System.out.println("Error: No booking provided to approve.");
            return;
        }

        try {
            booking.confirmBooking(this);
            System.out.println("Booking " + booking.getBookingId() + " has been APPROVED.");
        } catch (Exception e) {
            System.out.println("Approval Failed: " + e.getMessage());
        }
    }

    // 4. Reject booking (optional enhancement)
    public void rejectBooking(Booking booking, String reason) {
        if (booking == null) {
            System.out.println("Error: No booking provided to reject.");
            return;
        }
        System.out.println("Booking " + booking.getBookingId() + " has been REJECTED. Reason: " + reason);
    }

    // 5. Add new vehicle to system (optional)
    public void addNewVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Error: Vehicle cannot be null.");
            return;
        }
        System.out.println("Vehicle added to system: " + vehicle.getVehicleInfo());
    }
}