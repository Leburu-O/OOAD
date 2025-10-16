package entities;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstName;
    private String surname;
    private String address;
    private String accountNumber; // used as username
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

    public boolean authenticate(String accNum, String pin) {
        return this.accountNumber.equals(accNum) && this.pin.equals(pin);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getAccountNumber() { return accountNumber; }

    @Override
    public String toString() {
        return String.format("Customer{%s %s | Addr: %s}", firstName, surname, address);
    }
}