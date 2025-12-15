package com.vehicle_project;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.Optional;

public class AdminView extends BorderPane {
    
    private Admin admin;
    private TableView<Vehicle> vehicleTable;
    private TableView<Booking> bookingTable;
    private TableView<Payment> paymentTable;
    private Label totalRevenueLabel;
    
    public AdminView(Admin admin) {
        this.admin = admin;
        initializeUI();
    }
    
    private void initializeUI() {
        // Top Section - Header
        setTop(createHeader());
        
        // Center Section - TabPane
        setCenter(createTabPane());
        
        // Apply padding
        setPadding(new Insets(10));
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #FF5722; -fx-background-radius: 5;");
        
        // Dashboard label
        Label dashboardLabel = new Label("Admin Dashboard");
        dashboardLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        dashboardLabel.setStyle("-fx-text-fill: white;");
        
        // Spacer
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; " +
                          "-fx-font-weight: bold; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> Main.showLoginView());
        
        header.getChildren().addAll(dashboardLabel, spacer, logoutBtn);
        return header;
    }
    
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Tab 1: Manage Fleet
        Tab fleetTab = new Tab("Manage Fleet");
        fleetTab.setContent(createManageFleetTab());
        
        // Tab 2: Manage Bookings
        Tab bookingsTab = new Tab("Manage Bookings");
        bookingsTab.setContent(createManageBookingsTab());
        
        // Tab 3: Financials
        Tab financialsTab = new Tab("Financials");
        financialsTab.setContent(createFinancialsTab());
        
