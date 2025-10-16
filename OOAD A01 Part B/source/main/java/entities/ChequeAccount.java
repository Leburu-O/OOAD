package entities;

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
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return;
        }
        balance -= amount;
        Transaction t = new Transaction("Withdrawal", amount, balance);
        transactionHistory.add(t);
        System.out.printf("Withdrew %.2f BWP. New balance: %.2f BWP\n", amount, balance);
    }

    @Override
    public double applyInterest() {
        // No interest
        return 0.0;
    }

    // Getters
    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}