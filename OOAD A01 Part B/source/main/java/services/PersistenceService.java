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
     * @return List of loaded customers
     */
    public List<Customer> loadAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerCache.clear();

        // Load all customers first
        loadCustomersFromDB(customers);

        // Then load all accounts and link them to customers
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
                    // Note: Timestamp can be added if needed
                    account.getTransactionHistory().add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to load transactions: " + e.getMessage());
        }
    }

    /**
     * Saves a single customer and all their accounts to the database.
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

            // Save all accounts
            for (Account acc : customer.getAccounts()) {
                saveAccount(acc, customer.getAccountNumber(), conn);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to save customer: " + e.getMessage());
        }
    }

    private void saveAccount(Account account, String customerAccNum, Connection conn) {
        String sql = """
            INSERT OR REPLACE INTO accounts 
            (accountNumber, balance, branch, customerAccountNumber, type, companyAccount, employerName, employerAddress)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        boolean isCompany = false;
        String employerName = null;
        String employerAddress = null;

        if (account instanceof SavingsAccount sa) {
            isCompany = sa.isCompanyAccount();
        } else if (account instanceof ChequeAccount ca) {
            employerName = ca.getEmployerName();
            employerAddress = ca.getEmployerAddress();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setString(3, account.getBranch());
            pstmt.setString(4, customerAccNum);
            pstmt.setString(5, account.getClass().getSimpleName());
            pstmt.setBoolean(6, isCompany);
            pstmt.setString(7, employerName);
            pstmt.setString(8, employerAddress);
            pstmt.executeUpdate();

            // Save all transactions for this account
            saveTransactionsForAccount(account, conn);

        } catch (SQLException e) {
            System.err.println("❌ Failed to save account: " + e.getMessage());
        }
    }

    private void saveTransactionsForAccount(Account account, Connection conn) {
        String sql = """
            INSERT INTO transactions (type, amount, balanceAfter, timestamp, accountNumber)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Transaction t : account.getTransactionHistory()) {
                pstmt.setString(1, t.getType());
                pstmt.setDouble(2, t.getAmount());
                pstmt.setDouble(3, t.getBalanceAfter());
                pstmt.setString(4, t.getTimestamp().toString());
                pstmt.setString(5, account.getAccountNumber());
                pstmt.addBatch(); // Use batch for efficiency
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("❌ Failed to save transactions: " + e.getMessage());
        }
    }

    /**
     * Saves all customers and their data to the database.
     */
    public void saveAllCustomers(List<Customer> customers) {
        for (Customer c : customers) {
            saveCustomer(c);
        }
    }
}