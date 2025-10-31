// gui/TransactionHistoryViewer.java
package gui;

import controllers.HistoryViewController;
import entities.Account;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * GUI Scene: Displays transaction history for a selected account.
 * Shows deposits, withdrawals, and "Interest Credited" entries.
 */
public class TransactionHistoryViewer {
    private Stage stage = new Stage();
    private HistoryViewController historyController;

    /**
     * Creates a new transaction history viewer.
     * @param historyController The controller managing history logic
     * @param account The account whose history to display
     */
    public TransactionHistoryViewer(HistoryViewController historyController, Account account) {
        this.historyController = historyController;
        this.historyController.setAccount(account);
    }

    /**
     * Displays the transaction history window.
     */
    public void show() {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(15));

        // Header with account number
        Label header = new Label("Transaction History – " + historyController.getSelectedAccount().getAccountNumber());
        header.getStyleClass().add("header-panel");

        // List view for transactions
        ListView<String> list = new ListView<>();
        historyController.getTransactionHistory().forEach(t -> list.getItems().add(t.toString()));

        // Close button
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());

        // Layout
        root.getChildren().addAll(header, list, closeBtn);

        // Scene setup
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Transaction History");
        stage.show();
    }
}