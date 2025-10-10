// Main.java
import entities.*;
import services.BankTeller;
import services.InterestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Customer> customers = new ArrayList<>();
    private static List<Account> allAccounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();

        while (true) {
            System.out.println("\n=== WELCOME TO BANKING SYSTEM ===");
            System.out.println("1. Customer Login");
            System.out.println("2. Bank Teller: Open Account");
            System.out.println("3. Run Monthly Interest");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> customerLogin();
                case 2 -> openAccountByTeller();
                case 3 -> runMonthlyInterest();
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void initializeData() {
        Customer c1 = new Customer("Olerato", "Leburu", "Gaborone", "ACC100001", "1234");
        Customer c2 = new Customer("Kentsenao", "Baseki", "Francistown", "ACC100002", "5678");
        customers.add(c1);
        customers.add(c2);
    }

    private static void customerLogin() {
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        Customer customer = findCustomer(accNum);
        if (customer == null || !customer.authenticate(accNum, pin)) {
            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("Login successful!\n");

        while (true) {
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Accounts");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> makeDeposit(customer);
                case 2 -> makeWithdrawal(customer);
                case 3 -> viewAccounts(customer);
                case 4 -> viewTransactionHistory(customer);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void makeDeposit(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;
        System.out.print("Enter deposit amount: ");
        double amount = getDoubleInput();
        account.deposit(amount);
    }

    private static void makeWithdrawal(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;
        System.out.print("Enter withdrawal amount: ");
        double amount = getDoubleInput();
        account.withdraw(amount);
    }

    private static void viewAccounts(Customer customer) {
        System.out.println("\nYour Accounts:");
        for (Account a : customer.getAccounts()) {
            System.out.println("  " + a);
        }
    }

    private static void viewTransactionHistory(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;
        System.out.println("\nTransaction History for " + account.getAccountNumber() + ":");
        for (Transaction t : account.getTransactionHistory()) {
            System.out.println("  " + t);
        }
    }

    private static Account selectAccount(Customer customer) {
        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return null;
        }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.printf("%d. %s\n", i+1, accounts.get(i));
        }
        int choice = getIntInput();
        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return accounts.get(choice - 1);
    }

    private static void openAccountByTeller() {
        System.out.print("Enter customer account number: ");
        String accNum = scanner.nextLine();
        Customer customer = findCustomer(accNum);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Account Type: 1=Savings, 2=Investment, 3=Cheque");
        int type = getIntInput();
        BankTeller teller = new BankTeller();
        Account account = null;

        switch (type) {
            case 1:
                System.out.print("Is company account? (1=Yes, 0=No): ");
                boolean isCompany = getIntInput() == 1;
                account = teller.openSavingsAccount("Main Branch", customer, isCompany);
                break;
            case 2:
                System.out.print("Enter initial deposit: ");
                double deposit = getDoubleInput();
                account = teller.openInvestmentAccount("Main Branch", customer, deposit);
                break;
            case 3:
                System.out.print("Employer Name: ");
                String empName = scanner.nextLine();
                System.out.print("Employer Address: ");
                String empAddr = scanner.nextLine();
                account = teller.openChequeAccount("Main Branch", customer, empName, empAddr);
                break;
            default:
                System.out.println("Invalid type.");
        }

        if (account != null) {
            allAccounts.add(account);
        }
    }

    private static void runMonthlyInterest() {
        InterestService service = new InterestService();
        service.processMonthlyInterest(allAccounts);
    }

    private static Customer findCustomer(String accNum) {
        return customers.stream()
                .filter(c -> c.getAccountNumber().equals(accNum))
                .findFirst()
                .orElse(null);
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}