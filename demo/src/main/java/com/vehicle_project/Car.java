package com.vehicle_project;

public class Car extends Vehicle {
    
    
    private final String carType; // e.g., Sedan, SUV, Coupe

    // Constructor
    public Car(String vehicleId, String brand, String model, int year, double pricePerDay, int quantity, String carType) {
        super(vehicleId, brand, model, year, pricePerDay, quantity);
        this.carType = carType;
    }
    

    public String getCarType() {
        return carType;
    }


    @Override
    public String getVehicleInfo() {
        return "Car [ID=" + getVehicleId() + ", Brand=" + getBrand() + ", Model=" + getModel() + ", Year=" + getYear() +
               ", PricePerDay=" + getPricePerDay() + ", CarType=" + getCarType() + ", Available=" + isAvailable() + "]";
    }




    // Implementing Rentable interface methods


    @Override
    public void rent(Customer customer, int days , Admin admin) {
        if (isAvailable()) {
            setQuantity(getQuantity() - 1);
            System.out.println("Car rented to " + customer.getName() + " for " + days + " days.");
        } else {
            System.out.println("Car is not available for rent.");
        }
        
    }

    @Override
    public double getRentalPrice(int days) {   // Calculate rental price
        return getPricePerDay() * days;
    }

    @Override
    public void returnItem() {    // Return the rented car
        setQuantity(getQuantity() + 1);
        System.out.println("Car returned successfully.");   // Confirmation message
    }

    @Override
    public String getDescription() {
        return "Car: " + getBrand() + " " + getModel() + " (" + getYear() + "), Type: " + getCarType();
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

   
}


