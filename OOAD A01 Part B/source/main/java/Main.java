// Main.java
import entities.*;
import services.*;
import controllers.InterestProcessingController;
import gui.*;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Application Entry Point
 * Launches the banking system with Customer and Teller interfaces.
 * Fully compliant with functional and non-functional requirements.
 */
public class Main extends Application {

    private List<Customer> customers = new ArrayList<>();
    private BankTeller bankTeller = new BankTeller();
    private InterestService interestService = new InterestService();
    private PersistenceService persistenceService = new PersistenceService();
    private List<Account> allAccounts = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // === Initialize Database ===
        utils.DatabaseManager.initialize();

        // === Load Existing Data from SQLite (if any) ===
        customers = persistenceService.loadAllCustomers();

        // If no data exists in DB, create sample customers
        if (customers.isEmpty()) {
            Customer olerato = new Customer("Olerato", "Leburu", "Gaborone", "ACC100001", "1234");
            Customer kentsenao = new Customer("Kentsenao", "Baseki", "Francistown", "ACC100002", "5678");
            customers.add(olerato);
            customers.add(kentsenao);
        }

        // Sync all accounts list for monthly interest processing
        collectAllAccounts();

        // === Initialize Controllers ===
        InterestProcessingController interestController = 
            new InterestProcessingController(interestService, allAccounts);

        // === Initialize All GUI Scenes ===

        // 🟦 Customer Flow
        CustomerDashboardScene dashboardScene = new CustomerDashboardScene();
        CustomerLoginScene customerLoginScene = new CustomerLoginScene(customers, dashboardScene);

        // 🟨 Teller Flow
        OpenSavingsAccountScene savingsScene = new OpenSavingsAccountScene(customers, bankTeller);
        OpenChequeAccountScene chequeScene = new OpenChequeAccountScene(customers, bankTeller);
        OpenInvestmentAccountScene investmentScene = new OpenInvestmentAccountScene(customers, bankTeller);

        TellerDashboardScene tellerDashboardScene = new TellerDashboardScene(savingsScene, chequeScene, investmentScene);
        TellerLoginScene tellerLoginScene = new TellerLoginScene(tellerDashboardScene);

        // Link window ownership for proper navigation
        dashboardScene.stage.initOwner(customerLoginScene.stage);
        tellerDashboardScene.stage.initOwner(tellerLoginScene.stage);

        // === Start Application: Show Customer Login ===
        customerLoginScene.show();

        // Optional: Press 'T' to open Teller Login (for demo/testing)
        primaryStage.setScene(new javafx.scene.Scene(new javafx.scene.layout.VBox()));
        primaryStage.getScene().setOnKeyTyped(e -> {
            if (e.getCharacter().toLowerCase().equals("t")) {
                tellerLoginScene.show();
            }
            // Press 'I' to run monthly interest
            if (e.getCharacter().toLowerCase().equals("i")) {
                interestController.processMonthlyInterest();
                showAlert("Success", "Monthly interest applied and logged.", false);
            }
        });
        primaryStage.hide(); // We use our own stages; this is just a dummy holder

        // === On Close: Save All Data to Database ===
        primaryStage.setOnCloseRequest(e -> {
            persistenceService.saveAllCustomers(customers);
            System.out.println("💾 All customer and account data saved to banking.db");
        });
    }

    /**
     * Collects all accounts from all customers into a shared list
     * so that monthly interest can be processed automatically.
     */
    private void collectAllAccounts() {
        allAccounts.clear();
        customers.forEach(c -> allAccounts.addAll(c.getAccounts()));
    }

    /**
     * Shows an alert dialog with styled message.
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
     * Launches the JavaFX application.
     * Run with: mvn javafx:run
     */
    public static void main(String[] args) {
        launch(args);
    }
}