// entities/InvestmentAccount.java
package entities;

import services.PersistenceService;

public class InvestmentAccount extends Account {
    public static final double INTEREST_RATE = 0.05; // 5% monthly

    public InvestmentAccount(String branch, Customer customer, double initialDeposit) {
        super(branch, customer);
        if (initialDeposit < 500.0) {
            throw new IllegalArgumentException("Investment account requires minimum deposit of BWP 500.00.");
        }
        deposit(initialDeposit); // Uses safe deposit → auto-saved
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
        double interest = balance * INTEREST_RATE;
        if (interest > 0) {
            logInterest(interest);
        }
        return interest;
    }
}