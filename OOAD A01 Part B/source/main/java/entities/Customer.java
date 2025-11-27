// entities/Customer.java
package entities;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String firstName;
    private final String surname;
    private final String address;
    private final String accountNumber;
    private String pin;
    private final List<Account> accounts;

    // Constructor
    public Customer(String firstName, String surname, String address, String accountNumber, String pin) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    // Authentication
    public boolean authenticate(String accNum, String pin) {
        return this.accountNumber.equals(accNum) && this.pin.equals(pin);
    }

    // Account management
    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    // ----- Getters -----
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getAccountNumber() { return accountNumber; }
    public String getPIN() { return pin; }

    // ----- Setters -----

    /**
     * Updates the customer's PIN.
     * @param pin New 4-digit PIN
     * @throws IllegalArgumentException if PIN is not exactly 4 digits
     */
    public void setPIN(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        this.pin = pin;
    }

    // ----- toString -----
    @Override
    public String toString() {
        return String.format("Customer{%s %s | Acc: %s}", firstName, surname, accountNumber);
    }
}