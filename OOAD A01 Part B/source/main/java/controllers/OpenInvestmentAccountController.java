// controllers/OpenInvestmentAccountController.java
package controllers;

import entities.Customer;
import entities.InvestmentAccount;
import services.BankTeller;

/**
 * Controller for opening an Investment Account.
 * Ensures minimum initial deposit of BWP500.00 is met.
 */
public class OpenInvestmentAccountController {

    private final BankTeller bankTeller;

    public OpenInvestmentAccountController(BankTeller bankTeller) {
        this.bankTeller = bankTeller;
    }

    /**
     * Opens an Investment Account for the customer.
     * @param branch Bank branch
     * @param customer Customer opening the account
     * @param initialDeposit Initial deposit amount
     * @return Newly created InvestmentAccount
     * @throws IllegalArgumentException if deposit < BWP500.00
     */
    public InvestmentAccount openAccount(String branch, Customer customer, double initialDeposit) {
        InvestmentAccount account = bankTeller.openInvestmentAccount(branch, customer, initialDeposit);
        customer.addAccount(account);
        return account;
    }
}