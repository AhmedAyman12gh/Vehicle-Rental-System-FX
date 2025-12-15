package com.vehicle_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * LoginView is a custom VBox component that provides a login interface
 * for the Vehicle Rental System. It allows users to authenticate as either
 * an Admin or a Customer by entering their credentials.
 * 
 * <p>This view includes:</p>
 * <ul>
 *   <li>Input fields for name and email</li>
 *   <li>A checkbox to select Admin or Customer role</li>
 *   <li>Validation to ensure required fields are filled</li>
 *   <li>Navigation to the appropriate view based on user role</li>
 * </ul>
 * 
 * <p>This implementation uses pure Java API without CSS styling.</p>
 * 
 * @author Vehicle Rental System
 * @version 1.0
 */
public class LoginView extends VBox {
    
    // ============================================================
    // UI COMPONENTS - Form elements for user input
    // ============================================================
    
    /** Input field for user's full name */
    private TextField nameField;
    
    /** Input field for user's email address */
    private TextField emailField;
    
    /** Checkbox to toggle between Admin and Customer roles */
    private CheckBox adminCheckBox;
    
    /** Button to submit login credentials */
    private Button loginButton;
    
    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    
    /**
     * Constructs a new LoginView with all necessary UI components.
     * Initializes the layout, styling, and event handlers for the login form.
     * 
     * <p>The constructor performs the following:</p>
     * <ol>
     *   <li>Sets up the VBox container with alignment and spacing</li>
     *   <li>Creates and styles the title label</li>
     *   <li>Initializes input fields with prompts</li>
     *   <li>Adds the admin role checkbox</li>
     *   <li>Configures the login button with click handler</li>
     * </ol>
     */
    public LoginView() {
        // Configure VBox layout properties
        this.setAlignment(Pos.CENTER);      // Center all children horizontally and vertically
        this.setSpacing(15);                // 15px space between each child element
        this.setPadding(new Insets(40));    // 40px padding on all sides
        
        // Set background color for the login view using Java API
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.rgb(236, 240, 241),       // Light gray background (#ecf0f1)
            CornerRadii.EMPTY,              // No rounded corners
            Insets.EMPTY                    // No insets
        );
        this.setBackground(new Background(backgroundFill));
        
