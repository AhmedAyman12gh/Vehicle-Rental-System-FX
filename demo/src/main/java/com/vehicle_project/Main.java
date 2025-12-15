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

public class VehicleRentalApp extends Application {
    
    // Data stores - These will hold your actual class instances
    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    
    // Current user (for demo, we'll use Admin)
    private User currentUser;
    private Admin admin;
    private Customer customer;
    
    // UI Components
    private TabPane mainTabPane;
    private TableView<Vehicle> vehicleTable;
    private TableView<Booking> bookingTable;
    private TableView<Payment> paymentTable;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize users using your User classes
        admin = new Admin("John Admin", "admin@rental.com");
        customer = new Customer("Alice Customer", "alice@gmail.com");
        currentUser = admin; // Start as admin
        
        // Initialize sample data
        initializeSampleData();
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createMainContent());
        
        Scene scene = new Scene(root, 1200, 700);
        
        primaryStage.setTitle("Vehicle Rental Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #2c3e50;");
        
        Label title = new Label("🚗 Vehicle Rental System");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label userLabel = new Label("Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        userLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        Button switchUserBtn = new Button("Switch User");
        switchUserBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        switchUserBtn.setOnAction(e -> switchUser());
        
        header.getChildren().addAll(title, spacer, userLabel, switchUserBtn);
        return header;
    }
    
    private TabPane createMainContent() {
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab vehiclesTab = new Tab("Vehicles", createVehiclesView());
        Tab bookingsTab = new Tab("Bookings", createBookingsView());
        Tab paymentsTab = new Tab("Payments", createPaymentsView());
        Tab addVehicleTab = new Tab("Add Vehicle", createAddVehicleView());
        
        mainTabPane.getTabs().addAll(vehiclesTab, bookingsTab, paymentsTab, addVehicleTab);
        return mainTabPane;
    }
    
    // ============ VEHICLES VIEW ============
    private VBox createVehiclesView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Available Vehicles");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Vehicle table - connected to your Vehicle class
        vehicleTable = new TableView<>();
        vehicleTable.setItems(vehicleList);
        
        TableColumn<Vehicle, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleId()));
        idCol.setPrefWidth(80);
        
        TableColumn<Vehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> {
            Vehicle v = data.getValue();
            String type = v.getClass().getSimpleName(); // Car, Van, or Bike
            return new SimpleStringProperty(type);
        });
        typeCol.setPrefWidth(100);
        
        TableColumn<Vehicle, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand()));
        brandCol.setPrefWidth(120);
        
        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        modelCol.setPrefWidth(150);
        
        TableColumn<Vehicle, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getYear())));
        yearCol.setPrefWidth(80);
        
        TableColumn<Vehicle, String> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty("$" + String.format("%.2f", data.getValue().getPricePerDay())));
        priceCol.setPrefWidth(100);
        
        TableColumn<Vehicle, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        qtyCol.setPrefWidth(80);
        
        TableColumn<Vehicle, String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Yes" : "✗ No"));
        availCol.setPrefWidth(100);
        
        TableColumn<Vehicle, String> subtypeCol = new TableColumn<>("Subtype");
        subtypeCol.setCellValueFactory(data -> {
            Vehicle v = data.getValue();
            String subtype = "";
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
        
        vehicleTable.getColumns().addAll(idCol, typeCol, brandCol, modelCol, yearCol, priceCol, qtyCol, availCol, subtypeCol);
        
        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        Button rentBtn = new Button("Create Booking");
        rentBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        rentBtn.setOnAction(e -> showCreateBookingDialog(vehicleTable.getSelectionModel().getSelectedItem()));
        
        Button addQtyBtn = new Button("Add Quantity (Admin)");
        addQtyBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        addQtyBtn.setOnAction(e -> showAddQuantityDialog(vehicleTable.getSelectionModel().getSelectedItem()));
        addQtyBtn.setDisable(!currentUser.hasRole(User.UserRole.ADMIN));
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> vehicleTable.refresh());
        
        buttonBox.getChildren().addAll(rentBtn, addQtyBtn, refreshBtn);
        
        container.getChildren().addAll(heading, vehicleTable, buttonBox);
        VBox.setVgrow(vehicleTable, Priority.ALWAYS);
        return container;
    }
    
    // ============ BOOKINGS VIEW ============
    private VBox createBookingsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Bookings Management");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Booking table - connected to your Booking class
        bookingTable = new TableView<>();
        bookingTable.setItems(bookingList);
        
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
        
        HBox buttonBox = new HBox(10);
        
        Button approveBtn = new Button("Approve Booking (Admin)");
        approveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        approveBtn.setOnAction(e -> approveBooking(bookingTable.getSelectionModel().getSelectedItem()));
        approveBtn.setDisable(!currentUser.hasRole(User.UserRole.ADMIN));
        
        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        detailsBtn.setOnAction(e -> showBookingDetails(bookingTable.getSelectionModel().getSelectedItem()));
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> bookingTable.refresh());
        
        buttonBox.getChildren().addAll(approveBtn, detailsBtn, refreshBtn);
        
        container.getChildren().addAll(heading, bookingTable, buttonBox);
        VBox.setVgrow(bookingTable, Priority.ALWAYS);
        return container;
    }
    
    // ============ PAYMENTS VIEW ============
    private VBox createPaymentsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Payment History");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Payment table - connected to your Payment class
        paymentTable = new TableView<>();
        paymentTable.setItems(paymentList);
        
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
        
        // Summary
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
    
    private void updatePaymentSummary(Label label) {
        double total = 0;
        for (Payment p : paymentList) {
            total += p.getAmount();
        }
        label.setText("Total Revenue: $" + String.format("%.2f", total));
    }
    
    // ============ ADD VEHICLE VIEW ============
    private VBox createAddVehicleView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label heading = new Label("Add New Vehicle (Admin Only)");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        if (!currentUser.hasRole(User.UserRole.ADMIN)) {
            Label warning = new Label("⚠ Only administrators can add vehicles to the system.");
            warning.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px;");
            container.getChildren().addAll(heading, warning);
            return container;
        }
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        
        // Vehicle Type
        Label typeLabel = new Label("Vehicle Type:");
        typeLabel.setStyle("-fx-font-weight: bold;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Car", "Van", "Bike");
        typeCombo.setValue("Car");
        typeCombo.setPrefWidth(200);
        
        // Vehicle ID
        Label idLabel = new Label("Vehicle ID:");
        idLabel.setStyle("-fx-font-weight: bold;");
        TextField idField = new TextField();
        idField.setPromptText("e.g., C001, V001, B001");
        idField.setPrefWidth(200);
        
        // Brand
        Label brandLabel = new Label("Brand:");
        brandLabel.setStyle("-fx-font-weight: bold;");
        TextField brandField = new TextField();
        brandField.setPromptText("e.g., Toyota, Honda");
        brandField.setPrefWidth(200);
        
        // Model
        Label modelLabel = new Label("Model:");
        modelLabel.setStyle("-fx-font-weight: bold;");
        TextField modelField = new TextField();
        modelField.setPromptText("e.g., Camry, Civic");
        modelField.setPrefWidth(200);
        
        // Year
        Label yearLabel = new Label("Year:");
        yearLabel.setStyle("-fx-font-weight: bold;");
        TextField yearField = new TextField();
        yearField.setPromptText("e.g., 2023");
        yearField.setPrefWidth(200);
        
        // Price per day
        Label priceLabel = new Label("Price/Day ($):");
        priceLabel.setStyle("-fx-font-weight: bold;");
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 60.00");
        priceField.setPrefWidth(200);
        
        // Quantity
        Label qtyLabel = new Label("Quantity:");
        qtyLabel.setStyle("-fx-font-weight: bold;");
        TextField qtyField = new TextField();
        qtyField.setPromptText("e.g., 5");
        qtyField.setPrefWidth(200);
        
        // Subtype (Car/Van/Bike type)
        Label subtypeLabel = new Label("Subtype:");
        subtypeLabel.setStyle("-fx-font-weight: bold;");
        TextField subtypeField = new TextField();
        subtypeField.setPromptText("e.g., Sedan, SUV, Sport");
        subtypeField.setPrefWidth(200);
        
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
        
        Button addBtn = new Button("Add Vehicle to System");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        addBtn.setOnAction(e -> {
            try {
                // Get values from form
                String type = typeCombo.getValue();
                String id = idField.getText().trim();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int qty = Integer.parseInt(qtyField.getText().trim());
                String subtype = subtypeField.getText().trim();
                
                // Create the appropriate vehicle using your classes
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
                    // Use Admin's method to add vehicle
                    admin.addNewVehicle(vehicle);
                    vehicleList.add(vehicle);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Vehicle added successfully!\n\n" + vehicle.getVehicleInfo());
                    
                    // Clear all fields
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
    
    // ============ DIALOGS ============
    private void showCreateBookingDialog(Vehicle vehicle) {
        if (vehicle == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a vehicle first.");
            return;
        }
        
        if (!vehicle.isAvailable()) {
            showAlert(Alert.AlertType.ERROR, "Not Available", "This vehicle is not currently available.");
            return;
        }
        
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Create Booking");
        dialog.setHeaderText("Create a new booking for:\n" + vehicle.getDescription());
        
        ButtonType createBtn = new ButtonType("Create Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtn, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        Label rentalLabel = new Label("Rental Date:");
        DatePicker rentalDate = new DatePicker(LocalDate.now());
        
        Label returnLabel = new Label("Return Date:");
        DatePicker returnDate = new DatePicker(LocalDate.now().plusDays(7));
        
        Label priceLabel = new Label("Price per Day:");
        Label priceValue = new Label("$" + String.format("%.2f", vehicle.getPricePerDay()));
        priceValue.setStyle("-fx-font-weight: bold;");
        
        Label estimateLabel = new Label("Estimated Cost:");
        Label estimateValue = new Label();
        estimateValue.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        
        // Calculate estimate when dates change
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
        
        rentalDate.setOnAction(e -> updateEstimate.run());
        returnDate.setOnAction(e -> updateEstimate.run());
        updateEstimate.run();
        
        grid.add(rentalLabel, 0, 0);
        grid.add(rentalDate, 1, 0);
        grid.add(returnLabel, 0, 1);
        grid.add(returnDate, 1, 1);
        grid.add(priceLabel, 0, 2);
        grid.add(priceValue, 1, 2);
        grid.add(estimateLabel, 0, 3);
        grid.add(estimateValue, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(btn -> {
            if (btn == createBtn) {
                try {
                    // Use Customer's requestVehicle method from your Customer class
                    Booking booking = customer.requestVehicle(vehicle, rentalDate.getValue(), returnDate.getValue());
                    bookingList.add(booking);
                    
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
    
    private void showAddQuantityDialog(Vehicle vehicle) {
        if (vehicle == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a vehicle first.");
            return;
        }
        
        if (!currentUser.hasRole(User.UserRole.ADMIN)) {
            showAlert(Alert.AlertType.ERROR, "Unauthorized", "Only admins can add quantity.");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add Quantity");
        dialog.setHeaderText("Add quantity for:\n" + vehicle.getDescription() + "\n\nCurrent Quantity: " + vehicle.getQuantity());
        dialog.setContentText("Quantity to add:");
        
        dialog.showAndWait().ifPresent(qty -> {
            try {
                int quantity = Integer.parseInt(qty);
                // Use Admin's addVehicleQuantity method
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
    
    private void approveBooking(Booking booking) {
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
        
        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Approval");
        confirm.setHeaderText("Approve this booking?");
        confirm.setContentText(booking.getDetails());
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Use Admin's approveBooking method
                    admin.approveBooking(booking);
                    
                    // Create payment record using your Payment class
                    Payment payment = new Payment(
                        booking.getBookingId(), 
                        booking.getCustomer(), 
                        booking.getTotalCost(), 
                        LocalDate.now()
                    );
                    paymentList.add(payment);
                    
                    // Refresh tables
                    bookingTable.refresh();
                    vehicleTable.refresh();
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Booking approved successfully!\n\n" +