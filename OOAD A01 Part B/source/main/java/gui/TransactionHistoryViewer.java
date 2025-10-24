// gui/TransactionHistoryViewer.java
package gui;

import controllers.HistoryViewController;
import entities.Account;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransactionHistoryViewer {
    private Stage stage = new Stage();
    private HistoryViewController historyController;

    public TransactionHistoryViewer(HistoryViewController txController, Account account) {
        this.historyController = txController;
        this.historyController.setAccount(account);
    }

    public void show() {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(15));

        Label header = new Label("Transaction History – " + historyController.getSelectedAccount().getAccountNumber());
        header.getStyleClass().add("header-panel");

        ListView<String> list = new ListView<>();
        historyController.getTransactionHistory().forEach(t -> list.getItems().add(t.toString()));

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