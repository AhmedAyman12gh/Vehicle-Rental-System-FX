package com.vehicle_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

/**
 * Main JavaFX Application for Vehicle Rental Management System
 * This application provides a GUI for managing vehicle rentals, bookings, and payments
 * Supports two user roles: Admin and Customer with different permissions
 */
public class Main extends Application {
    
    // ============================================================
    // DATA STORES - Observable lists for UI binding
    // ============================================================
    
    /** List of all vehicles in the system */
    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    
    /** List of all bookings (pending and approved) */
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    
    /** List of all completed payments */
    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    
    // ============================================================
    // USER MANAGEMENT
    // ============================================================
    
    /** Currently logged-in user */
    private User currentUser;
    
    /** Admin user instance */
    private Admin admin;
    
    /** Customer user instance */
    private Customer customer;
    
    // ============================================================
    // UI COMPONENTS - Main interface elements
    // ============================================================
    
    /** Main tab container for different views */
    private TabPane mainTabPane;
    
    /** Table displaying all vehicles */
    private TableView<Vehicle> vehicleTable;
    
    /** Table displaying all bookings */
    private TableView<Booking> bookingTable;
    
    /** Table displaying payment history */
    private TableView<Payment> paymentTable;
    
    /** Label showing current user info in header */
    private Label userLabel;
    
    /** Button to switch between Admin and Customer */
    private Button switchUserBtn;
    
    // ============================================================
    // APPLICATION ENTRY POINT
    // ============================================================
    
    /**
     * JavaFX start method - called when application launches
     * Initializes users, sample data, and UI components
     * @param primaryStage The main window of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize users using User class hierarchy
        admin = new Admin("John Admin", "admin@rental.com");
        customer = new Customer("Alice Customer", "alice@gmail.com");
        currentUser = admin; // Start session as admin
        
        // Load sample data for demonstration
        initializeSampleData();
        
        // Build main layout structure
        BorderPane root = new BorderPane();
        root.setTop(createHeader());        // Top: Header with user info
        root.setCenter(createMainContent()); // Center: Tabbed content area
        
        // Create and configure the scene
        Scene scene = new Scene(root, 1200, 700);
        
        primaryStage.setTitle("Vehicle Rental Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // ============================================================
    // HEADER SECTION - Top bar with branding and user info
    // ============================================================
    
    /**
     * Creates the top header bar with title, user info, and switch user button
     * @return HBox containing header elements
     */
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #2c3e50;"); // Dark blue background
        
        // Application title with icon
        Label title = new Label("🚗 Vehicle Rental System");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Spacer to push user info to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Display current logged-in user
        userLabel = new Label("Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        userLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        // Button to toggle between Admin and Customer
        switchUserBtn = new Button("Switch User");
        switchUserBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        switchUserBtn.setOnAction(e -> switchUser());
        
        header.getChildren().addAll(title, spacer, userLabel, switchUserBtn);
        return header;
    }
    
    // ============================================================
    // MAIN CONTENT AREA - Tabbed interface
    // ============================================================
    
    /**
     * Creates the main tabbed interface with all management views
     * @return TabPane containing all tabs
     */
    private TabPane createMainContent() {
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Create all tabs
        Tab vehiclesTab = new Tab("Vehicles", createVehiclesView());
        Tab bookingsTab = new Tab("Bookings", createBookingsView());
        Tab paymentsTab = new Tab("Payments", createPaymentsView());
        Tab addVehicleTab = new Tab("Add Vehicle", createAddVehicleView());
        
        mainTabPane.getTabs().addAll(vehiclesTab, bookingsTab, paymentsTab, addVehicleTab);
        return mainTabPane;
    }
    
    // ============================================================
    // VEHICLES VIEW - Browse and manage vehicles
    // ============================================================
    
    /**
     * Creates the vehicles management view with table and action buttons
     * Displays all vehicles with their details and availability
     * @return VBox containing the complete vehicles view
     */
    private VBox createVehiclesView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        // Section heading
        Label heading = new Label("Available Vehicles");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Create and configure vehicle table
        vehicleTable = new TableView<>();
        vehicleTable.setItems(vehicleList); // Bind to observable list
        
