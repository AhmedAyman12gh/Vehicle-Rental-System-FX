package com.vehicle_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Main application class for the Vehicle Rental Management System.
 * This is a test implementation to demonstrate the LoginView functionality.
 * 
 * <p>Application Flow:</p>
 * <ol>
 *   <li>Application starts with LoginView</li>
 *   <li>User enters credentials and selects role</li>
 *   <li>System navigates to Admin or Customer view based on selection</li>
 *   <li>User can logout and return to LoginView</li>
 * </ol>
 * 
 * <p>This implementation uses pure Java API without CSS styling.</p>
 * 
 * @author Vehicle Rental System
 * @version 1.0
 */
public class Main extends Application {
    
    // ============================================================
    // STATIC FIELDS - Shared across all views
    // ============================================================
    
    /**
     * Primary stage reference - used to switch between scenes
     * Must be static to allow access from LoginView callbacks
     */
    private static Stage primaryStage;
    
    /**
     * Current logged-in user (can be Admin or Customer)
     * Stored for reference across views
     */
    private static User currentUser;
    
    // ============================================================
    // APPLICATION ENTRY POINT
    // ============================================================
    
    /**
     * Main method - launches the JavaFX application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * JavaFX start method - called when application launches.
     * Initializes the primary stage and shows the login view.
     * 
     * @param stage The primary stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Store the primary stage for later use
        primaryStage = stage;
        
        // Configure window properties
        primaryStage.setTitle("Vehicle Rental Management System");
        primaryStage.setWidth(600);
        primaryStage.setHeight(500);
        
        // Prevent window from being resized too small
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        
        // Show login view as the initial screen
        showLoginView();
        
        // Display the window
        primaryStage.show();
    }
    
    // ============================================================
    // VIEW NAVIGATION METHODS
    // ============================================================
    
    /**
     * Displays the login view.
     * This is the initial view shown when the application starts
     * and also shown when users logout.
     */
    public static void showLoginView() {
        // Create new login view
        LoginView loginView = new LoginView();
        
        // Create scene with the login view
        Scene scene = new Scene(loginView, 600, 500);
        
        // Set the scene on the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login - Vehicle Rental System");
        
        // Set focus to name field for better UX
        loginView.requestFocusOnNameField();
        
        // Clear current user
        currentUser = null;
    }
    
