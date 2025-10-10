package entities;

public class SavingsAccount extends Account {
    public static final double INDIVIDUAL_RATE = 0.00025; // 0.025% monthly
    public static final double COMPANY_RATE = 0.00075;   // 0.075% monthly
    private boolean isCompanyAccount;

    public SavingsAccount(String branch, Customer customer, boolean isCompanyAccount) {
        super(branch, customer);
        this.isCompanyAccount = isCompanyAccount;
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawal not allowed from Savings Account.");
    }

    @Override
    public double applyInterest() {
        double rate = isCompanyAccount ? COMPANY_RATE : INDIVIDUAL_RATE;
        double interest = balance * rate;
        logInterest(interest);
        return interest;
    }

    public boolean isCompanyAccount() {
        return isCompanyAccount;
    }
}