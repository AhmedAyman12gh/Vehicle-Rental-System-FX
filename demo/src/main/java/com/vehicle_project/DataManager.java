package com.vehicle_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {
    private static ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private static ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<Admin> adminList = FXCollections.observableArrayList();
    private static ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    
    public static ObservableList<Vehicle> getVehicleList() {
        return vehicleList;
    }
    
    public static ObservableList<Booking> getBookingList() {
        return bookingList;
    }
    
    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }
    
    public static ObservableList<Admin> getAdminList() {
        return adminList;
    }
    
    public static ObservableList<Payment> getPaymentList() {
        return paymentList;
    }
}
