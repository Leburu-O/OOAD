// gui/CustomerDashboardScene.java
package gui;

import controllers.TransactionController;
import entities.Account;
import entities.Transaction;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerDashboardScene {
    public Stage stage = new Stage();
    private ComboBox<Account> accountCombo;
    private ListView<String> historyView;
    private TransactionController txController;

    public void setCustomer(entities.Customer customer) {
        this.txController = new TransactionController(customer);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        Label welcomeLabel = new Label("Welcome, " + customer.getFirstName() + " " + customer.getSurname());
        welcomeLabel.getStyleClass().add("header-panel");
        HBox topBar = new HBox(welcomeLabel);
        topBar.setStyle("-fx-background-color: #003366;");
        root.setTop(topBar);

        VBox left = new VBox(15);
        left.setPrefWidth(250);
        left.setPadding(new javafx.geometry.Insets(15));

        left.getChildren().addAll(
                new Label("Select Account:"),
                new Label("")
        );

        accountCombo = new ComboBox<>();
        accountCombo.setItems(FXCollections.observableArrayList(txController.getAccounts()));
        accountCombo.getSelectionModel().selectFirst();
        left.getChildren().add(accountCombo);

        Button depositBtn = new Button("Deposit");
        depositBtn.setOnAction(e -> performTransaction(true));

        Button withdrawBtn = new Button("Withdraw");
        withdrawBtn.setOnAction(e -> performTransaction(false));

        Button historyBtn = new Button("View History");
        historyBtn.setOnAction(e -> openHistoryViewer());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> confirmLogout());

        left.getChildren().addAll(depositBtn, withdrawBtn, historyBtn, logoutBtn);
        root.setLeft(left);

        VBox right = new VBox(10);
        right.setPadding(new javafx.geometry.Insets(15));
        right.getChildren().add(new Label("Recent Transactions:"));

        historyView = new ListView<>();
        historyView.setMinWidth(350);
        right.getChildren().add(historyView);

        root.setCenter(right);

        refreshHistory();
        accountCombo.setOnAction(e -> refreshHistory());

        Scene scene = new Scene(root, 750, 450);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Customer Dashboard");
    }

    private void performTransaction(boolean isDeposit) {
        Account selected = accountCombo.getValue();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(isDeposit ? "Deposit Funds" : "Withdraw Funds");
        dialog.setHeaderText("Enter amount in BWP:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                if (amount <= 0) {
                    showAlert("Invalid", "Amount must be positive.", true);
                    return;
                }
                if (isDeposit) {
                    txController.deposit(selected, amount);
                } else {
                    txController.withdraw(selected, amount);
                }
                refreshHistory();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showAlert("Error", ex.getMessage(), true);
            }
        });
    }

    private void openHistoryViewer() {
        new TransactionHistoryViewer(txController, accountCombo.getValue()).show();
    }

    private void confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");
        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                stage.hide();
                ((CustomerLoginScene) stage.getOwner()).show();
            }
        });
    }

    private void refreshHistory() {
        Account selected = accountCombo.getValue();
        if (selected == null) return;
        historyView.getItems().clear();
        selected.getTransactionHistory().forEach(t -> historyView.getItems().add(t.toString()));
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