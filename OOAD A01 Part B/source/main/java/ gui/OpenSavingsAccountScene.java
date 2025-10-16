// gui/OpenSavingsAccountScene.java
package gui;

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
    private BankTeller bankTeller;

    public OpenSavingsAccountScene(List<Customer> customers, BankTeller bankTeller) {
        this.customers = customers;
        this.bankTeller = bankTeller;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Open Savings Account");
        title.getStyleClass().add("header-panel");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Customer Account Number");

        ToggleGroup group = new ToggleGroup();
        RadioButton individual = new RadioButton("Individual");
        individual.setToggleGroup(group);
        individual.setSelected(true);
        RadioButton company = new RadioButton("Company");
        company.setToggleGroup(group);

        VBox typeBox = new VBox(5, new Label("Customer Type:"), individual, company);

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            String accNum = accNumField.getText().trim();
            Customer c = findCustomer(accNum);
            if (c == null) {
                showAlert("Not Found", "Customer not found.", true);
                return;
            }
            boolean isCompany = company.isSelected();
            bankTeller.openSavingsAccount("Main Branch", c, isCompany);
            c.addAccount(c.getAccounts().get(c.getAccounts().size() - 1));
            showAlert("Success", "Savings account opened successfully!", false);
        });

        root.getChildren().addAll(title, new Label("Customer Account #:"), accNumField, typeBox, openBtn);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Open Savings Account");
        stage.show();
    }

    private Customer findCustomer(String accNum) {
        return customers.stream().filter(c -> c.getAccountNumber().equals(accNum)).findFirst().orElse(null);
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