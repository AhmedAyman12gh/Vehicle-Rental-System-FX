package com.vehicle_project;

public class Bike extends Vehicle {
   
    private final String bikeType; // e.g., Minivan, Cargo Van, Passenger Van, etc.
  
     
    
    
    
    public Bike(String vehicleId, String brand, String model, int year, double pricePerDay, int quantity, String bikeType) {
        super(vehicleId, brand, model, year, pricePerDay, quantity);
        this.bikeType = bikeType;
    }
    
    public String getBikeType() {
        return bikeType;
    }
    @Override
    public String getVehicleInfo() {
        return "Car [ID=" + getVehicleId() + ", Brand=" + getBrand() + ", Model=" + getModel() + ", Year=" + getYear() +
               ", PricePerDay=" + getPricePerDay() + ", CarType=" + getBikeType() + ", Available=" + isAvailable() + "]";
    }

    
    // implementing Rentable interface methods
    @Override
    public String getDescription() {    
        return "Bike: " + getBrand() + " " + getModel() + " (" + getYear() + "), Type: " + getBikeType();
    }

    @Override
    public void rent(Customer customer, int days , Admin admin) {
        if (isAvailable()) {
            setQuantity(getQuantity() - 1);
            System.out.println("Bike rented to " + customer.getName() + " for " + days + " days.");
        } else {
            System.out.println("Bike is not available for rent.");
        }
        
    }
    @Override
    public double getRentalPrice(int days) {   // Calculate rental price
        return getPricePerDay() * days;
    }
    @Override
    public void returnItem() {    // Return the rented bike
        setQuantity(getQuantity() + 1);
        System.out.println("Bike returned successfully.");   // Confirmation message
    }



    



}