// gui/TellerLoginScene.java
package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TellerLoginScene {
    private Stage stage;
    private TellerDashboardScene tellerDashboard;

    public TellerLoginScene(TellerDashboardScene tellerDashboard) {
        this.tellerDashboard = tellerDashboard;
        this.stage = new Stage();
        createUI();
    }

    private void createUI() {
        stage.setTitle("🔐 Teller Login");
        stage.setResizable(false);

        Label titleLabel = new Label("Bank Teller Login");
        titleLabel.getStyleClass().add("header-panel");

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Enter Teller PIN");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String pin = pinField.getText();
            if ("1234".equals(pin)) { // Simple demo PIN
                stage.hide();
                tellerDashboard.show();
            } else {
                showAlert("Access Denied", "Incorrect PIN.", true);
            }
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Teller PIN:"), pinField,
                loginButton
        );
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout, 350, 250);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
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