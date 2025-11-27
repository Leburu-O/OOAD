// gui/OpenInvestmentAccountScene.java
package gui;

import controllers.OpenInvestmentAccountController;
import entities.Customer;
import services.BankTeller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class OpenInvestmentAccountScene {
    private final Stage stage = new Stage();
    private final List<Customer> customers;
    private final OpenInvestmentAccountController controller;

    public OpenInvestmentAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.controller = new OpenInvestmentAccountController(bankTeller);
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Investment Account");
        title.getStyleClass().add("header-panel");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number (or New: e.g. ACC100005)");

        TextField depositField = new TextField();
        depositField.setPromptText("Initial Deposit (Minimum: BWP 500.00)");

        // 🔐 NEW: PIN Field
        TextField pinField = new TextField();
        pinField.setPromptText("Set 4-digit PIN for customer");

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {  // <-- 'e' is ActionEvent here
            String accNum = accNumField.getText().trim();
            String depositStr = depositField.getText().trim();
            String pin = pinField.getText().trim();

            if (accNum.isEmpty() || depositStr.isEmpty() || pin.isEmpty()) {
                showAlert("Missing Data", "Please fill all fields.", true);
                return;
            }
            if (!pin.matches("\\d{4}")) {
                showAlert("Invalid PIN", "PIN must be exactly 4 digits.", true);
                return;
            }

            double deposit;
            try {
                deposit = Double.parseDouble(depositStr);
            } catch (NumberFormatException ex) {  // <-- Changed 'e' to 'ex'
                showAlert("Invalid Amount", "Please enter a valid number.", true);
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
                controller.openAccount("Main Branch", customer, deposit);
                showAlert("Success", "Investment account opened successfully! Customer can now log in with PIN.", false);
                clearForm(accNumField, depositField, pinField);
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage(), true);
            }
        });

        root.getChildren().addAll(
                title,
                new Label("Customer Account #:"), accNumField,
                new Label("Initial Deposit:"), depositField,
                new Label("Set Customer PIN:"), pinField,
                openBtn
        );

        Scene scene = new Scene(root, 450, 400);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Investment Account");
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