        // Define table columns
        
        // Column 1: Vehicle ID
        TableColumn<Vehicle, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleId()));
        idCol.setPrefWidth(80);
        
        // Column 2: Vehicle Type (Car/Van/Bike)
        TableColumn<Vehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> {
            Vehicle v = data.getValue();
            String type = v.getClass().getSimpleName(); // Get class name
            return new SimpleStringProperty(type);
        });
        typeCol.setPrefWidth(100);
        
        // Column 3: Brand
        TableColumn<Vehicle, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand()));
        brandCol.setPrefWidth(120);
        
        // Column 4: Model
        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        modelCol.setPrefWidth(150);
        
        // Column 5: Year
        TableColumn<Vehicle, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getYear())));
        yearCol.setPrefWidth(80);
        
        // Column 6: Price per day
        TableColumn<Vehicle, String> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty("$" + String.format("%.2f", data.getValue().getPricePerDay())));
        priceCol.setPrefWidth(100);
        
        // Column 7: Available quantity
        TableColumn<Vehicle, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        qtyCol.setPrefWidth(80);
        
        // Column 8: Availability status
        TableColumn<Vehicle, String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Yes" : "✗ No"));
        availCol.setPrefWidth(100);
        
        // Column 9: Subtype (Sedan, SUV, Minivan, etc.)
        TableColumn<Vehicle, String> subtypeCol = new TableColumn<>("Subtype");
        subtypeCol.setCellValueFactory(data -> {
            Vehicle v = data.getValue();
            String subtype = "";
            // Get specific type based on vehicle class
            if (v instanceof Car) {
                subtype = ((Car) v).getCarType();
            } else if (v instanceof Van) {
                subtype = ((Van) v).getVanType();
            } else if (v instanceof Bike) {
                subtype = ((Bike) v).getBikeType();
            }
            return new SimpleStringProperty(subtype);
        });
        subtypeCol.setPrefWidth(120);
        
        // Add all columns to table
        vehicleTable.getColumns().addAll(idCol, typeCol, brandCol, modelCol, yearCol, priceCol, qtyCol, availCol, subtypeCol);
        
        // Action buttons for vehicle management
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        // Button 1: Create new booking for selected vehicle
        Button rentBtn = new Button("Create Booking");
        rentBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        rentBtn.setOnAction(e -> showCreateBookingDialog(vehicleTable.getSelectionModel().getSelectedItem()));
        
        // Button 2: Add quantity (Admin only)
        Button addQtyBtn = new Button("Add Quantity (Admin)");
        addQtyBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        addQtyBtn.setOnAction(e -> showAddQuantityDialog(vehicleTable.getSelectionModel().getSelectedItem()));
        addQtyBtn.setDisable(!currentUser.hasRole(User.UserRole.ADMIN)); // Disable for non-admin
        
        // Button 3: Refresh table
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> vehicleTable.refresh());
        
        buttonBox.getChildren().addAll(rentBtn, addQtyBtn, refreshBtn);
        
        // Assemble the view
        container.getChildren().addAll(heading, vehicleTable, buttonBox);
        VBox.setVgrow(vehicleTable, Priority.ALWAYS); // Make table fill available space
        return container;
    }
    
    // ============================================================
    // BOOKINGS VIEW - Manage rental bookings
    // ============================================================
    
    /**
     * Creates the bookings management view
     * Displays all bookings with their status and allows admin approval
     * @return VBox containing the complete bookings view
     */
    private VBox createBookingsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Bookings Management");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Create and configure bookings table
        bookingTable = new TableView<>();
        bookingTable.setItems(bookingList);
        
        // Define columns
        
        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));
        idCol.setPrefWidth(100);
        
        TableColumn<Booking, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer().getName()));
        customerCol.setPrefWidth(150);
        
        TableColumn<Booking, String> vehicleCol = new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRentedItem().getDescription()));
        vehicleCol.setPrefWidth(250);
        
        TableColumn<Booking, String> rentalCol = new TableColumn<>("Rental Date");
        rentalCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRentalDate().toString()));
        rentalCol.setPrefWidth(110);
        
        TableColumn<Booking, String> returnCol = new TableColumn<>("Return Date");
        returnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReturnDate().toString()));
        returnCol.setPrefWidth(110);
        
        TableColumn<Booking, String> costCol = new TableColumn<>("Total Cost");
        costCol.setCellValueFactory(data -> new SimpleStringProperty("$" + String.format("%.2f", data.getValue().getTotalCost())));
        costCol.setPrefWidth(100);
        
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            Booking.BookingStatus status = data.getValue().getStatus();
            return new SimpleStringProperty(status.toString());
        });
        statusCol.setPrefWidth(100);
        
        TableColumn<Booking, String> paidCol = new TableColumn<>("Paid");
        paidCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isPaid() ? "✓ Yes" : "✗ No"));
        paidCol.setPrefWidth(80);
        
        bookingTable.getColumns().addAll(idCol, customerCol, vehicleCol, rentalCol, returnCol, costCol, statusCol, paidCol);
        
        // Action buttons
        HBox buttonBox = new HBox(10);
        
        // Approve booking (Admin only)
        Button approveBtn = new Button("Approve Booking (Admin)");
        approveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        approveBtn.setOnAction(e -> approveBooking(bookingTable.getSelectionModel().getSelectedItem()));
        approveBtn.setDisable(!currentUser.hasRole(User.UserRole.ADMIN));
        
        // View booking details
        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        detailsBtn.setOnAction(e -> showBookingDetails(bookingTable.getSelectionModel().getSelectedItem()));
        
        // Refresh table
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> bookingTable.refresh());
        
        buttonBox.getChildren().addAll(approveBtn, detailsBtn, refreshBtn);
        
        container.getChildren().addAll(heading, bookingTable, buttonBox);
        VBox.setVgrow(bookingTable, Priority.ALWAYS);
        return container;
    }
    
    // ============================================================
    // PAYMENTS VIEW - View payment history
    // ============================================================
    
    /**
     * Creates the payments history view
     * Displays all completed payments with summary
     * @return VBox containing the complete payments view
     */
    private VBox createPaymentsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Payment History");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Create and configure payments table
        paymentTable = new TableView<>();
        paymentTable.setItems(paymentList);
        
        // Define columns
        
        TableColumn<Payment, String> idCol = new TableColumn<>("Payment ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentId()));
        idCol.setPrefWidth(120);
        
        TableColumn<Payment, String> bookingCol = new TableColumn<>("Booking ID");
        bookingCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));
        bookingCol.setPrefWidth(120);
        
        TableColumn<Payment, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer().getName()));
        customerCol.setPrefWidth(200);
        
        TableColumn<Payment, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> new SimpleStringProperty("$" + String.format("%.2f", data.getValue().getAmount())));
        amountCol.setPrefWidth(120);
        
        TableColumn<Payment, String> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentDate().toString()));
        dateCol.setPrefWidth(150);
        
        paymentTable.getColumns().addAll(idCol, bookingCol, customerCol, amountCol, dateCol);
        
        // Summary label showing total revenue
        Label totalLabel = new Label();
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        updatePaymentSummary(totalLabel);
        
        HBox summaryBox = new HBox(10);
        summaryBox.setAlignment(Pos.CENTER_RIGHT);
        summaryBox.getChildren().add(totalLabel);
        
        container.getChildren().addAll(heading, paymentTable, summaryBox);
        VBox.setVgrow(paymentTable, Priority.ALWAYS);
        return container;
    }
    
    /**
     * Calculates and updates the total revenue from all payments
     * @param label Label to display the total
     */
    private void updatePaymentSummary(Label label) {
        double total = 0;
        for (Payment p : paymentList) {
            total += p.getAmount();
        }
        label.setText("Total Revenue: $" + String.format("%.2f", total));
    }
    
    // ============================================================
    // ADD VEHICLE VIEW - Form to add new vehicles (Admin only)
    // ============================================================
    
    /**
     * Creates the add vehicle form view
     * Only accessible to Admin users
     * @return VBox containing the complete add vehicle form
     */
    private VBox createAddVehicleView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Add New Vehicle (Admin Only)");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Check if user is Admin
        if (!currentUser.hasRole(User.UserRole.ADMIN)) {
            Label warning = new Label("⚠ Only administrators can add vehicles to the system.");
            warning.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px;");
            container.getChildren().addAll(heading, warning);
            return container;
        }
        
        // Create form layout
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        
        // Form field 1: Vehicle Type dropdown
        Label typeLabel = new Label("Vehicle Type:");
        typeLabel.setStyle("-fx-font-weight: bold;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Car", "Van", "Bike");
        typeCombo.setValue("Car");
        typeCombo.setPrefWidth(200);
        
        // Form field 2: Vehicle ID
        Label idLabel = new Label("Vehicle ID:");
        idLabel.setStyle("-fx-font-weight: bold;");
        TextField idField = new TextField();
        idField.setPromptText("e.g., C001, V001, B001");
        idField.setPrefWidth(200);
        
        // Form field 3: Brand
        Label brandLabel = new Label("Brand:");
        brandLabel.setStyle("-fx-font-weight: bold;");
        TextField brandField = new TextField();
        brandField.setPromptText("e.g., Toyota, Honda");
        brandField.setPrefWidth(200);
        
        // Form field 4: Model
        Label modelLabel = new Label("Model:");
        modelLabel.setStyle("-fx-font-weight: bold;");
        TextField modelField = new TextField();
        modelField.setPromptText("e.g., Camry, Civic");
        modelField.setPrefWidth(200);
        
        // Form field 5: Year
        Label yearLabel = new Label("Year:");
        yearLabel.setStyle("-fx-font-weight: bold;");
        TextField yearField = new TextField();
        yearField.setPromptText("e.g., 2023");
        yearField.setPrefWidth(200);
        
        // Form field 6: Price per day
        Label priceLabel = new Label("Price/Day ($):");
        priceLabel.setStyle("-fx-font-weight: bold;");
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 60.00");
        priceField.setPrefWidth(200);
        
        // Form field 7: Quantity
        Label qtyLabel = new Label("Quantity:");
        qtyLabel.setStyle("-fx-font-weight: bold;");
        TextField qtyField = new TextField();
        qtyField.setPromptText("e.g., 5");
        qtyField.setPrefWidth(200);
        
        // Form field 8: Subtype (Car type, Van type, or Bike type)
        Label subtypeLabel = new Label("Subtype:");
        subtypeLabel.setStyle("-fx-font-weight: bold;");
        TextField subtypeField = new TextField();
        subtypeField.setPromptText("e.g., Sedan, SUV, Sport");
        subtypeField.setPrefWidth(200);
        
        // Add all form fields to grid
        form.add(typeLabel, 0, 0);
        form.add(typeCombo, 1, 0);
        form.add(idLabel, 0, 1);
        form.add(idField, 1, 1);
        form.add(brandLabel, 0, 2);
        form.add(brandField, 1, 2);
        form.add(modelLabel, 0, 3);
        form.add(modelField, 1, 3);
        form.add(yearLabel, 0, 4);
        form.add(yearField, 1, 4);
        form.add(priceLabel, 0, 5);
        form.add(priceField, 1, 5);
        form.add(qtyLabel, 0, 6);
        form.add(qtyField, 1, 6);
        form.add(subtypeLabel, 0, 7);
        form.add(subtypeField, 1, 7);
        
        // Add Vehicle button
        Button addBtn = new Button("Add Vehicle to System");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        addBtn.setOnAction(e -> {
            try {
                // Collect values from form
                String type = typeCombo.getValue();
                String id = idField.getText().trim();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int qty = Integer.parseInt(qtyField.getText().trim());
                String subtype = subtypeField.getText().trim();
                
                // Create appropriate vehicle object based on type
                Vehicle vehicle = null;
                switch(type) {
                    case "Car":
                        vehicle = new Car(id, brand, model, year, price, qty, subtype);
                        break;
                    case "Van":
                        vehicle = new Van(id, brand, model, year, price, qty, subtype);
                        break;
                    case "Bike":
                        vehicle = new Bike(id, brand, model, year, price, qty, subtype);
                        break;
                }
                
                if (vehicle != null) {
                    // Use Admin's addNewVehicle method
                    admin.addNewVehicle(vehicle);
                    vehicleList.add(vehicle); // Add to observable list
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Vehicle added successfully!\n\n" + vehicle.getVehicleInfo());
                    
                    // Clear all form fields
                    idField.clear();
                    brandField.clear();
                    modelField.clear();
                    yearField.clear();
                    priceField.clear();
                    qtyField.clear();
                    subtypeField.clear();
                    typeCombo.setValue("Car");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", 
                    "Please enter valid numbers for Year, Price, and Quantity.");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Failed to add vehicle:\n" + ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "An unexpected error occurred:\n" + ex.getMessage());
            }
        });
        
        // Clear Form button
        Button clearBtn = new Button("Clear Form");
        clearBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        clearBtn.setOnAction(e -> {
            idField.clear();
            brandField.clear();
            modelField.clear();
            yearField.clear();
            priceField.clear();
            qtyField.clear();
            subtypeField.clear();
            typeCombo.setValue("Car");
        });
        
        HBox buttonBox = new HBox(10, addBtn, clearBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        container.getChildren().addAll(heading, form, buttonBox);
        return container;
    }
    
    // ============================================================
    // DIALOG METHODS - Popup windows for user interactions
    // ============================================================
    
    /**
     * Shows dialog to create a new booking for selected vehicle
     * Allows user to select rental and return dates
     * Calculates estimated cost dynamically
     * @param vehicle The vehicle to book
     */
    private void showCreateBookingDialog(Vehicle vehicle) {
        // Validation: Check if vehicle is selected
        if (vehicle == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a vehicle first.");
            return;
        }
        
        // Check if vehicle is available
        if (!vehicle.isAvailable()) {
            showAlert(Alert.AlertType.ERROR, "Not Available", "This vehicle is not currently available.");
            return;
        }
        
        // Create dialog
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Create Booking");
        dialog.setHeaderText("Create a new booking for:\n" + vehicle.getDescription());
        
        // Add buttons
        ButtonType createBtn = new ButtonType("Create Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtn, ButtonType.CANCEL);
        
        // Create form layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Rental date picker (defaults to today)
        Label rentalLabel = new Label("Rental Date:");
        DatePicker rentalDate = new DatePicker(LocalDate.now());
        
        // Return date picker (defaults to 7 days from now)
        Label returnLabel = new Label("Return Date:");
        DatePicker returnDate = new DatePicker(LocalDate.now().plusDays(7));
        
        // Display price per day
        Label priceLabel = new Label("Price per Day:");
        Label priceValue = new Label("$" + String.format("%.2f", vehicle.getPricePerDay()));
        priceValue.setStyle("-fx-font-weight: bold;");
        
        // Display estimated total cost
        Label estimateLabel = new Label("Estimated Cost:");
        Label estimateValue = new Label();
        estimateValue.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        
        // Calculate and update estimate when dates change
        Runnable updateEstimate = () -> {
            try {
                LocalDate rental = rentalDate.getValue();
                LocalDate returnD = returnDate.getValue();
                if (rental != null && returnD != null && !returnD.isBefore(rental)) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(rental, returnD);
                    double cost = vehicle.getRentalPrice((int) days);
                    estimateValue.setText("$" + String.format("%.2f", cost) + " (" + days + " days)");
                } else {
                    estimateValue.setText("Invalid dates");
                }
            } catch (Exception e) {
                estimateValue.setText("Error calculating");
            }
        };
        
        // Attach listeners to date pickers
        rentalDate.setOnAction(e -> updateEstimate.run());
        returnDate.setOnAction(e -> updateEstimate.run());
        updateEstimate.run(); // Initial calculation
        
        // Add fields to grid
        grid.add(rentalLabel, 0, 0);
        grid.add(rentalDate, 1, 0);
        grid.add(returnLabel, 0, 1);
        grid.add(returnDate, 1, 1);
        grid.add(priceLabel, 0, 2);
        grid.add(priceValue, 1, 2);
        grid.add(estimateLabel, 0, 3);
        grid.add(estimateValue, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Handle dialog result
        dialog.setResultConverter(btn -> {
            if (btn == createBtn) {
                try {
                    // Use Customer's requestVehicle method
                    Booking booking = customer.requestVehicle(vehicle, rentalDate.getValue(), returnDate.getValue());
                    bookingList.add(booking); // Add to observable list
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Booking created successfully!\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Status: " + booking.getStatus() + "\n" +
                        "Total Cost: $" + String.format("%.2f", booking.getTotalCost()) + "\n\n" +
                        "Waiting for admin approval...");
                    
                    vehicleTable.refresh();
                    return booking;
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", 
                        "Failed to create booking:\n" + e.getMessage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", 
                        "Failed to create booking:\n" + e.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    /**
     * Shows dialog to add quantity to a vehicle (Admin only)
     * @param vehicle The vehicle to update
     */
    private void showAddQuantityDialog(Vehicle vehicle) {
        // Validation checks
        if (vehicle == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a vehicle first.");
            return;
        }
        
        if (!currentUser.hasRole(User.UserRole.ADMIN)) {
            showAlert(Alert.AlertType.ERROR, "Unauthorized", "Only admins can add quantity.");
            return;
        }
        
        // Create input dialog
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add Quantity");
        dialog.setHeaderText("Add quantity for:\n" + vehicle.getDescription() + "\n\nCurrent Quantity: " + vehicle.getQuantity());
        dialog.setContentText("Quantity to add:");
        
        // Handle dialog result
        dialog.showAndWait().ifPresent(qty -> {
            try {
                int quantity = Integer.parseInt(qty);
                // Use Admin's addVehicleQuantity method (includes security checks)
                admin.addVehicleQuantity(vehicle, quantity);
                
                vehicleTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Quantity added successfully!\n\n" +
                    "New Quantity: " + vehicle.getQuantity());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number.");
            } catch (SecurityException e) {
                showAlert(Alert.AlertType.ERROR, "Security Error", e.getMessage());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add quantity:\n" + e.getMessage());
            }
        });
    }
    
    /**
     * Approves a pending booking (Admin only)
     * Creates payment record and updates vehicle availability
     * @param booking The booking to approve
     */
    private void approveBooking(Booking booking) {
        // Validation checks
        if (booking == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking first.");
            return;
        }
        
        if (!currentUser.hasRole(User.UserRole.ADMIN)) {
            showAlert(Alert.AlertType.ERROR, "Unauthorized", "Only admins can approve bookings.");
            return;
        }
        
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            showAlert(Alert.AlertType.WARNING, "Invalid Status", 
                "This booking cannot be approved. Current status: " + booking.getStatus());
            return;
        }
        
        // Show confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Approval");
        confirm.setHeaderText("Approve this booking?");
        confirm.setContentText(booking.getDetails());
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Use Admin's approveBooking method (includes all business logic)
                    admin.approveBooking(booking);
                    
                    // Create payment record
                    Payment payment = new Payment(
                        booking.getBookingId(), 
                        booking.getCustomer(), 
                        booking.getTotalCost(), 
                        LocalDate.now()
                    );
                    paymentList.add(payment); // Add to observable list
                    
                    // Refresh all affected tables
                    bookingTable.refresh();
                    vehicleTable.refresh();
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Booking approved successfully!\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Status: " + booking.getStatus() + "\n" +
                        "Payment ID: " + payment.getPaymentId() + "\n" +
                        "Vehicle has been rented.");
                        
                } catch (SecurityException e) {
                    showAlert(Alert.AlertType.ERROR, "Security Error", e.getMessage());
                } catch (IllegalStateException e) {
                    showAlert(Alert.AlertType.ERROR, "State Error", e.getMessage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", 
                        "Failed to approve booking:\n" + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Shows detailed information about a booking
     * @param booking The booking to display
     */
    private void showBookingDetails(Booking booking) {
        if (booking == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking first.");
            return;
        }
        
        // Create details dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Details");
        alert.setHeaderText("Booking Information");
        alert.setContentText(booking.getDetails());
        alert.showAndWait();
    }
    
    // ============================================================
    // USER MANAGEMENT METHODS
    // ============================================================
    
    /**
     * Switches between Admin and Customer user accounts
     * Updates UI elements based on new user role
     */
    private void switchUser() {
        // Toggle between admin and customer
        if (currentUser == admin) {
            currentUser = customer;
        } else {
            currentUser = admin;
        }
        
        // Update header label
        userLabel.setText("Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        
        // Refresh all views to update permissions
        mainTabPane.getTabs().clear();
        Tab vehiclesTab = new Tab("Vehicles", createVehiclesView());
        Tab bookingsTab = new Tab("Bookings", createBookingsView());
        Tab paymentsTab = new Tab("Payments", createPaymentsView());
        Tab addVehicleTab = new Tab("Add Vehicle", createAddVehicleView());
        mainTabPane.getTabs().addAll(vehiclesTab, bookingsTab, paymentsTab, addVehicleTab);
        
        // Show notification
        showAlert(Alert.AlertType.INFORMATION, "User Switched", 
            "Now logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
    }
    
    // ============================================================
    // DATA INITIALIZATION - Sample data for testing
    // ============================================================
    
    /**
     * Initializes sample vehicles, bookings, and payments for demonstration
     * This method populates the system with test data
     */
    private void initializeSampleData() {
        // Add sample cars
        vehicleList.add(new Car("C001", "Toyota", "Camry", 2022, 50.00, 3, "Sedan"));
        vehicleList.add(new Car("C002", "Honda", "Accord", 2023, 55.00, 2, "Sedan"));
        vehicleList.add(new Car("C003", "BMW", "X5", 2023, 120.00, 1, "SUV"));
        vehicleList.add(new Car("C004", "Tesla", "Model 3", 2024, 100.00, 2, "Sedan"));
        
        // Add sample vans
        vehicleList.add(new Van("V001", "Ford", "Transit", 2022, 80.00, 2, "Cargo Van"));
        vehicleList.add(new Van("V002", "Mercedes", "Sprinter", 2023, 95.00, 1, "Passenger Van"));
        vehicleList.add(new Van("V003", "Dodge", "Grand Caravan", 2022, 70.00, 3, "Minivan"));
        
        // Add sample bikes
        vehicleList.add(new Bike("B001", "Harley", "Sportster", 2023, 40.00, 5, "Cruiser"));
        vehicleList.add(new Bike("B002", "Yamaha", "R1", 2024, 60.00, 2, "Sport"));
        vehicleList.add(new Bike("B003", "Honda", "CB500X", 2023, 35.00, 4, "Adventure"));
        
        // Add sample pending bookings
        try {
            Booking booking1 = customer.requestVehicle(
                vehicleList.get(0), 
                LocalDate.now(), 
                LocalDate.now().plusDays(5)
            );
            bookingList.add(booking1);
            
            Booking booking2 = customer.requestVehicle(
                vehicleList.get(3), 
                LocalDate.now().plusDays(2), 
                LocalDate.now().plusDays(10)
            );
            bookingList.add(booking2);
            
        } catch (Exception e) {
            System.err.println("Error creating sample bookings: " + e.getMessage());
        }
    }
    
    // ============================================================
    // UTILITY METHODS
    // ============================================================
    
    /**
     * Helper method to show alert dialogs
     * @param type Type of alert (INFO, WARNING, ERROR)
     * @param title Dialog title
     * @param content Dialog content message
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // ============================================================
    // MAIN METHOD - Application entry point
    // ============================================================
    
    /**
     * Main method - launches the JavaFX application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}