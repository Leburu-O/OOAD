// gui/CustomerLoginScene.java
package gui;

import entities.Customer;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CustomerLoginScene {
    public final Stage stage;
    private final List<Customer> customers;
    private final CustomerDashboardScene dashboardScene;
    private final TellerLoginScene tellerLoginScene; // Reference to teller login

    public CustomerLoginScene(List<Customer> customers, CustomerDashboardScene dashboardScene, TellerLoginScene tellerLoginScene) {
        this.customers = customers;
        this.dashboardScene = dashboardScene;
        this.tellerLoginScene = tellerLoginScene;
        this.stage = new Stage();
        createUI();
    }

    private void createUI() {
        stage.setTitle("🏦 Customer Login");
        stage.setResizable(false);

        Label titleLabel = new Label("Customer Login");
        titleLabel.getStyleClass().add("header-panel");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Account Number");

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            String pin = pinField.getText().trim();

            if (accNum.isEmpty() || pin.isEmpty()) {
                showAlert("Error", "Please fill all fields.", true);
                return;
            }

            Customer customer = findCustomer(accNum);
            if (customer != null && customer.authenticate(accNum, pin)) {
                dashboardScene.setCustomer(customer, this);
                stage.hide();
                dashboardScene.show();
            } else {
                showAlert("Failed", "Invalid credentials.", true);
            }
        });

        // 🔘 NEW: Button to open Teller Login
        Button tellerButton = new Button("🔐 Bank Teller? Click Here");
        tellerButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        tellerButton.setOnAction(e -> {
            stage.hide();
            tellerLoginScene.show();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Account Number:"), accNumField,
                new Label("PIN:"), pinField,
                loginButton,
                tellerButton  // ← Added here
        );
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout, 380, 380); // Slightly taller for button
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
    }

    private Customer findCustomer(String accNum) {
        return customers.stream()
                .filter(c -> c.getAccountNumber().equals(accNum))
                .findFirst()
                .orElse(null);
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

    public void show() {
        stage.show();
    }
}