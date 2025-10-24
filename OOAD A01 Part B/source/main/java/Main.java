// Main.java
import entities.*;
import services.BankTeller;
import services.InterestService;
import controllers.InterestProcessingController;
import gui.*;

import javafx.application.Application;
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
    private List<Account> allAccounts = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // === Initialize Sample Customers (from interviewee details) ===
        Customer olerato = new Customer("Olerato", "Leburu", "Gaborone", "ACC100001", "1234");
        Customer kentsenao = new Customer("Kentsenao", "Baseki", "Francistown", "ACC100002", "5678");
        customers.add(olerato);
        customers.add(kentsenao);

        // Share accounts for monthly interest processing
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
        });
        primaryStage.hide(); // We use our own stages; this is just a dummy holder
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
     * Launches the JavaFX application.
     * Run with: mvn javafx:run
     */
    public static void main(String[] args) {
        launch(args);
    }
}