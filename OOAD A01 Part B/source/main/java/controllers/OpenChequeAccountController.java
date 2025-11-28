// controllers/OpenChequeAccountController.java
package controllers;

import entities.Customer;
import entities.ChequeAccount;
import services.BankTeller;
import services.PersistenceService;

public class OpenChequeAccountController {

    private final BankTeller bankTeller;
    private final PersistenceService persistenceService = new PersistenceService();

    public OpenChequeAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens a Cheque Account for the customer and saves it to the database.
     */
    public ChequeAccount openAccount(String branch, Customer customer, String employerName, String employerAddress) {
        ChequeAccount account = bankTeller.openChequeAccount(branch, customer, employerName, employerAddress);
        customer.addAccount(account);

        // ✅ Save customer and all accounts to database
        persistenceService.saveCustomer(customer);

        return account;
    }
}