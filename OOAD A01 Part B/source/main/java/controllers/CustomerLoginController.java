// controllers/CustomerLoginController.java
package controllers;

import entities.Customer;

import java.util.List;

/**
 * Controller for handling customer login.
 * Validates credentials and authenticates user access.
 */
public class CustomerLoginController {

    private final List<Customer> customers;

    public CustomerLoginController(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * Attempts to authenticate a customer.
     * @param accountNumber The account number provided
     * @param pin The PIN provided
     * @return Authenticated Customer if successful, null otherwise
     */
    public Customer login(String accountNumber, String pin) {
        if (accountNumber == null || pin == null || accountNumber.trim().isEmpty() || pin.trim().isEmpty()) {
            return null;
        }

        return customers.stream()
                .filter(c -> c.getAccountNumber().equals(accountNumber.trim()))
                .filter(c -> c.authenticate(accountNumber.trim(), pin.trim()))
                .findFirst()
                .orElse(null);
    }
}