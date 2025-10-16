package services;

import entities.Account;

import java.util.List;

public class InterestService {

    public void processMonthlyInterest(List<Account> allAccounts) {
        System.out.println("\n--- Processing Monthly Interest ---");
        for (Account account : allAccounts) {
            account.applyInterest();
        }
        System.out.println("--- Interest Processing Complete ---\n");
    }
}