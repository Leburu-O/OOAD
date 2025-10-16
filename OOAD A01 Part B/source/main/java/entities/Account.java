package entities;

import utils.AccountNumberGenerator;
import java.util.ArrayList;
import java.util.List;

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

    // Common method
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        balance += amount;
        Transaction t = new Transaction("Deposit", amount, balance);
        transactionHistory.add(t);
        System.out.printf("Deposited %.2f BWP. New balance: %.2f BWP\n", amount, balance);
    }

    // Abstract methods — must be defined by subclasses
    public abstract void withdraw(double amount);

    public abstract double applyInterest();

    // Logging interest
    protected void logInterest(double interest) {
        balance += interest;
        Transaction t = new Transaction("Interest Credited", interest, balance);
        transactionHistory.add(t);
        System.out.printf("Interest credited: %.2f BWP to Account %s\n", interest, accountNumber);
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    @Override
    public String toString() {
        return String.format("Account{Num='%s', Type='%s', Balance=%.2f}", 
                accountNumber, this.getClass().getSimpleName(), balance);
    }
}