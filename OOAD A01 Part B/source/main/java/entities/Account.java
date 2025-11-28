// entities/Account.java
package entities;

import services.PersistenceService;
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

    /**
     * Deposits funds into the account and saves the transaction.
     */
    public void deposit(double amount) {
        if (amount <= 0) return;
        balance += amount;
        Transaction t = new Transaction("Deposit", amount, balance);
        transactionHistory.add(t);

        // ✅ Save transaction immediately
        new PersistenceService().saveTransaction(t, this.accountNumber);
    }

    /**
     * Withdraws funds — must be overridden per account type.
     */
    public abstract void withdraw(double amount);

    /**
     * Applies monthly interest — implemented differently per subclass.
     */
    public abstract double applyInterest();

    /**
     * Logs interest as a transaction and saves it.
     */
    protected void logInterest(double interest) {
        balance += interest;
        Transaction t = new Transaction("Interest Credited", interest, balance);
        transactionHistory.add(t);

        // ✅ Save interest transaction immediately
        new PersistenceService().saveTransaction(t, this.accountNumber);
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    // Setters (for DB loading)
    public void setBalance(double balance) { this.balance = balance; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    @Override
    public String toString() {
        return String.format("Account{Num='%s', Type='%s', Balance=%.2f BWP}",
                accountNumber, this.getClass().getSimpleName(), balance);
    }
}