// controllers/OpenChequeAccountController.java
package controllers;

import entities.Customer;
import entities.ChequeAccount;
import services.BankTeller;

/**
 * Controller for opening a Cheque Account.
 * Requires employer or business name for employed/self-employed customers.
 */
public class OpenChequeAccountController {

    private final BankTeller bankTeller;

    public OpenChequeAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens a Cheque Account for the customer.
     * @param branch Bank branch
     * @param customer Customer opening the account
     * @param employerName Employer or business name
     * @param employerAddress Company or business address
     * @return Newly created ChequeAccount
     */
    public ChequeAccount openAccount(String branch, Customer customer, String employerName, String employerAddress) {
        ChequeAccount account = bankTeller.openChequeAccount(branch, customer, employerName, employerAddress);
        customer.addAccount(account);
        return account;
    }
}