// controllers/TransactionController.java
package controllers;

import entities.Account;
import entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    private final Customer customer;

    public TransactionController(Customer customer) {
        this.customer = customer;
    }

    public void deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        account.deposit(amount);
    }

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

    public List<Account> getAccounts() {
        return new ArrayList<>(customer.getAccounts());
    }
}