package com.aurionpro.bank;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        AccountOperations[] accounts = {
            new SavingsAccount (5_000),
            new CurrentAccount (15_000),
            new LoanAccount    (50_000)
        };
        String[] names = { "Savings", "Current", "Loan" };

        System.out.println(" Welcome to Tiwari Bank \n");

        while (true) {
            try {
                int acc = chooseAccount(names);
                if (acc == 0) break;  
                AccountOperations chosen = accounts[acc - 1];

                while (true) {
                    int op = chooseOperation();
                    if (op == 4) break; 
                    switch (op) {
                        case 1 -> performDeposit(chosen);
                        case 2 -> performWithdraw(chosen);
                        case 3 -> System.out.printf("Balance: ₹%.2f%n%n", chosen.checkBalance());
                    }
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input – numeric values only.\n");
                in.nextLine(); 
            }
        }
        System.out.println("Thank you for banking with us!");
    }

    private static int chooseAccount(String[] names){
        System.out.println("Select Account:");
        for (int i=0;i<names.length;i++)
            System.out.printf("  %d. %s%n", i+1, names[i]);
        System.out.println("  0. Exit");
        System.out.print("Choice: ");
        int c = in.nextInt(); in.nextLine();
        System.out.println();
        return c;
    }

    private static int chooseOperation(){
        System.out.println(" 1. Deposit");
        System.out.println(" 2. Withdraw");
        System.out.println(" 3. Check Balance");
        System.out.println(" 4. Back");
        System.out.print  ("Select operation: ");
        int op = in.nextInt(); in.nextLine();
        System.out.println();
        return op;
    }

    private static void performDeposit(AccountOperations acc){
        System.out.print("Enter amount to deposit: ");
        double amt = in.nextDouble(); in.nextLine();
        try {
            acc.deposit(amt);                      
        } catch (InvalidAmountException | OperationNotAllowedException e) {
            System.out.println("❌ " + e.getMessage());
        }
        System.out.println();
    }

    private static void performWithdraw(AccountOperations acc){
        System.out.print("Enter amount to withdraw/disburse: ");
        double amt = in.nextDouble(); in.nextLine();
        try {
            acc.withdraw(amt);                      
        } catch (InvalidAmountException | InsufficientFundsException |
                 OperationNotAllowedException e) {
            System.out.println("❌ " + e.getMessage());
        }
        System.out.println();

}}
