package entities;

public class InvestmentAccount extends Account {
    public static final double INTEREST_RATE = 0.05; // 5% monthly
    public static final double MIN_DEPOSIT = 500.00;

    public InvestmentAccount(String branch, Customer customer, double initialDeposit) {
        super(branch, customer);
        if (initialDeposit < MIN_DEPOSIT) {
            throw new IllegalArgumentException(
                "Investment account requires minimum deposit of BWP" + MIN_DEPOSIT);
        }
        deposit(initialDeposit);
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
        double interest = balance * INTEREST_RATE;
        logInterest(interest);
        return interest;
    }
}