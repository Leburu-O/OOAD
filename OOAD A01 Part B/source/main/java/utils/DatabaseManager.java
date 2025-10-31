// utils/DatabaseManager.java
package utils;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:banking.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            createTables(conn);
            System.out.println("✅ Database initialized: banking.db");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        // Customers Table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS customers (
                accountNumber TEXT PRIMARY KEY,
                firstName TEXT NOT NULL,
                surname TEXT NOT NULL,
                address TEXT,
                pin TEXT NOT NULL
            );
        """);

        // Accounts Table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS accounts (
                accountNumber TEXT PRIMARY KEY,
                balance REAL NOT NULL,
                branch TEXT,
                customerAccountNumber TEXT,
                type TEXT NOT NULL,
                companyAccount BOOLEAN DEFAULT FALSE,
                employerName TEXT,
                employerAddress TEXT,
                FOREIGN KEY (customerAccountNumber) REFERENCES customers (accountNumber)
            );
        """);

        // Transactions Table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                amount REAL NOT NULL,
                balanceAfter REAL NOT NULL,
                timestamp TEXT NOT NULL,
                accountNumber TEXT,
                FOREIGN KEY (accountNumber) REFERENCES accounts (accountNumber)
            );
        """);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}