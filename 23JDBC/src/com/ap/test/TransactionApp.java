package com.ap.test;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TransactionApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/organization";
        String user = "root";
        String password = "nhi bataunga";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            Scanner sc = new Scanner(System.in);

            while (true) {
                try {
                    System.out.println("\n===== Bank Transaction Menu =====");
                    System.out.println("1. Check Account Balance");
                    System.out.println("2. Transfer Money");
                    System.out.println("3. Deposit Money");
                    System.out.println("4. Withdraw Money");
                    System.out.println("5. Add New Account");
                    System.out.println("6. Delete Account");
                    System.out.println("7. Display All Accounts");
                    System.out.println("8. Show All Transaction History");
                    System.out.println("9. Show Transaction History for Specific Account");
                    System.out.println("10. Exit");

                    System.out.print("Enter your choice: ");
                    int choice = sc.nextInt();

                    switch (choice) {
                        case 1 -> checkBalance(conn, sc);
                        case 2 -> transferAmount(conn, sc);
                        case 3 -> depositAmount(conn, sc);
                        case 4 -> withdrawAmount(conn, sc);
                        case 5 -> addAccount(conn, sc);
                        case 6 -> deleteAccount(conn, sc);
                        case 7 -> displayAccounts(conn);
                        case 8 -> showAllTransactionHistory(conn);
                        case 9 -> showAccountTransactionHistory(conn, sc);
                        case 10 -> {
                            System.out.println("Exiting program");
                            return;
                        }
                        default -> System.out.println("Invalid choice");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number");
                    sc.nextLine(); // clear invalid input
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static boolean accountExists(Connection conn, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    static double getBalance(Connection conn, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getDouble("balance");
        return -1;
    }

    static void checkBalance(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Account ID: ");
            int id = sc.nextInt();
            if (!accountExists(conn, id)) {
                System.out.println("Account not found");
                return;
            }
            PreparedStatement ps = conn.prepareStatement("SELECT name, balance FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Balance: Rs" + rs.getDouble("balance"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void transferAmount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Sender ID: ");
            int fromId = sc.nextInt();
            System.out.print("Enter Receiver ID: ");
            int toId = sc.nextInt();
            System.out.print("Enter Amount to Transfer: Rs");
            double amount = sc.nextDouble();

            if (fromId == toId || amount <= 0) {
                System.out.println("Invalid transaction");
                return;
            }

            if (!accountExists(conn, fromId) || !accountExists(conn, toId)) {
                System.out.println("Sender or Receiver account not found");
                return;
            }

            double balance = getBalance(conn, fromId);
            if (balance < amount) {
                System.out.println("Insufficient balance");
                return;
            }

            PreparedStatement debit = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
            debit.setDouble(1, amount);
            debit.setInt(2, fromId);
            debit.executeUpdate();

            PreparedStatement credit = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
            credit.setDouble(1, amount);
            credit.setInt(2, toId);
            credit.executeUpdate();

            PreparedStatement log = conn.prepareStatement("INSERT INTO transaction_history(type, sender_id, receiver_id, amount) VALUES (?, ?, ?, ?)");
            log.setString(1, "Transfer");
            log.setInt(2, fromId);
            log.setInt(3, toId);
            log.setDouble(4, amount);
            log.executeUpdate();

            conn.commit();
            System.out.println("Rs" + amount + " transferred from ID " + fromId + " to ID " + toId);
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    static void depositAmount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Account ID: ");
            int id = sc.nextInt();
            System.out.print("Enter Amount to Deposit: Rs");
            double amount = sc.nextDouble();

            if (!accountExists(conn, id) || amount <= 0) {
                System.out.println("Invalid deposit");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
            ps.setDouble(1, amount);
            ps.setInt(2, id);
            ps.executeUpdate();

            PreparedStatement log = conn.prepareStatement("INSERT INTO transaction_history(type, receiver_id, amount) VALUES (?, ?, ?)");
            log.setString(1, "Deposit");
            log.setInt(2, id);
            log.setDouble(3, amount);
            log.executeUpdate();

            conn.commit();
            System.out.println("Rs" + amount + " deposited to ID " + id);
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    static void withdrawAmount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Account ID: ");
            int id = sc.nextInt();
            System.out.print("Enter Amount to Withdraw: Rs");
            double amount = sc.nextDouble();

            if (!accountExists(conn, id) || amount <= 0) {
                System.out.println("Invalid withdrawal");
                return;
            }

            double balance = getBalance(conn, id);
            if (balance < amount) {
                System.out.println("Insufficient balance");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
            ps.setDouble(1, amount);
            ps.setInt(2, id);
            ps.executeUpdate();

            PreparedStatement log = conn.prepareStatement("INSERT INTO transaction_history(type, sender_id, amount) VALUES (?, ?, ?)");
            log.setString(1, "Withdraw");
            log.setInt(2, id);
            log.setDouble(3, amount);
            log.executeUpdate();

            conn.commit();
            System.out.println("Rs" + amount + " withdrawn from ID " + id);
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }

    static void addAccount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter New Account ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Opening Balance: Rs");
            double bal = sc.nextDouble();

            if (accountExists(conn, id)) {
                System.out.println("Account already exists");
                return;
            }

            if (bal < 0) {
                System.out.println("Opening balance cannot be negative");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts(id, name, balance) VALUES (?, ?, ?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, bal);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Account created successfully");
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Account creation failed: " + e.getMessage());
        }
    }

    static void deleteAccount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Account ID to delete: ");
            int id = sc.nextInt();
            if (!accountExists(conn, id)) {
                System.out.println("Account not found");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Account deleted successfully");
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    static void displayAccounts(Connection conn) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM accounts");
            System.out.println("ID\tName\tBalance");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\tRs" + rs.getDouble("balance"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void showAllTransactionHistory(Connection conn) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM transaction_history ORDER BY timestamp DESC");

            while (rs.next()) {
                String type = rs.getString("type");
                int sender = rs.getInt("sender_id");
                int receiver = rs.getInt("receiver_id");
                double amt = rs.getDouble("amount");
                String time = rs.getString("timestamp");

                switch (type) {
                    case "Transfer" -> System.out.println("Rs" + amt + " transferred from ID " + sender + " to ID " + receiver + " on " + time);
                    case "Deposit" -> System.out.println("Rs" + amt + " deposited to ID " + receiver + " on " + time);
                    case "Withdraw" -> System.out.println("Rs" + amt + " withdrawn from ID " + sender + " on " + time);
                }
            }
        } catch (Exception e) {
            System.out.println("History fetch error: " + e.getMessage());
        }
    }

    static void showAccountTransactionHistory(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter Account ID to view history: ");
            int id = sc.nextInt();

            if (!accountExists(conn, id)) {
                System.out.println("Account not found");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM transaction_history WHERE sender_id = ? OR receiver_id = ? ORDER BY timestamp DESC");
            ps.setInt(1, id);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                int sender = rs.getInt("sender_id");
                int receiver = rs.getInt("receiver_id");
                double amt = rs.getDouble("amount");
                String time = rs.getString("timestamp");

                switch (type) {
                    case "Transfer" -> System.out.println("Rs" + amt + " transferred from ID " + sender + " to ID " + receiver + " on " + time);
                    case "Deposit" -> System.out.println("Rs" + amt + " deposited to ID " + receiver + " on " + time);
                    case "Withdraw" -> System.out.println("Rs" + amt + " withdrawn from ID " + sender + " on " + time);
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching account history: " + e.getMessage());
        }
    }
}
