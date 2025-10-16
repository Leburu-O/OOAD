// gui/TransactionHistoryViewer.java
package gui;

import entities.Account;
import entities.Customer;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransactionHistoryViewer {
    private Stage stage = new Stage();
    private Customer customer;
    private Account account;

    public TransactionHistoryViewer(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    public void show() {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(15));

        Label header = new Label("Transaction History – " + account.getAccountNumber());
        header.getStyleClass().add("header-panel");

        ListView<String> list = new ListView<>();
        account.getTransactionHistory().forEach(t -> list.getItems().add(t.toString()));

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(header, list, closeBtn);

        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Transaction History");
        stage.show();
    }
}