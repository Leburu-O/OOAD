// entities/ChequeAccount.java
package entities;

import services.PersistenceService;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String branch, Customer customer, String employerName, String employerAddress) {
        super(branch, customer);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (amount > balance) throw new IllegalStateException("Insufficient funds.");

        balance -= amount;
        Transaction t = new Transaction("Withdrawal", amount, balance);
        transactionHistory.add(t);

        // ✅ Save withdrawal immediately
        new PersistenceService().saveTransaction(t, this.accountNumber);
    }

    @Override
    public double applyInterest() {
        return 0.0; // No interest
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}