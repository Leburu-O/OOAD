// controllers/OpenSavingsAccountController.java
package controllers;

import entities.Customer;
import entities.SavingsAccount;
import services.BankTeller;
import services.PersistenceService;

public class OpenSavingsAccountController {

    private final BankTeller bankTeller;
    private final PersistenceService persistenceService = new PersistenceService();

    public OpenSavingsAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens a Savings Account for the customer and saves it to the database.
     */
    public SavingsAccount openAccount(String branch, Customer customer, boolean isCompany) {
        SavingsAccount account = bankTeller.openSavingsAccount(branch, customer, isCompany);
        customer.addAccount(account);

        // ✅ Save customer and all accounts to database
        persistenceService.saveCustomer(customer);

        return account;
    }
}