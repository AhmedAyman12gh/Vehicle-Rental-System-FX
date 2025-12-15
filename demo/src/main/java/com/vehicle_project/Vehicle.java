package com.vehicle_project;
public abstract class Vehicle implements Rentable, Comparable<Vehicle>  {
    private String vehicleId;      
    private String brand;          
    private String model;          
    private int year;            
    private double pricePerDay;  
    private int quantity; 


    public Vehicle(String vehicleId, String brand, String model, int year, double pricePerDay, int quantity) {
      
        
       
        if (vehicleId == null || vehicleId.isBlank()) {
        
        throw new IllegalArgumentException("Vehicle ID cannot be null or empty");
    }
    

    if (brand == null || brand.isBlank()) {
        throw new IllegalArgumentException("Brand cannot be null or empty");
    }

    if (model == null || model.isBlank()) {
        throw new IllegalArgumentException("Model cannot be null or empty");
    }

    if (year < 1886) { 
        throw new IllegalArgumentException("Year is not valid");
    }

    if (pricePerDay <= 0) {
        throw new IllegalArgumentException("Price per day must be greater than 0");
    }
    if (quantity < 0) {
        throw new IllegalArgumentException("Quantity cannot be negative");
    }

        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.quantity = quantity;

    
    }
   public String getVehicleId() {
        return vehicleId;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public int getYear() {
        return year;
    }
    
    public double getPricePerDay() {
        return pricePerDay;
    }
    
    public void setPricePerDay(double pricePerDay) {
        if (pricePerDay <= 0) {
            throw new IllegalArgumentException("Price per day must be greater than 0");
        }
        this.pricePerDay = pricePerDay;
    }

    
   @Override
    public boolean isAvailable() {
        return quantity >0;
    }


     public void setQuantity(int quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            this.quantity = quantity;
     }
    
    public int getQuantity() {
        return quantity;
    }


    //////////////////////////////////////////////////////////
 // Enhanced addQuantity with role checking
    public void addQuantity(int quantity, User user) {
        // Check 1: User must not be null
        if (user == null) {
            throw new SecurityException("User cannot be null!");
        }

         //Check 2: User must be Admin
       if (!user.hasRole(User.UserRole.ADMIN)) {
           throw new SecurityException("Only Admin can add quantity! User " + user.getName() + " is not authorized.");
       }

        // Check 3: Quantity validation
        if (quantity < -this.quantity) {
            throw new IllegalArgumentException("Quantity cannot be negative after update");
        }

        // Update quantity
        this.quantity += quantity;
        System.out.println("Quantity updated by Admin " + user.getName());
    }
    ///////////////////////////////////////////////////////////
    
    public abstract String getVehicleInfo();
    
    @Override
    public String toString() {
        return getVehicleInfo();
    }

     @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.pricePerDay, other.pricePerDay);
    }

          
  
}

    