        tabPane.getTabs().addAll(fleetTab, bookingsTab, financialsTab);
        return tabPane;
    }
    
    private VBox createManageFleetTab() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(15));
        
        // Top Half - Vehicle Table
        Label tableLabel = new Label("Current Fleet");
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        vehicleTable = new TableView<>();
        vehicleTable.setItems(DataManager.getVehicleList());
        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vehicleTable.setPrefHeight(250);
        
        // ID Column
        TableColumn<Vehicle, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        idCol.setPrefWidth(80);
        
        // Brand Column
        TableColumn<Vehicle, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(100);
        
        // Model Column
        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(120);
        
        // Year Column
        TableColumn<Vehicle, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);
        
        // Price Column
        TableColumn<Vehicle, Double> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        priceCol.setPrefWidth(100);
        priceCol.setCellFactory(col -> new TableCell<Vehicle, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        // Quantity Column
        TableColumn<Vehicle, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(80);
        
        // Available Column
        TableColumn<Vehicle, Boolean> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isAvailable())
        );
        availCol.setPrefWidth(80);
        availCol.setCellFactory(col -> new TableCell<Vehicle, Boolean>() {
            @Override
            protected void updateItem(Boolean available, boolean empty) {
                super.updateItem(available, empty);
                if (empty || available == null) {
                    setText(null);
                } else {
                    setText(available ? "Yes" : "No");
                    setStyle(available ? "-fx-text-fill: green; -fx-font-weight: bold;" 
                                      : "-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });
        
        // Action Column
        TableColumn<Vehicle, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            private final Button addQtyBtn = new Button("Add Quantity");
            
            {
                addQtyBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                 "-fx-font-weight: bold; -fx-cursor: hand;");
                addQtyBtn.setOnAction(e -> {
                    Vehicle vehicle = getTableView().getItems().get(getIndex());
                    if (vehicle != null) {
                        showAddQuantityDialog(vehicle);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addQtyBtn);
                }
            }
        });
        
        vehicleTable.getColumns().addAll(idCol, brandCol, modelCol, yearCol, priceCol, 
                                         quantityCol, availCol, actionCol);
        
        // Bottom Half - Add Vehicle Form
        Label formLabel = new Label("Add New Vehicle");
        formLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        GridPane form = createAddVehicleForm();
        
        container.getChildren().addAll(tableLabel, vehicleTable, new Separator(), formLabel, form);
        return container;
    }
    
    private GridPane createAddVehicleForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 5;");
        
        // Row 0: ID and Brand
        Label idLabel = new Label("Vehicle ID:");
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField idField = new TextField();
        idField.setPromptText("e.g., C004");
        
        Label brandLabel = new Label("Brand:");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField brandField = new TextField();
        brandField.setPromptText("e.g., Toyota");
        
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(brandLabel, 2, 0);
        grid.add(brandField, 3, 0);
        
        // Row 1: Model and Year
        Label modelLabel = new Label("Model:");
        modelLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField modelField = new TextField();
        modelField.setPromptText("e.g., Corolla");
        
        Label yearLabel = new Label("Year:");
        yearLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField yearField = new TextField();
        yearField.setPromptText("e.g., 2023");
        
        grid.add(modelLabel, 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(yearLabel, 2, 1);
        grid.add(yearField, 3, 1);
        
        // Row 2: Price and Quantity
        Label priceLabel = new Label("Price/Day:");
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 50.00");
        
        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField quantityField = new TextField();
        quantityField.setPromptText("e.g., 5");
        
        grid.add(priceLabel, 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(quantityLabel, 2, 2);
        grid.add(quantityField, 3, 2);
        
        // Row 3: Type and Subtype
        Label typeLabel = new Label("Type:");
        typeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Car", "Van", "Bike");
        typeCombo.setPromptText("Select Type");
        typeCombo.setPrefWidth(150);
        
        Label subtypeLabel = new Label("Subtype:");
        subtypeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        TextField subtypeField = new TextField();
        subtypeField.setPromptText("e.g., Sedan, Cargo Van, Sport");
        
        grid.add(typeLabel, 0, 3);
        grid.add(typeCombo, 1, 3);
        grid.add(subtypeLabel, 2, 3);
        grid.add(subtypeField, 3, 3);
        
        // Row 4: Add Button
        Button addVehicleBtn = new Button("Add Vehicle to Fleet");
        addVehicleBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                              "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand;");
        addVehicleBtn.setPrefWidth(200);
        
        addVehicleBtn.setOnAction(e -> {
            try {
                // Validate inputs
                String id = idField.getText().trim();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                String yearStr = yearField.getText().trim();
                String priceStr = priceField.getText().trim();
                String quantityStr = quantityField.getText().trim();
                String type = typeCombo.getValue();
                String subtype = subtypeField.getText().trim();
                
                if (id.isEmpty() || brand.isEmpty() || model.isEmpty() || 
                    yearStr.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() ||
                    type == null || subtype.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", 
                             "All fields are required!");
                    return;
                }
                
                int year = Integer.parseInt(yearStr);
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                
                // Create vehicle based on type
                Vehicle newVehicle = null;
                switch (type) {
                    case "Car":
                        newVehicle = new Car(id, brand, model, year, price, quantity, subtype);
                        break;
                    case "Van":
                        newVehicle = new Van(id, brand, model, year, price, quantity, subtype);
                        break;
                    case "Bike":
                        newVehicle = new Bike(id, brand, model, year, price, quantity, subtype);
                        break;
                }
                
                // Add vehicle using admin method
                admin.addNewVehicle(newVehicle);
                
                // Add to data manager
                DataManager.getVehicleList().add(newVehicle);
                
                // Refresh table
                vehicleTable.refresh();
                
                // Clear form
                idField.clear();
                brandField.clear();
                modelField.clear();
                yearField.clear();
                priceField.clear();
                quantityField.clear();
                typeCombo.setValue(null);
                subtypeField.clear();
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         "Vehicle added to fleet successfully!");
                
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", 
                         "Year, Price, and Quantity must be valid numbers!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Failed to add vehicle: " + ex.getMessage());
            }
        });
        
        grid.add(addVehicleBtn, 1, 4, 2, 1);
        
        return grid;
    }
    
    private VBox createManageBookingsTab() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("All Booking Requests");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Create booking table
        bookingTable = new TableView<>();
        bookingTable.setItems(DataManager.getBookingList());
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Booking ID Column
        TableColumn<Booking, String> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingIdCol.setPrefWidth(100);
        
        // Customer Name Column
        TableColumn<Booking, String> customerCol = new TableColumn<>("Customer Name");
        customerCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCustomer().getName()
            )
        );
        customerCol.setPrefWidth(120);
        
        // Vehicle Column
        TableColumn<Booking, String> vehicleCol = new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRentedItem().getDescription()
            )
        );
        vehicleCol.setPrefWidth(200);
        
        // Rental Date Column
        TableColumn<Booking, LocalDate> rentalDateCol = new TableColumn<>("Rental Date");
        rentalDateCol.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        rentalDateCol.setPrefWidth(100);
        
        // Return Date Column
        TableColumn<Booking, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(100);
        
        // Total Cost Column
        TableColumn<Booking, Double> costCol = new TableColumn<>("Total Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        costCol.setPrefWidth(100);
        costCol.setCellFactory(col -> new TableCell<Booking, Double>() {
            @Override
            protected void updateItem(Double cost, boolean empty) {
                super.updateItem(cost, empty);
                if (empty || cost == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", cost));
                }
            }
        });
        
        // Status Column
        TableColumn<Booking, Booking.BookingStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(col -> new TableCell<Booking, Booking.BookingStatus>() {
            @Override
            protected void updateItem(Booking.BookingStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.toString());
                    switch (status) {
                        case PENDING:
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            break;
                        case APPROVED:
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case REJECTED:
                        case CANCELLED:
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        case COMPLETED:
                            setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        // Paid Column
        TableColumn<Booking, Boolean> paidCol = new TableColumn<>("Paid");
        paidCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isPaid())
        );
        paidCol.setPrefWidth(80);
        paidCol.setCellFactory(col -> new TableCell<Booking, Boolean>() {
            @Override
            protected void updateItem(Boolean paid, boolean empty) {
                super.updateItem(paid, empty);
                if (empty || paid == null) {
                    setText(null);
                } else {
                    setText(paid ? "Yes" : "No");
                    setStyle(paid ? "-fx-text-fill: green; -fx-font-weight: bold;" 
                                 : "-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });
        
        // Action Column
        TableColumn<Booking, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<Booking, Void>() {
            private final Button approveBtn = new Button("Approve");
            
            {
                approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                  "-fx-font-weight: bold; -fx-cursor: hand;");
                approveBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    if (booking != null) {
                        approveBooking(booking);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    approveBtn.setDisable(booking.getStatus() != Booking.BookingStatus.PENDING);
                    setGraphic(approveBtn);
                }
            }
        });
        
        bookingTable.getColumns().addAll(bookingIdCol, customerCol, vehicleCol, rentalDateCol, 
                                         returnDateCol, costCol, statusCol, paidCol, actionCol);
        
        container.getChildren().addAll(titleLabel, bookingTable);
        return container;
    }
    
    private VBox createFinancialsTab() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Payment History");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Create payment table
        paymentTable = new TableView<>();
        paymentTable.setItems(DataManager.getPaymentList());
        paymentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Payment ID Column
        TableColumn<Payment, String> paymentIdCol = new TableColumn<>("Payment ID");
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        paymentIdCol.setPrefWidth(100);
        
        // Booking ID Column
        TableColumn<Payment, String> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingIdCol.setPrefWidth(100);
        
        // Customer Column
        TableColumn<Payment, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCustomer().getName()
            )
        );
        customerCol.setPrefWidth(150);
        
        // Amount Column
        TableColumn<Payment, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(120);
        amountCol.setCellFactory(col -> new TableCell<Payment, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                }
            }
        });
        
        // Payment Date Column
        TableColumn<Payment, LocalDate> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        dateCol.setPrefWidth(120);
        
        paymentTable.getColumns().addAll(paymentIdCol, bookingIdCol, customerCol, amountCol, dateCol);
        
        // Total Revenue Section
        HBox summaryBox = new HBox(10);
        summaryBox.setAlignment(Pos.CENTER_RIGHT);
        summaryBox.setPadding(new Insets(15));
        summaryBox.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 5;");
        
        Label summaryLabel = new Label("Total Revenue:");
        summaryLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        totalRevenueLabel = new Label("$0.00");
        totalRevenueLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        totalRevenueLabel.setStyle("-fx-text-fill: #1976D2;");
        
        summaryBox.getChildren().addAll(summaryLabel, totalRevenueLabel);
        
        updatePaymentSummary();
        
        container.getChildren().addAll(titleLabel, paymentTable, summaryBox);
        return container;
    }
    
    private void showAddQuantityDialog(Vehicle vehicle) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add Quantity");
        dialog.setHeaderText("Add quantity to: " + vehicle.getBrand() + " " + vehicle.getModel());
        dialog.setContentText("Enter quantity to add:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                
                if (quantity <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", 
                             "Quantity must be greater than 0!");
                    return;
                }
                
                // Use admin method to add quantity
                admin.addVehicleQuantity(vehicle, quantity);
                
                // Refresh table
                vehicleTable.refresh();
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         "Quantity added successfully!\nNew quantity: " + vehicle.getQuantity());
                
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", 
                         "Please enter a valid number!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Failed to add quantity: " + e.getMessage());
            }
        });
    }
    
    private void approveBooking(Booking booking) {
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            showAlert(Alert.AlertType.WARNING, "Cannot Approve", 
                     "Only pending bookings can be approved!\nCurrent status: " + booking.getStatus());
            return;
        }
        
        try {
            // Approve booking using admin method
            admin.approveBooking(booking);
            
            // Create payment record
            Payment payment = new Payment(
                booking.getBookingId(),
                booking.getCustomer(),
                booking.getTotalCost(),
                LocalDate.now()
            );
            
            // Add payment to data manager
            DataManager.getPaymentList().add(payment);
            
            // Refresh tables
            bookingTable.refresh();
            vehicleTable.refresh();
            if (paymentTable != null) {
                paymentTable.refresh();
            }
            
            // Update payment summary
            updatePaymentSummary();
            
            showAlert(Alert.AlertType.INFORMATION, "Booking Approved", 
                     "Booking " + booking.getBookingId() + " has been approved successfully!\n" +
                     "Payment ID: " + payment.getPaymentId() + "\n" +
                     "Amount: $" + String.format("%.2f", payment.getAmount()));
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Approval Failed", 
                     "Failed to approve booking: " + e.getMessage());
        }
    }
    
    private void updatePaymentSummary() {
        double totalRevenue = DataManager.getPaymentList().stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}