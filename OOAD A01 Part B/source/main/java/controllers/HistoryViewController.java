// controllers/HistoryViewController.java
package controllers;

import entities.Account;
import entities.Transaction;

import java.util.List;

/**
 * Controller for viewing transaction history.
 * Retrieves and formats transaction data for UI display.
 */
public class HistoryViewController {

    private Account selectedAccount;

    public HistoryViewController() {
        // No dependencies needed — bound at runtime
    }

    /**
     * Sets the current account to view.
     */
    public void setAccount(Account account) {
        this.selectedAccount = account;
    }

    /**
     * Gets the list of transactions for the selected account.
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory() {
        if (selectedAccount == null) {
            throw new IllegalStateException("No account selected.");
        }
        return selectedAccount.getTransactionHistory();
    }

    /**
     * Gets a formatted string representation of all transactions.
     * @return Formatted transaction log
     */
    public String getFormattedHistory() {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : getTransactionHistory()) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the currently selected account.
     * @return The account being viewed
     */
    public Account getSelectedAccount() {
        return selectedAccount;
    }
}