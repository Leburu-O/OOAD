// gui/OpenChequeAccountScene.java
package gui;

import controllers.OpenChequeAccountController;
import entities.Customer;
import services.BankTeller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class OpenChequeAccountScene {
    private Stage stage = new Stage();
    private List<Customer> customers;
    private OpenChequeAccountController controller;

    public OpenChequeAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.controller = new OpenChequeAccountController(bankTeller);
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Cheque Account");
        title.getStyleClass().add("header-panel");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number (or New: e.g. ACC100004)");

        TextField employerField = new TextField();
        employerField.setPromptText("Employer or Business Name");

        TextField addressField = new TextField();
        addressField.setPromptText("Company/Business Address");

        // 🔐 NEW: PIN Field
        TextField pinField = new TextField();
        pinField.setPromptText("Set 4-digit PIN for customer");

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            String employer = employerField.getText().trim();
            String address = addressField.getText().trim();
            String pin = pinField.getText().trim();

            if (accNum.isEmpty() || employer.isEmpty() || address.isEmpty() || pin.isEmpty()) {
                showAlert("Missing Data", "Please fill all fields.", true);
                return;
            }
            if (!pin.matches("\\d{4}")) {
                showAlert("Invalid PIN", "PIN must be exactly 4 digits.", true);
                return;
            }

            Customer customer = findCustomer(accNum);
            if (customer == null) {
                customer = new Customer("New", "Customer", "Gaborone", accNum, pin);
            } else {
                if (customer.getPIN() == null || customer.getPIN().isEmpty()) {
                    customer.setPIN(pin);
                }
            }

            try {
                controller.openAccount("Main Branch", customer, employer, address);
                showAlert("Success", "Cheque account opened successfully! Customer can now log in with PIN.", false);
                clearForm(accNumField, employerField, addressField, pinField);
            } catch (Exception ex) {
                showAlert("Error", "Failed to open account: " + ex.getMessage(), true);
            }
        });

        root.getChildren().addAll(
                title,
                new Label("Customer Account #:"), accNumField,
                new Label("Employer / Business Name:"), employerField,
                new Label("Address:"), addressField,
                new Label("Set Customer PIN:"), pinField,
                openBtn
        );

        Scene scene = new Scene(root, 450, 450);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Cheque Account");
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