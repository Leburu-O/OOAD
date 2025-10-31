// gui/TellerDashboardScene.java
package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TellerDashboardScene {
    public Stage stage = new Stage();
    private OpenSavingsAccountScene savingsScene;
    private OpenChequeAccountScene chequeScene;
    private OpenInvestmentAccountScene investmentScene;

    public TellerDashboardScene(OpenSavingsAccountScene s, OpenChequeAccountScene c, OpenInvestmentAccountScene i) {
        this.savingsScene = s;
        this.chequeScene = c;
        this.investmentScene = i;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Teller Dashboard");
        title.getStyleClass().add("header-panel");
        title.setMinWidth(300);

        Button openSavings = new Button("Open Savings Account");
        openSavings.setOnAction(e -> savingsScene.show());

        Button openCheque = new Button("Open Cheque Account");
        openCheque.setOnAction(e -> chequeScene.show());

        Button openInvestment = new Button("Open Investment Account");
        openInvestment.setOnAction(e -> investmentScene.show());

        Button logout = new Button("Logout");
        logout.setOnAction(e -> stage.hide());

        root.getChildren().addAll(title, openSavings, openCheque, openInvestment, logout);

        Scene scene = new Scene(root, 400, 350);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("Teller Dashboard");
        stage.show();
    }
}