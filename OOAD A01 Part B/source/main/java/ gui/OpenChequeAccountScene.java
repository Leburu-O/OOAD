// gui/OpenChequeAccountScene.java
package gui;

import entities.Customer;
import services.BankTeller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * GUI Scene: Teller opens a Cheque Account.
 * Requires employer or business name for employed/self-employed customers.
 */
public class OpenChequeAccountScene {
    private Stage stage = new Stage();
    private List<Customer> customers;
    private BankTeller bankTeller;

    public OpenChequeAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.bankTeller = bankTeller;
    }

    /**
     * Displays the form to open a Cheque Account.
     */
    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Cheque Account");
        title.getStyleClass().add("header-panel");
        title.setMinWidth(Region.USE_PREF_SIZE);
        title.setAlignment(javafx.geometry.Pos.CENTER);

        // Customer lookup
        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number");

        // Employer/Business Info
        TextField employerField = new TextField();
        employerField.setPromptText("Employer Name (or Business Name if self-employed)");

        TextField addressField = new TextField();
        addressField.setPromptText("Company/Business Address");

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            String employer = employerField.getText().trim();
            String address = addressField.getText().trim();

            if (accNum.isEmpty() || employer.isEmpty() || address.isEmpty()) {
                showAlert("Missing Data", "Please fill all fields.", true);
                return;
            }

            Customer customer = findCustomer(accNum);
            if (customer == null) {
                showAlert("Not Found", "Customer not found.", true);
                return;
            }

            try {
                // Open account via teller
                bankTeller.openChequeAccount("Main Branch", customer, employer, address);
                customer.addAccount(customer.getAccounts().get(customer.getAccounts().size() - 1));
                showAlert("Success", "Cheque account opened successfully!", false);
                clearForm(accNumField, employerField, addressField);
            } catch (Exception ex) {
                showAlert("Error", "Failed to open account: " + ex.getMessage(), true);
            }
        });

        root.getChildren().addAll(
                title,
                new Label("Customer Account #:"), accNumField,
                new Label("Employer / Business Name:"), employerField,
                new Label("Address:"), addressField,
                openBtn
        );

        Scene scene = new Scene(root, 450, 400);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Cheque Account");
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