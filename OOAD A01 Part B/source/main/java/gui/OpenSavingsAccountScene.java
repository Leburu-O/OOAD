// gui/OpenSavingsAccountScene.java
package gui;

import controllers.OpenSavingsAccountController;
import entities.Customer;
import services.BankTeller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class OpenSavingsAccountScene {
    private Stage stage = new Stage();
    private List<Customer> customers;
    private OpenSavingsAccountController controller;

    public OpenSavingsAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.controller = new OpenSavingsAccountController(bankTeller);
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Savings Account");
        title.getStyleClass().add("header-panel");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number (or New: e.g. ACC100003)");

        ToggleGroup group = new ToggleGroup();
        RadioButton individual = new RadioButton("Individual");
        individual.setToggleGroup(group);
        individual.setSelected(true);
        RadioButton company = new RadioButton("Company");
        company.setToggleGroup(group);

        VBox typeBox = new VBox(5, new Label("Customer Type:"), individual, company);

        // 🔐 NEW: PIN Field
        TextField pinField = new TextField();
        pinField.setPromptText("Set 4-digit PIN for customer");

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            String pin = pinField.getText().trim();

            if (accNum.isEmpty() || pin.isEmpty() || pin.length() != 4 || !pin.matches("\\d{4}")) {
                showAlert("Invalid Input", "Please enter valid account number and 4-digit PIN.", true);
                return;
            }

            boolean isCompany = company.isSelected();

            // Check if existing customer
            Customer customer = findCustomer(accNum);
            if (customer == null) {
                // Create new customer
                customer = new Customer("New", "Customer", "Gaborone", accNum, pin);
            } else {
                // Update PIN if not already set
                if (customer.getPIN() == null || customer.getPIN().isEmpty()) {
                    customer.setPIN(pin);
                }
            }

            try {
                controller.openAccount("Main Branch", customer, isCompany);
                showAlert("Success", "Savings account opened successfully! Customer can now log in with PIN.", false);
                clearForm(accNumField, pinField);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage(), true);
            }
        });

        root.getChildren().addAll(
                title,
                new Label("Customer Account #:"), accNumField,
                typeBox,
                new Label("Set Customer PIN:"), pinField,
                openBtn
        );

        Scene scene = new Scene(root, 400, 380);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Savings Account");
        stage.show();
    }

    private Customer findCustomer(String accNum) {
        return customers.stream()
                .filter(c -> c.getAccountNumber().equals(accNum))
                .findFirst()
                .orElse(null);
    }

    private void clearForm(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private void showAlert(String title, String message, boolean isError) {
        Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        DialogPane dp = alert.getDialogPane();
        if (isError) dp.getStyleClass().add("alert-error");
        else dp.getStyleClass().add("alert-success");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}