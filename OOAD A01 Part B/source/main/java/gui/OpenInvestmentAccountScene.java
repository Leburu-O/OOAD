// gui/OpenInvestmentAccountScene.java
package gui;

import controllers.OpenInvestmentAccountController;
import entities.Customer;
import services.BankTeller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;
import java.util.List;

/**
 * GUI Scene: Teller opens an Investment Account.
 * Requires minimum initial deposit of BWP500.00.
 */
public class OpenInvestmentAccountScene {
    private Stage stage = new Stage();
    private List<Customer> customers;
    private OpenInvestmentAccountController controller;

    public OpenInvestmentAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.controller = new OpenInvestmentAccountController(bankTeller);
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Investment Account");
        title.getStyleClass().add("header-panel");
        title.setMinWidth(Region.USE_PREF_SIZE);
        title.setAlignment(javafx.geometry.Pos.CENTER);

        // Customer lookup
        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number");

        // Initial Deposit
        TextField depositField = new TextField();
        depositField.setPromptText("Initial Deposit (Minimum: BWP 500.00)");

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            String depositStr = depositField.getText().trim();

            if (accNum.isEmpty() || depositStr.isEmpty()) {
                showAlert("Missing Data", "Please fill all fields.", true);
                return;
            }

            Customer customer = findCustomer(accNum);
            if (customer == null) {
                showAlert("Not Found", "Customer not found.", true);
                return;
            }

            try {
                double deposit = Double.parseDouble(depositStr);
                controller.openAccount("Main Branch", customer, deposit);
                showAlert("Success", "Investment account opened successfully!", false);
                clearForm(accNumField, depositField);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Amount", "Please enter a valid number for deposit.", true);
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage(), true);
            }
        });

        root.getChildren().addAll(
                title,
                new Label("Customer Account #:"), accNumField,
                new Label("Initial Deposit:"), depositField,
                openBtn
        );

        Scene scene = new Scene(root, 450, 350);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Investment Account");
        stage.setResizable(false);
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