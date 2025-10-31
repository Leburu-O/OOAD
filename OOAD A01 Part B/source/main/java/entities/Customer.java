// Customer.java
package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank customer.
 * Holds personal details and manages linked accounts.
 */
public class Customer {
    private String firstName;
    private String surname;
    private String address;
    private String accountNumber; // Used as username
    private String pin;
    private List<Account> accounts;

    public Customer(String firstName, String surname, String address, String accountNumber, String pin) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    /**
     * Authenticates the customer using account number and PIN.
     * @param accNum Account number provided during login
     * @param pin PIN provided during login
     * @return true if credentials match, false otherwise
     */
    public boolean authenticate(String accNum, String pin) {
        return this.accountNumber.equals(accNum) && this.pin.equals(pin);
    }

    /**
     * Adds an account to the customer's list of owned accounts.
     * @param account The account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Gets all accounts owned by this customer.
     * @return List of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    // ----- Getters -----

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPIN() {
        return pin;
    }

    // ----- Optional: Setters (if needed for persistence) -----
    // Not required unless loading from database

    // ----- toString for debugging -----
    @Override
    public String toString() {
        return String.format("Customer{%s %s | Acc: %s | Addr: %s}", 
                firstName, surname, accountNumber, address);
    }
}