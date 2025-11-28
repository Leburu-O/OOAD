// controllers/OpenInvestmentAccountController.java
package controllers;

import entities.Customer;
import entities.InvestmentAccount;
import services.BankTeller;
import services.PersistenceService;

public class OpenInvestmentAccountController {

    private final BankTeller bankTeller;
    private final PersistenceService persistenceService = new PersistenceService();

    public OpenInvestmentAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens an Investment Account for the customer and saves it to the database.
     */
    public InvestmentAccount openAccount(String branch, Customer customer, double initialDeposit) {
        InvestmentAccount account = bankTeller.openInvestmentAccount(branch, customer, initialDeposit);
        customer.addAccount(account);

        // ✅ Save customer and all accounts to database
        persistenceService.saveCustomer(customer);

        return account;
    }
}