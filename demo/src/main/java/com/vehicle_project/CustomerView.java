package com.vehicle_project;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.transformation.FilteredList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.Optional;

public class CustomerView extends BorderPane {
    
    private Customer customer;
    private TableView<Vehicle> vehicleTable;
    private TableView<Booking> bookingTable;
    
    public CustomerView(Customer customer) {
        this.customer = customer;
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
        header.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 5;");
        
        // Welcome label
        Label welcomeLabel = new Label("Welcome, " + customer.getName());
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        welcomeLabel.setStyle("-fx-text-fill: white;");
        
        // Spacer
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                          "-fx-font-weight: bold; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> Main.showLoginView());
        
        header.getChildren().addAll(welcomeLabel, spacer, logoutBtn);
        return header;
    }
    
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Tab 1: Rent Vehicle
        Tab rentTab = new Tab("Rent Vehicle");
        rentTab.setContent(createRentVehicleTab());
        
        // Tab 2: My Bookings
        Tab bookingsTab = new Tab("My Bookings");
        bookingsTab.setContent(createMyBookingsTab());
        
        tabPane.getTabs().addAll(rentTab, bookingsTab);
        return tabPane;
    }
    
    private VBox createRentVehicleTab() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Available Vehicles");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Create vehicle table
        vehicleTable = new TableView<>();
        vehicleTable.setItems(DataManager.getVehicleList());
        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
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
        
        // Price Per Day Column
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
        
        // Available Column
        TableColumn<Vehicle, Boolean> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isAvailable())
        );
        availCol.setPrefWidth(100);
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
            private final Button rentBtn = new Button("Rent Vehicle");
            
            {
                rentBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                               "-fx-font-weight: bold; -fx-cursor: hand;");
                rentBtn.setOnAction(e -> {
                    Vehicle vehicle = getTableView().getItems().get(getIndex());
                    if (vehicle != null) {
                        showCreateBookingDialog(vehicle);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Vehicle vehicle = getTableView().getItems().get(getIndex());
                    rentBtn.setDisable(!vehicle.isAvailable());
                    setGraphic(rentBtn);
                }
            }
        });
        
        vehicleTable.getColumns().addAll(idCol, brandCol, modelCol, yearCol, priceCol, availCol, actionCol);
        
        container.getChildren().addAll(titleLabel, vehicleTable);
        return container;
    }
    
    private VBox createMyBookingsTab() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("My Bookings");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Create booking table with filtered list
        bookingTable = new TableView<>();
        
        // Filter bookings to only show current customer's bookings
        FilteredList<Booking> filteredBookings = new FilteredList<>(
            DataManager.getBookingList(),
            booking -> booking.getCustomer().equals(customer)
        );
        
        bookingTable.setItems(filteredBookings);
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Booking ID Column
        TableColumn<Booking, String> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingIdCol.setPrefWidth(100);
        
        // Vehicle Description Column
        TableColumn<Booking, String> vehicleCol = new TableColumn<>("Vehicle Description");
        vehicleCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRentedItem().getDescription()
            )
        );
        vehicleCol.setPrefWidth(200);
        
        // Rental Date Column
        TableColumn<Booking, LocalDate> rentalDateCol = new TableColumn<>("Rental Date");
        rentalDateCol.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        rentalDateCol.setPrefWidth(120);
        
        // Return Date Column
        TableColumn<Booking, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(120);
        
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
        statusCol.setPrefWidth(120);
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
        
        bookingTable.getColumns().addAll(bookingIdCol, vehicleCol, rentalDateCol, 
                                        returnDateCol, costCol, statusCol);
        
        container.getChildren().addAll(titleLabel, bookingTable);
        return container;
    }
    
    private void showCreateBookingDialog(Vehicle vehicle) {
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Rent Vehicle");
        dialog.setHeaderText("Create Booking for: " + vehicle.getDescription());
        
        // Dialog buttons
        ButtonType rentButtonType = new ButtonType("Rent", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(rentButtonType, ButtonType.CANCEL);
        
        // Dialog content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Rental Date Picker
        Label rentalDateLabel = new Label("Rental Date:");
        rentalDateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        DatePicker rentalDatePicker = new DatePicker(LocalDate.now());
        rentalDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        // Return Date Picker
        Label returnDateLabel = new Label("Return Date:");
        returnDateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        DatePicker returnDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        returnDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate rentalDate = rentalDatePicker.getValue();
                setDisable(empty || date.isBefore(LocalDate.now()) || 
                          (rentalDate != null && date.isBefore(rentalDate)));
            }
        });
        
        // Update return date picker when rental date changes
        rentalDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && returnDatePicker.getValue() != null && 
                returnDatePicker.getValue().isBefore(newVal)) {
                returnDatePicker.setValue(newVal.plusDays(1));
            }
        });
        
        // Price Preview
        Label priceLabel = new Label("Estimated Cost: $0.00");
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        priceLabel.setStyle("-fx-text-fill: #2196F3;");
        
        // Update price when dates change
        Runnable updatePrice = () -> {
            LocalDate rental = rentalDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            if (rental != null && returnDate != null && !returnDate.isBefore(rental)) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(rental, returnDate);
                double cost = vehicle.getRentalPrice((int) days);
                priceLabel.setText(String.format("Estimated Cost: $%.2f", cost));
            }
        };
        
        rentalDatePicker.valueProperty().addListener((obs, old, newVal) -> updatePrice.run());
        returnDatePicker.valueProperty().addListener((obs, old, newVal) -> updatePrice.run());
        updatePrice.run();
        
        content.getChildren().addAll(
            rentalDateLabel, rentalDatePicker,
            returnDateLabel, returnDatePicker,
            new Separator(),
            priceLabel
        );
        
        dialog.getDialogPane().setContent(content);
        
        // Convert result to booking
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == rentButtonType) {
                try {
                    LocalDate rentalDate = rentalDatePicker.getValue();
                    LocalDate returnDate = returnDatePicker.getValue();
                    
                    if (rentalDate == null || returnDate == null) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Dates", 
                                "Please select both rental and return dates.");
                        return null;
                    }
                    
                    if (returnDate.isBefore(rentalDate)) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Dates", 
                                "Return date cannot be before rental date.");
                        return null;
                    }
                    
                    // Create booking using customer's requestVehicle method
                    Booking booking = customer.requestVehicle(vehicle, rentalDate, returnDate);
                    return booking;
                    
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Booking Failed", 
                            "Error creating booking: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        // Show dialog and handle result
        Optional<Booking> result = dialog.showAndWait();
        result.ifPresent(booking -> {
            // Add booking to DataManager
            DataManager.getBookingList().add(booking);
            
            // Refresh tables
            vehicleTable.refresh();
            bookingTable.refresh();
            
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Booking Created", 
                    "Booking " + booking.getBookingId() + " has been created successfully!\n" +
                    "Status: " + booking.getStatus() + "\n" +
                    "Please wait for admin approval.");
        });
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}