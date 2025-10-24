// controllers/TransactionController.java
package controllers;

import entities.Account;
import entities.Customer;

/**
 * Controller for handling deposit and withdrawal transactions.
 * Enforces business rules (e.g., no withdrawals from Savings).
 */
public class TransactionController {

    private final Customer customer;

    public TransactionController(Customer customer) {
        this.customer = customer;
    }

    /**
     * Deposits funds into the specified account.
     * @param account Account to deposit into
     * @param amount Amount to deposit
     * @throws IllegalArgumentException if amount is invalid
     */
    public void deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        account.deposit(amount);
    }

    /**
     * Withdraws funds from the specified account if allowed.
     * @param account Account to withdraw from
     * @param amount Amount to withdraw
     * @throws IllegalStateException if withdrawal is not allowed or insufficient funds
     */
    public void withdraw(Account account, double amount) {
        if (account.getClass().getSimpleName().contains("Savings")) {
            throw new IllegalStateException("Withdrawal not allowed from Savings Account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > account.getBalance()) {
            throw new IllegalStateException("Insufficient funds.");
        }
        account.withdraw(amount);
    }

    /**
     * Gets all accounts owned by the customer.
     * @return Array of customer's accounts
     */
    public Account[] getAccounts() {
        return customer.getAccounts().toArray(new Account[0]);
    }
}