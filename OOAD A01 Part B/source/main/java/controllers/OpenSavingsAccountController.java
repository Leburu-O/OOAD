// controllers/OpenSavingsAccountController.java
package controllers;

import entities.Customer;
import entities.SavingsAccount;
import services.BankTeller;

/**
 * Controller for opening a Savings Account.
 * Determines interest rate based on customer type: Individual or Company.
 */
public class OpenSavingsAccountController {

    private final BankTeller bankTeller;

    public OpenSavingsAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens a Savings Account for the customer.
     * @param branch Bank branch
     * @param customer Customer opening the account
     * @param isCompany True if company account, false for individual
     * @return Newly created SavingsAccount
     */
    public SavingsAccount openAccount(String branch, Customer customer, boolean isCompany) {
        SavingsAccount account = bankTeller.openSavingsAccount(branch, customer, isCompany);
        customer.addAccount(account);
        return account;
    }
}