    /**
     * Displays the admin view for the logged-in admin user.
     * This method is called by LoginView after successful admin login.
     * 
     * @param admin The admin user who logged in
     */
    public static void showAdminView(Admin admin) {
        // Store current user
        currentUser = admin;
        
        // Create admin view
        VBox adminView = createAdminView(admin);
        
        // Create and set scene
        Scene scene = new Scene(adminView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin View - Vehicle Rental System");
    }
    
    /**
     * Displays the customer view for the logged-in customer user.
     * This method is called by LoginView after successful customer login.
     * 
     * @param customer The customer user who logged in
     */
    public static void showCustomerView(Customer customer) {
        // Store current user
        currentUser = customer;
        
        // Create customer view
        VBox customerView = createCustomerView(customer);
        
        // Create and set scene
        Scene scene = new Scene(customerView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer View - Vehicle Rental System");
    }
    
    // ============================================================
    // ADMIN VIEW CREATION
    // ============================================================
    
    /**
     * Creates the admin dashboard view.
     * This is a placeholder implementation that demonstrates successful login.
     * In a full implementation, this would contain vehicle management,
     * booking approvals, and other admin features.
     * 
     * @param admin The logged-in admin user
     * @return VBox containing the admin interface
     */
    private static VBox createAdminView(Admin admin) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        
        // Set background color
        BackgroundFill bgFill = new BackgroundFill(
            Color.rgb(236, 240, 241),
            CornerRadii.EMPTY,
            Insets.EMPTY
        );
        container.setBackground(new Background(bgFill));
        
        // === HEADER SECTION ===
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        
        // Welcome message
        Label welcomeLabel = new Label("Welcome, Administrator!");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.rgb(44, 62, 80));
        
        // User info
        Label userInfoLabel = new Label("Logged in as: " + admin.getName());
        userInfoLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        userInfoLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label emailLabel = new Label("Email: " + admin.getEmail());
        emailLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        emailLabel.setTextFill(Color.rgb(127, 140, 141));
        
        Label roleLabel = new Label("Role: " + admin.getRole());
        roleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        roleLabel.setTextFill(Color.rgb(231, 76, 60));
        
        headerBox.getChildren().addAll(welcomeLabel, userInfoLabel, emailLabel, roleLabel);
        
        // === CONTENT SECTION ===
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30, 0, 30, 0));
        
        // Admin features list
        Label featuresTitle = new Label("Admin Features:");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.rgb(52, 73, 94));
        
        Label feature1 = new Label("✓ Manage Vehicle Inventory");
        feature1.setFont(Font.font("System", 14));
        
        Label feature2 = new Label("✓ Approve/Reject Bookings");
        feature2.setFont(Font.font("System", 14));
        
        Label feature3 = new Label("✓ View Payment History");
        feature3.setFont(Font.font("System", 14));
        
        Label feature4 = new Label("✓ Add New Vehicles");
        feature4.setFont(Font.font("System", 14));
        
        Label feature5 = new Label("✓ Manage Customers");
        feature5.setFont(Font.font("System", 14));
        
        contentBox.getChildren().addAll(
            featuresTitle,
            feature1,
            feature2,
            feature3,
            feature4,
            feature5
        );
        
        // === ACTION BUTTONS ===
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        // Placeholder button for admin dashboard
        Button dashboardButton = createStyledButton(
            "Open Dashboard",
            Color.rgb(52, 152, 219),
            Color.rgb(41, 128, 185)
        );
        dashboardButton.setOnAction(e -> {
            showAlert(
                Alert.AlertType.INFORMATION,
                "Dashboard",
                "Dashboard Coming Soon",
                "The full admin dashboard will be implemented here.\n\n" +
                "Features will include:\n" +
                "• Vehicle management\n" +
                "• Booking approvals\n" +
                "• Payment tracking\n" +
                "• Report generation"
            );
        });
        
        // Logout button
        Button logoutButton = createStyledButton(
            "Logout",
            Color.rgb(231, 76, 60),
            Color.rgb(192, 57, 43)
        );
        logoutButton.setOnAction(e -> handleLogout());
        
        buttonBox.getChildren().addAll(dashboardButton, logoutButton);
        
        // === ASSEMBLE VIEW ===
        container.getChildren().addAll(headerBox, contentBox, buttonBox);
        
        return container;
    }
    
    // ============================================================
    // CUSTOMER VIEW CREATION
    // ============================================================
    
    /**
     * Creates the customer dashboard view.
     * This is a placeholder implementation that demonstrates successful login.
     * In a full implementation, this would contain vehicle browsing,
     * booking creation, and booking management features.
     * 
     * @param customer The logged-in customer user
     * @return VBox containing the customer interface
     */
    private static VBox createCustomerView(Customer customer) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        
        // Set background color
        BackgroundFill bgFill = new BackgroundFill(
            Color.rgb(236, 240, 241),
            CornerRadii.EMPTY,
            Insets.EMPTY
        );
        container.setBackground(new Background(bgFill));
        
        // === HEADER SECTION ===
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        
        // Welcome message
        Label welcomeLabel = new Label("Welcome, " + customer.getName() + "!");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.rgb(44, 62, 80));
        
        // User info
        Label userInfoLabel = new Label("Email: " + customer.getEmail());
        userInfoLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        userInfoLabel.setTextFill(Color.rgb(127, 140, 141));
        
        Label roleLabel = new Label("Role: " + customer.getRole());
        roleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        roleLabel.setTextFill(Color.rgb(46, 204, 113));
        
        headerBox.getChildren().addAll(welcomeLabel, userInfoLabel, roleLabel);
        
        // === CONTENT SECTION ===
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30, 0, 30, 0));
        
        // Customer features list
        Label featuresTitle = new Label("Available Actions:");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.rgb(52, 73, 94));
        
        Label feature1 = new Label("✓ Browse Available Vehicles");
        feature1.setFont(Font.font("System", 14));
        
        Label feature2 = new Label("✓ Create Booking Requests");
        feature2.setFont(Font.font("System", 14));
        
        Label feature3 = new Label("✓ View Your Bookings");
        feature3.setFont(Font.font("System", 14));
        
        Label feature4 = new Label("✓ View Booking History");
        feature4.setFont(Font.font("System", 14));
        
        contentBox.getChildren().addAll(
            featuresTitle,
            feature1,
            feature2,
            feature3,
            feature4
        );
        
        // === ACTION BUTTONS ===
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        // Placeholder button for vehicle browsing
        Button browseButton = createStyledButton(
            "Browse Vehicles",
            Color.rgb(46, 204, 113),
            Color.rgb(39, 174, 96)
        );
        browseButton.setOnAction(e -> {
            showAlert(
                Alert.AlertType.INFORMATION,
                "Browse Vehicles",
                "Vehicle Catalog Coming Soon",
                "The vehicle browsing feature will be implemented here.\n\n" +
                "You will be able to:\n" +
                "• View all available vehicles\n" +
                "• Filter by type, price, and availability\n" +
                "• Create booking requests\n" +
                "• View vehicle details"
            );
        });
        
        // Placeholder button for my bookings
        Button bookingsButton = createStyledButton(
            "My Bookings",
            Color.rgb(52, 152, 219),
            Color.rgb(41, 128, 185)
        );
        bookingsButton.setOnAction(e -> {
            showAlert(
                Alert.AlertType.INFORMATION,
                "My Bookings",
                "Bookings Management Coming Soon",
                "Your booking management panel will be implemented here.\n\n" +
                "Features will include:\n" +
                "• View pending bookings\n" +
                "• View approved bookings\n" +
                "• View booking history\n" +
                "• Track booking status"
            );
        });
        
        // Logout button
        Button logoutButton = createStyledButton(
            "Logout",
            Color.rgb(231, 76, 60),
            Color.rgb(192, 57, 43)
        );
        logoutButton.setOnAction(e -> handleLogout());
        
        buttonBox.getChildren().addAll(browseButton, bookingsButton, logoutButton);
        
        // === ASSEMBLE VIEW ===
        container.getChildren().addAll(headerBox, contentBox, buttonBox);
        
        return container;
    }
    
    // ============================================================
    // UTILITY METHODS
    // ============================================================
    
    /**
     * Creates a styled button with hover effects using pure Java API.
     * 
     * @param text Button text
     * @param normalColor Background color in normal state
     * @param hoverColor Background color on hover
     * @return Configured Button object
     */
    private static Button createStyledButton(Color normalColor, Color hoverColor, String text) {
        Button button = new Button(text);
        button.setPrefWidth(180);
        button.setPrefHeight(40);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        
        // Set normal background
        BackgroundFill normalBg = new BackgroundFill(
            normalColor,
            new CornerRadii(5),
            Insets.EMPTY
        );
        button.setBackground(new Background(normalBg));
        button.setBorder(Border.EMPTY);
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            BackgroundFill hoverBg = new BackgroundFill(
                hoverColor,
                new CornerRadii(5),
                Insets.EMPTY
            );
            button.setBackground(new Background(hoverBg));
        });
        
        button.setOnMouseExited(e -> {
            button.setBackground(new Background(normalBg));
        });
        
        return button;
    }
    
    /**
     * Overloaded method for createStyledButton with text as first parameter.
     * 
     * @param text Button text
     * @param normalColor Background color in normal state
     * @param hoverColor Background color on hover
     * @return Configured Button object
     */
    private static Button createStyledButton(String text, Color normalColor, Color hoverColor) {
        return createStyledButton(normalColor, hoverColor, text);
    }
    
    /**
     * Handles the logout action.
     * Shows confirmation dialog and returns to login view if confirmed.
     */
    private static void handleLogout() {
        // Create confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Confirm Logout");
        confirm.setContentText("Are you sure you want to logout?");
        
        // Wait for user response
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed logout
                showLoginView();
            }
        });
    }
    
    /**
     * Shows an alert dialog with the specified parameters.
     * 
     * @param type Alert type (INFO, WARNING, ERROR, etc.)
     * @param title Dialog title
     * @param header Dialog header text
     * @param content Dialog content text
     */
    private static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}