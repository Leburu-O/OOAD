// entities/Account.java
package entities;

import java.util.ArrayList;
import java.util.List;
import utils.AccountNumberGenerator;

/**
 * Abstract base class for all account types.
 * Enforces shared structure and behavior across Savings, Investment, and Cheque accounts.
 * Supports OOP principles: abstraction, inheritance, polymorphism.
 */
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    protected List<Transaction> transactionHistory;

    public Account(String branch, Customer customer) {
        this.branch = branch;
        this.customer = customer;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
        this.accountNumber = AccountNumberGenerator.generate();
    }

    // ----- CORE FUNCTIONALITY -----

    /**
     * Deposits funds into the account.
     * @param amount Amount to deposit (must be positive)
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        balance += amount;
        Transaction t = new Transaction("Deposit", amount, balance);
        transactionHistory.add(t);
        System.out.printf("Deposited %.2f BWP. New balance: %.2f BWP%n", amount, balance);
    }

    /**
     * Withdraws funds from the account.
     * Must be overridden by subclasses — behavior differs per account type.
     * @param amount Amount to withdraw
     */
    public abstract void withdraw(double amount);

    /**
     * Applies monthly interest based on account type.
     * Implemented differently in each subclass.
     * @return Interest amount credited
     */
    public abstract double applyInterest();

    // ----- HELPER METHODS -----

    /**
     * Logs interest credit as a transaction.
     * Called by subclasses when applying interest.
     * @param interest Amount of interest to credit
     */
    protected void logInterest(double interest) {
        balance += interest;
        Transaction t = new Transaction("Interest Credited", interest, balance);
        transactionHistory.add(t);
        System.out.printf("Interest credited: %.2f BWP to Account %s%n", interest, accountNumber);
    }

    // ----- GETTERS -----

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getBranch() {
        return branch;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    // ----- SETTERS FOR DATABASE LOADING -----
    // These are protected to maintain encapsulation
    // Used only during object reconstruction from SQLite

    /**
     * Sets the account number (used when loading from database).
     * @param accountNumber Account number to set
     */
    protected void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Sets the account balance (used when loading from database).
     * @param balance Balance to set
     */
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // ----- TOSTRING & DEBUG -----

    @Override
    public String toString() {
        return String.format("Account{Num='%s', Type='%s', Balance=%.2f BWP}", 
                accountNumber, this.getClass().getSimpleName(), balance);
    }
}