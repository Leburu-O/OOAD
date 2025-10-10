package services;

import entities.*;

public class BankTeller {

    public SavingsAccount openSavingsAccount(String branch, Customer customer, boolean isCompany) {
        SavingsAccount account = new SavingsAccount(branch, customer, isCompany);
        customer.addAccount(account);
        System.out.println("Savings Account opened: " + account.getAccountNumber());
        return account;
    }

    public InvestmentAccount openInvestmentAccount(String branch, Customer customer, double initialDeposit) {
        InvestmentAccount account = new InvestmentAccount(branch, customer, initialDeposit);
        customer.addAccount(account);
        System.out.println("Investment Account opened: " + account.getAccountNumber());
        return account;
    }

    public ChequeAccount openChequeAccount(String branch, Customer customer, String employerName, String employerAddress) {
        ChequeAccount account = new ChequeAccount(branch, customer, employerName, employerAddress);
        customer.addAccount(account);
        System.out.println("Cheque Account opened: " + account.getAccountNumber());
        return account;
    }
}