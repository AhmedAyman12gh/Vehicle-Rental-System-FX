package com.vehicle_project;

public class Van extends Vehicle  {
   
    private final String vanType; // e.g., Minivan, Cargo Van, Passenger Van, etc.
    
     
    
    
    
    public Van(String vehicleId, String brand, String model, int year, double pricePerDay, int quantity, String vanType) {
        super(vehicleId, brand, model, year, pricePerDay, quantity);
        this.vanType = vanType;
    }
    
    public String getVanType() {
        return vanType;
    }


    @Override
    public String getVehicleInfo() {
        return "Car [ID=" + getVehicleId() + ", Brand=" + getBrand() + ", Model=" + getModel() + ", Year=" + getYear() +
               ", PricePerDay=" + getPricePerDay() + ", CarType=" + getVanType() + ", Available=" + isAvailable() + "]";
    }


    

   


    // implementing Rentable interface methods

    @Override
    public void rent(Customer customer, int days , Admin admin) {
        if (isAvailable()) {
            setQuantity(getQuantity() - 1);
            System.out.println("Van rented to " + customer.getName() + " for " + days + " days.");
        } else {
            System.out.println("Van is not available for rent.");
        }
        
    }

    @Override
    public double getRentalPrice(int days) {   // Calculate rental price
        return getPricePerDay() * days;
    }

    @Override
    public void returnItem() {    // Return the rented van
        setQuantity(getQuantity() + 1);
        System.out.println("Van returned successfully.");   // Confirmation message
    }

    @Override
    public String getDescription() {    
        return "Van: " + getBrand() + " " + getModel() + " (" + getYear() + "), Type: " + getVanType();
    }

    

    
    






}

   
    
    
    
    
    

    
   
   

    

