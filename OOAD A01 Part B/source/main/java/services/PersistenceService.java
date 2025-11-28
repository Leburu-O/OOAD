// services/PersistenceService.java
package services;

import entities.*;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class PersistenceService {
    private final Map<String, Customer> customerCache = new ConcurrentHashMap<>();

    /**
     * Loads all customers and their accounts from the database.
     */
    public List<Customer> loadAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerCache.clear();

        // Load customers
        loadCustomersFromDB(customers);

        // Load accounts and link to customers
        loadAccountsAndTransactions();

        return customers;
    }

    private void loadCustomersFromDB(List<Customer> customers) {
        String sql = "SELECT * FROM customers";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("firstName"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("accountNumber"),
                        rs.getString("pin")
                );
                customers.add(c);
                customerCache.put(c.getAccountNumber(), c);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to load customers: " + e.getMessage());
        }
    }

    private void loadAccountsAndTransactions() {
        String sql = "SELECT * FROM accounts ORDER BY customerAccountNumber";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String customerAccNum = rs.getString("customerAccountNumber");
                Customer owner = customerCache.get(customerAccNum);
                if (owner == null) continue;

                Account account = createAccountFromResultSet(rs);
                owner.addAccount(account);

                loadTransactionsForAccount(account);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to load accounts: " + e.getMessage());
        }
    }

    private Account createAccountFromResultSet(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String branch = rs.getString("branch");
        double balance = rs.getDouble("balance");
        String accNum = rs.getString("accountNumber");

        return switch (type) {
            case "SavingsAccount" -> {
                boolean isCompany = rs.getBoolean("companyAccount");
                SavingsAccount sa = new SavingsAccount(branch, null, isCompany);
                sa.setBalance(balance);
                sa.setAccountNumber(accNum);
                yield sa;
            }
            case "InvestmentAccount" -> {
                InvestmentAccount ia = new InvestmentAccount(branch, null, balance);
                ia.setAccountNumber(accNum);
                yield ia;
            }
            case "ChequeAccount" -> {
                String empName = rs.getString("employerName");
                String empAddr = rs.getString("employerAddress");
                ChequeAccount ca = new ChequeAccount(branch, null, empName, empAddr);
                ca.setBalance(balance);
                ca.setAccountNumber(accNum);
                yield ca;
            }
            default -> throw new IllegalArgumentException("Unknown account type: " + type);
        };
    }

    private void loadTransactionsForAccount(Account account) {
        String sql = "SELECT * FROM transactions WHERE accountNumber = ? ORDER BY timestamp";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountNumber());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getDouble("balanceAfter")
                    );
                    account.getTransactionHistory().add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to load transactions: " + e.getMessage());
        }
    }

    /**
     * Saves a single transaction to the database immediately.
     */
    public void saveTransaction(Transaction transaction, String accountNumber) {
        String sql = """
            INSERT INTO transactions (type, amount, balanceAfter, timestamp, accountNumber)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getType());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setDouble(3, transaction.getBalanceAfter());
            pstmt.setString(4, transaction.getTimestamp().toString());
            pstmt.setString(5, accountNumber);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Failed to save transaction: " + e.getMessage());
        }
    }

    /**
     * Saves a customer and all their accounts (but not individual transactions — those are saved immediately).
     */
    public void saveCustomer(Customer customer) {
        String sql = "INSERT OR REPLACE INTO customers (accountNumber, firstName, surname, address, pin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getAccountNumber());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPIN());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Failed to save customer: " + e.getMessage());
        }
    }

    /**
     * Saves all customers at shutdown.
     */
    public void saveAllCustomers(List<Customer> customers) {
        for (Customer c : customers) {
            saveCustomer(c);
        }
    }
}