        // Initialize and configure all UI components
        initializeTitle();
        initializeInputFields();
        initializeCheckBox();
        initializeLoginButton();
    }
    
    // ============================================================
    // UI INITIALIZATION METHODS
    // ============================================================
    
    /**
     * Creates and styles the title label for the login view.
     * The title displays "Vehicle Rental System" with prominent styling.
     * Uses pure Java API for all styling properties.
     */
    private void initializeTitle() {
        Label titleLabel = new Label("🚗 Vehicle Rental System");
        
        // Set font properties using Java API
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        // Set text color using Java API
        titleLabel.setTextFill(Color.rgb(44, 62, 80)); // Dark blue color (#2c3e50)
        
        // Set padding below the title
        titleLabel.setPadding(new Insets(0, 0, 20, 0));
        
        // Add title to the VBox
        this.getChildren().add(titleLabel);
    }
    
    /**
     * Creates and configures the input fields for name and email.
     * Includes labels and prompt text for user guidance.
     * Uses pure Java API for all styling properties.
     */
    private void initializeInputFields() {
        // Create a container for input fields with better organization
        VBox inputContainer = new VBox(10);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.setMaxWidth(350);    // Limit width for better appearance
        
        // === NAME FIELD ===
        Label nameLabel = new Label("Full Name:");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setPrefWidth(350);
        nameField.setFont(Font.font("System", 13));
        
        // Set padding for the text field
        nameField.setPadding(new Insets(10));
        
        // Set border for text field
        BorderStroke borderStroke = new BorderStroke(
            Color.rgb(189, 195, 199),       // Light gray border
            BorderStrokeStyle.SOLID,
            new CornerRadii(3),             // Slightly rounded corners
            new BorderWidths(1)             // 1px border width
        );
        nameField.setBorder(new Border(borderStroke));
        
        // === EMAIL FIELD ===
        Label emailLabel = new Label("Email Address:");
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefWidth(350);
        emailField.setFont(Font.font("System", 13));
        
        // Set padding for the text field
        emailField.setPadding(new Insets(10));
        
        // Set border for email field (same as name field)
        emailField.setBorder(new Border(borderStroke));
        
        // Add all input components to the container
        inputContainer.getChildren().addAll(
            nameLabel,
            nameField,
            emailLabel,
            emailField
        );
        
        // Add the input container to the main VBox
        this.getChildren().add(inputContainer);
    }
    
    /**
     * Creates and configures the checkbox for admin role selection.
     * When checked, the user will be logged in as an Admin.
     * When unchecked, the user will be logged in as a Customer.
     * Uses pure Java API for all styling properties.
     */
    private void initializeCheckBox() {
        adminCheckBox = new CheckBox("Login as Admin");
        
        // Set font properties using Java API
        adminCheckBox.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Set text color to red to indicate special role
        adminCheckBox.setTextFill(Color.rgb(231, 76, 60)); // Red color (#e74c3c)
        
        // Add spacing above the checkbox
        VBox.setMargin(adminCheckBox, new Insets(10, 0, 0, 0));
        
        this.getChildren().add(adminCheckBox);
    }
    
    /**
     * Creates and configures the login button with its event handler.
     * The button validates input and navigates to the appropriate view.
     * Uses pure Java API for all styling properties.
     */
    private void initializeLoginButton() {
        loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(40);
        
        // Set font properties
        loginButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Set text color to white
        loginButton.setTextFill(Color.WHITE);
        
        // Set background color to blue
        BackgroundFill buttonBackground = new BackgroundFill(
            Color.rgb(52, 152, 219),        // Blue background (#3498db)
            new CornerRadii(5),             // Rounded corners
            Insets.EMPTY
        );
        loginButton.setBackground(new Background(buttonBackground));
        
        // Remove default button border
        loginButton.setBorder(Border.EMPTY);
        
        // Add hover effect using mouse events
        loginButton.setOnMouseEntered(e -> {
            // Darker blue on hover
            BackgroundFill hoverBackground = new BackgroundFill(
                Color.rgb(41, 128, 185),    // Darker blue (#2980b9)
                new CornerRadii(5),
                Insets.EMPTY
            );
            loginButton.setBackground(new Background(hoverBackground));
        });
        
        loginButton.setOnMouseExited(e -> {
            // Return to original blue
            BackgroundFill normalBackground = new BackgroundFill(
                Color.rgb(52, 152, 219),
                new CornerRadii(5),
                Insets.EMPTY
            );
            loginButton.setBackground(new Background(normalBackground));
        });
        
        // Attach click event handler
        loginButton.setOnAction(event -> handleLogin());
        
        // Add spacing above the button
        VBox.setMargin(loginButton, new Insets(20, 0, 0, 0));
        
        this.getChildren().add(loginButton);
    }
    
    // ============================================================
    // EVENT HANDLERS
    // ============================================================
    
    /**
     * Handles the login button click event.
     * Performs the following operations:
     * <ol>
     *   <li>Validates that name and email fields are not empty</li>
     *   <li>Determines user role based on checkbox state</li>
     *   <li>Creates appropriate user object (Admin or Customer)</li>
     *   <li>Navigates to the corresponding view</li>
     * </ol>
     * 
     * <p>If validation fails, displays an error alert to the user.</p>
     */
    // private void handleLogin() {
    //     // Step 1: Retrieve and trim input values
    //     String name = nameField.getText().trim();
    //     String email = emailField.getText().trim();
        
    //     // Step 2: Validate inputs - check for empty fields
    //     if (name.isEmpty() || email.isEmpty()) {
    //         showValidationError();
    //         return; // Exit method if validation fails
    //     }
        
    //     // Step 3: Determine user role based on checkbox state
    //     boolean isAdmin = adminCheckBox.isSelected();
        
    //     // Step 4: Create user object and navigate to appropriate view
    //     if (isAdmin) {
    //         // Create Admin user and show admin view
    //         Admin admin = new Admin(name, email);
    //         Main.showAdminView(admin);
    //     } else {
    //         // Create Customer user and show customer view
    //         Customer customer = new Customer(name, email);
    //         Main.showCustomerView(customer);
    //     }
    // }
    


    
    /**
     * Displays a validation error alert when required fields are empty.
     * The alert provides clear feedback to the user about what went wrong.
     */
    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Missing Required Information");
        alert.setContentText(
            "Please fill in all required fields:\n" +
            "• Full Name\n" +
            "• Email Address"
        );
        
        // Show alert and wait for user to acknowledge
        alert.showAndWait();
    }
    
    // ============================================================
    // PUBLIC UTILITY METHODS
    // ============================================================
    
    /**
     * Clears all input fields and resets the form to its initial state.
     * This method can be called externally if needed to reset the login form.
     */
    public void clearFields() {
        nameField.clear();
        emailField.clear();
        adminCheckBox.setSelected(false);
    }
    
    /**
     * Sets the initial focus to the name field.
     * This provides better user experience by allowing immediate typing.
     * Should be called after the scene is shown.
     */
    public void requestFocusOnNameField() {
        nameField.requestFocus();
    }

    private void handleLogin() {
        // Step 1: Retrieve and trim input values
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        
        // Step 2: Validate inputs
        if (name.isEmpty() || email.isEmpty()) {
            showValidationError();
            return;
        }
        
        boolean isAdmin = adminCheckBox.isSelected();
        
        if (isAdmin) {
            // Check if Admin exists in DataManager, otherwise create new
            Admin admin = null;
            for (Admin a : DataManager.getAdminList()) {
                if (a.getEmail().equalsIgnoreCase(email)) {
                    admin = a;
                    break;
                }
            }
            
            if (admin == null) {
                admin = new Admin(name, email);
                DataManager.getAdminList().add(admin);
            }
            
            Main.showAdminView(admin);
            
        } else {
            // Check if Customer exists in DataManager
            Customer customer = null;
            for (Customer c : DataManager.getCustomerList()) {
                if (c.getEmail().equalsIgnoreCase(email)) {
                    customer = c; // Found existing customer!
                    break;
                }
            }
            
            // If not found, create new and add to DataManager
            if (customer == null) {
                customer = new Customer(name, email);
                DataManager.getCustomerList().add(customer);
            }
            
            Main.showCustomerView(customer);
        }
 }
}