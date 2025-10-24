// controllers/InterestProcessingController.java
package controllers;

import entities.Account;
import services.InterestService;

import java.util.List;

/**
 * Controller for processing monthly interest.
 * Fully automated, uses polymorphism to apply correct rates.
 */
public class InterestProcessingController {

    private final InterestService interestService;
    private final List<Account> allAccounts;

    public InterestProcessingController(InterestService interestService, List<Account> allAccounts) {
        this.interestService = interestService;
        this.allAccounts = allAccounts;
    }

    /**
     * Processes monthly interest for all eligible accounts.
     * Logs each credit as a transaction ("Interest Credited").
     */
    public void processMonthlyInterest() {
        interestService.processMonthlyInterest(allAccounts);
    }

    /**
     * Gets total interest paid this month.
     * @return Sum of all interest credited
     */
    public double getTotalInterestPaid() {
        return allAccounts.stream()
                .mapToDouble(Account::applyInterest)
                .sum();
    }
}