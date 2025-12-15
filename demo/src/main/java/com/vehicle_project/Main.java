package com.vehicle_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
// ... other imports ...


// import javafx.scene.layout.VBox;
// import javafx.scene.layout.HBox;
// import javafx.scene.control.*;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class Main extends Application {
    
    private static Stage primaryStage;
    private static final String APP_TITLE = "Vehicle Rental Management System";
    
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        
        // Initialize sample data
        initializeSampleData();
        
        // Show login view
        showLoginView();
        
        primaryStage.show();
    }
    
    public static void showLoginView() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE + " - Login");
    }
    
    public static void showCustomerView(Customer customer) {
        CustomerView customerView = new CustomerView(customer);
        Scene scene = new Scene(customerView, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE + " - Customer Portal");
    }
    
    public static void showAdminView(Admin admin) {
        AdminView adminView = new AdminView(admin);
        Scene scene = new Scene(adminView, 1100, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE + " - Admin Portal");
    }
    
    private void initializeSampleData() {
        // Create sample vehicles
        Car car1 = new Car("C001", "Toyota", "Camry", 2023, 50.0, 3, "Sedan");
        Car car2 = new Car("C002", "Honda", "Accord", 2022, 45.0, 2, "Sedan");
        Car car3 = new Car("C003", "Ford", "Explorer", 2023, 75.0, 1, "SUV");
        Van van1 = new Van("V001", "Mercedes", "Sprinter", 2023, 85.0, 2, "Cargo Van");
        Bike bike1 = new Bike("B001", "Yamaha", "MT-07", 2023, 30.0, 5, "Sport");
        
        DataManager.getVehicleList().addAll(car1, car2, car3, van1, bike1);
        
        // Create sample users
        Customer sampleCustomer = new Customer("John Doe", "john@email.com");
        Customer sampleCustomer2 = new Customer("Jane Smith", "jane@email.com");
        Admin sampleAdmin = new Admin("Admin User", "admin@email.com");
        
        DataManager.getCustomerList().addAll(sampleCustomer, sampleCustomer2);
        DataManager.getAdminList().add(sampleAdmin);
        
        // Create sample booking
        Booking booking1 = new Booking(sampleCustomer, car1, 
                                      LocalDate.now(), LocalDate.now().plusDays(3));
        DataManager.getBookingList().add(booking1);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

// DataManager class to hold application data


