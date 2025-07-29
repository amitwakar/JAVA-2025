package com.ap.test;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/organization";
        String dbName = "bank";
        String user = "root";
        String password = "#1Sanikamit";

        Connection conn = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            stmt.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
            stmt.execute("USE " + dbName);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50) UNIQUE NOT NULL,
                    amount DOUBLE CHECK (amount >= 0)
                )
            """);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO accounts (name, amount) VALUES ('Alice', 1000), ('Bob', 1000)");
                System.out.println("Inserted Alice and Bob with ₹1000 each.");
            }

            System.out.print("Enter sender name (Alice/Bob): ");
            String sender = sc.nextLine().trim();

            System.out.print("Enter receiver name (Alice/Bob): ");
            String receiver = sc.nextLine().trim();

            if (sender.equalsIgnoreCase(receiver)) {
                System.out.println("Sender and receiver cannot be the same.");
                return;
            }

            System.out.print("Enter amount to transfer: ");
            if (!sc.hasNextDouble()) {
                System.out.println("Invalid amount input.");
                return;
            }
            double amount = sc.nextDouble();

            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 

            PreparedStatement checkStmt = conn.prepareStatement("SELECT amount FROM accounts WHERE name = ?");

            checkStmt.setString(1, sender);
            ResultSet senderRs = checkStmt.executeQuery();
            if (!senderRs.next()) {
                System.out.println("Sender account not found.");
                return;
            }
            double senderBalance = senderRs.getDouble("amount");

            checkStmt.setString(1, receiver);
            ResultSet receiverRs = checkStmt.executeQuery();
            if (!receiverRs.next()) {
                System.out.println("Receiver account not found.");
                return;
            }

            if (senderBalance < amount) {
                System.out.println("Insufficient balance in sender's account.");
                return;
            }

            PreparedStatement debitStmt = conn.prepareStatement("UPDATE accounts SET amount = amount - ? WHERE name = ?");
            debitStmt.setDouble(1, amount);
            debitStmt.setString(2, sender);
            int debited = debitStmt.executeUpdate();

            PreparedStatement creditStmt = conn.prepareStatement("UPDATE accounts SET amount = amount + ? WHERE name = ?");
            creditStmt.setDouble(1, amount);
            creditStmt.setString(2, receiver);
            int credited = creditStmt.executeUpdate();

            if (debited == 1 && credited == 1) {
                conn.commit();
                System.out.println("\nTransaction Successful!");
            } else {
                conn.rollback();
                System.out.println("\nTransaction Failed. Rolled back.");
                return;
            }

            ResultSet result = stmt.executeQuery("SELECT * FROM accounts");
            System.out.println("\nFinal Balances:");
            while (result.next()) {
                System.out.printf("%s: ₹%.2f\n", result.getString("name"), result.getDouble("amount"));
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Duplicate or invalid account entry.");
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
                System.out.println("\nTransaction failed due to database error. Rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                if (stmt != null) stmt.close();
                sc.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
