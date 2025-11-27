// gui/CustomerDashboardScene.java
package gui;

import controllers.HistoryViewController;
import controllers.TransactionController;
import entities.Account;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerDashboardScene {
    private Stage stage = new Stage();
    private ComboBox<Account> accountCombo;
    private ListView<String> historyView;
    private TransactionController txController;
    private entities.Customer customer;
    private CustomerLoginScene loginScene;

    /**
     * Sets the current customer and links back to login scene for logout.
     */
    public void setCustomer(entities.Customer customer, CustomerLoginScene loginScene) {
        this.customer = customer;
        this.loginScene = loginScene;
        this.txController = new TransactionController(customer);

        // Build UI
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Header
        Label welcomeLabel = new Label("Welcome, " + customer.getFirstName() + " " + customer.getSurname());
        welcomeLabel.getStyleClass().add("header-panel");
        HBox topBar = new HBox(welcomeLabel);
        topBar.setStyle("-fx-background-color: #003366;");
        root.setTop(topBar);

        // Left Panel: Account & Actions
        VBox left = new VBox(15);
        left.setPrefWidth(280);
        left.setPadding(new javafx.geometry.Insets(15));

        // Account Selection
        left.getChildren().add(new Label("Select Account:"));

        accountCombo = new ComboBox<>();
        accountCombo.setItems(FXCollections.observableArrayList(txController.getAccounts()));
        if (!txController.getAccounts().isEmpty()) {
            accountCombo.getSelectionModel().selectFirst();
        }
        accountCombo.setOnAction(e -> refreshHistory());
        left.getChildren().add(accountCombo);

        // Action Buttons
        Button depositBtn = new Button("Deposit");
        depositBtn.setOnAction(e -> performTransaction(true));

        Button withdrawBtn = new Button("Withdraw");
        withdrawBtn.setOnAction(e -> performTransaction(false));

        Button historyBtn = new Button("View History");
        historyBtn.setOnAction(e -> openHistoryViewer());

        Button changePinBtn = new Button("Change PIN");
        changePinBtn.setOnAction(e -> changePIN());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> confirmLogout());

        left.getChildren().addAll(depositBtn, withdrawBtn, historyBtn, changePinBtn, logoutBtn);
        root.setLeft(left);

        // Right Panel: Transaction Preview
        VBox right = new VBox(10);
        right.setPadding(new javafx.geometry.Insets(15));
        right.getChildren().add(new Label("Recent Transactions:"));

        historyView = new ListView<>();
        historyView.setMinWidth(350);
        right.getChildren().add(historyView);

        root.setCenter(right);

        // Initial load
        refreshHistory();

        // Final scene setup
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Customer Dashboard");
        stage.setResizable(false);
    }

    /**
     * Handles deposit or withdrawal.
     */
    private void performTransaction(boolean isDeposit) {
        Account selected = accountCombo.getValue();
        if (selected == null) {
            showAlert("No Account", "Please select an account.", true);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(isDeposit ? "Deposit Funds" : "Withdraw Funds");
        dialog.setHeaderText("Enter amount in BWP:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be positive.");
                }

                if (isDeposit) {
                    txController.deposit(selected, amount);
                    showAlert("Success", String.format("Deposited %.2f BWP", amount), false);
                } else {
                    txController.withdraw(selected, amount);
                    showAlert("Success", String.format("Withdrew %.2f BWP", amount), false);
                }

                // ✅ Refresh history after transaction
                refreshHistory();

            } catch (IllegalArgumentException ex) {
                showAlert("Invalid Input", ex.getMessage(), true);
            } catch (IllegalStateException ex) {
                showAlert("Action Not Allowed", ex.getMessage(), true);
            }
        });
    }

    /**
     * Opens full transaction history viewer.
     */
    private void openHistoryViewer() {
        HistoryViewController hvc = new HistoryViewController();
        hvc.setAccount(accountCombo.getValue());
        new TransactionHistoryViewer(hvc, accountCombo.getValue()).show();
    }

    /**
     * Allows customer to change their PIN.
     */
    private void changePIN() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change PIN");
        dialog.setHeaderText("Enter new 4-digit PIN");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("New 4-digit PIN");
        dialog.getDialogPane().setContent(pinField);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) return pinField.getText();
            return null;
        });

        dialog.showAndWait().ifPresent(newPin -> {
            if (newPin != null && newPin.matches("\\d{4}")) {
                customer.setPIN(newPin);
                showAlert("Success", "PIN changed successfully!", false);
            } else {
                showAlert("Invalid PIN", "PIN must be exactly 4 digits.", true);
            }
        });
    }

    /**
     * Confirms logout.
     */
    private void confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");
        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                stage.hide();
                loginScene.show();
            }
        });
    }

    /**
     * Refreshes the transaction preview list.
     */
    private void refreshHistory() {
        Account selected = accountCombo.getValue();
        if (selected == null) {
            historyView.getItems().clear();
            historyView.getItems().add("No account selected.");
            return;
        }
        historyView.getItems().clear();
        selected.getTransactionHistory().forEach(t -> historyView.getItems().add(t.toString()));
    }

    /**
     * Shows alert dialog with styling.
     */
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

    /**
     * Displays the dashboard window.
     */
    public void show() {
        stage.show();
    }
}