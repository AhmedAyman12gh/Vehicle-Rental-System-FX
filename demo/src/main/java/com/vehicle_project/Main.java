package com.vehicle_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
class DataManager {
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

// Enhanced LoginView
class LoginView extends VBox {
    
    public LoginView() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        
        // Main container with white background
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setMaxWidth(350);
        mainContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                              "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        // Title
        Label titleLabel = new Label("Vehicle Rental System");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #667eea;");
        
        // Subtitle
        Label subtitleLabel = new Label("Welcome! Please login to continue");
        subtitleLabel.setFont(Font.font("System", 13));
        subtitleLabel.setStyle("-fx-text-fill: #666;");
        
        // Separator
        Separator separator1 = new Separator();
        separator1.setPrefWidth(300);
        
        // Email field
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        emailLabel.setStyle("-fx-text-fill: #333;");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(300);
        emailField.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        
        // Role selection
        Label roleLabel = new Label("Select Your Role");
        roleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        roleLabel.setStyle("-fx-text-fill: #333;");
        
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton customerRadio = new RadioButton("Customer");
        RadioButton adminRadio = new RadioButton("Administrator");
        customerRadio.setToggleGroup(roleGroup);
        adminRadio.setToggleGroup(roleGroup);
        customerRadio.setSelected(true);
        customerRadio.setStyle("-fx-font-size: 12;");
        adminRadio.setStyle("-fx-font-size: 12;");
        
        HBox roleBox = new HBox(30);
        roleBox.setAlignment(Pos.CENTER);
        roleBox.getChildren().addAll(customerRadio, adminRadio);
        
        // Login button
        Button loginBtn = new Button("LOGIN");
        loginBtn.setPrefWidth(300);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                         "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand; " +
                         "-fx-background-radius: 5;");
        
        // Hover effect
        loginBtn.setOnMouseEntered(e -> 
            loginBtn.setStyle("-fx-background-color: #764ba2; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand; " +
                            "-fx-background-radius: 5;"));
        loginBtn.setOnMouseExited(e -> 
            loginBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand; " +
                            "-fx-background-radius: 5;"));
        
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            
            if (email.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter your email.");
                return;
            }
            
            if (customerRadio.isSelected()) {
                // Try to find existing customer
                Customer customer = DataManager.getCustomerList().stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
                
                if (customer == null) {
                    // Create new customer
                    String name = email.split("@")[0].replace(".", " ");
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    customer = new Customer(name, email);
                    DataManager.getCustomerList().add(customer);
                    showAlert(Alert.AlertType.INFORMATION, "Welcome", 
                             "New customer account created!\nWelcome, " + name + "!");
                }
                
                Main.showCustomerView(customer);
                
            } else {
                // Admin login
                Admin admin = DataManager.getAdminList().stream()
                    .filter(a -> a.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
                
                if (admin == null) {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", 
                             "Admin account not found.\n\nUse: admin@email.com");
                    return;
                }
                
                Main.showAdminView(admin);
            }
        });
        
        // Separator
        Separator separator2 = new Separator();
        separator2.setPrefWidth(300);
        
        // Quick login section
        Label quickLabel = new Label("Quick Access (For Testing)");
        quickLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        quickLabel.setStyle("-fx-text-fill: #999;");
        
        HBox quickBox = new HBox(10);
        quickBox.setAlignment(Pos.CENTER);
        
        Button quickCustomerBtn = new Button("Demo Customer");
        quickCustomerBtn.setPrefWidth(140);
        quickCustomerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                 "-fx-font-size: 11; -fx-cursor: hand; -fx-background-radius: 3;");
        quickCustomerBtn.setOnAction(e -> {
            if (!DataManager.getCustomerList().isEmpty()) {
                Main.showCustomerView(DataManager.getCustomerList().get(0));
            }
        });
        
        Button quickAdminBtn = new Button("Demo Admin");
        quickAdminBtn.setPrefWidth(140);
        quickAdminBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; " +
                              "-fx-font-size: 11; -fx-cursor: hand; -fx-background-radius: 3;");
        quickAdminBtn.setOnAction(e -> {
            if (!DataManager.getAdminList().isEmpty()) {
                Main.showAdminView(DataManager.getAdminList().get(0));
            }
        });
        
        quickBox.getChildren().addAll(quickCustomerBtn, quickAdminBtn);
        
        // Test info
        Label infoLabel = new Label("Admin Email: admin@email.com\nCustomer: Any email works!");
        infoLabel.setFont(Font.font("System", 10));
        infoLabel.setStyle("-fx-text-fill: #999; -fx-text-alignment: center;");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(280);
        
        mainContainer.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            separator1,
            emailLabel,
            emailField,
            roleLabel,
            roleBox,
            loginBtn,
            separator2,
            quickLabel,
            quickBox,
            infoLabel
        );
        
        getChildren().add(mainContainer);
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}