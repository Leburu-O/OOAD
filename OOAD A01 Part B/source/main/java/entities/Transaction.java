package entities;

import java.time.LocalDateTime;

public class Transaction {
    private String type; // "Deposit", "Withdrawal", "Interest Credited"
    private double amount;
    private LocalDateTime timestamp;
    private double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f BWP | Balance: %.2f BWP",
                timestamp.toString(), type, amount, balanceAfter);
    }

    // Getters
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getBalanceAfter() { return balanceAfter; }
}