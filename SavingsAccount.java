package com.aurionpro.bank;

class SavingsAccount implements AccountOperations {
    private static final double MIN_BALANCE     = 1_000.0;
    private static final int    MAX_WITHDRAWALS = 3;
    private double balance;
    private int    withdrawalsMade;

    public SavingsAccount(double opening) { balance = opening; }

    @Override public void deposit(double amt) {
        validatePositive(amt);
        balance += amt;
        System.out.printf("Savings ➜ Deposited ₹%.2f%n", amt);
    }

    @Override public void withdraw(double amt) {
        validatePositive(amt);

        if (withdrawalsMade >= MAX_WITHDRAWALS)
            throw new OperationNotAllowedException("Savings limit reached (" + MAX_WITHDRAWALS + " withdrawals).");

        if (balance - amt < MIN_BALANCE)
            throw new InsufficientFundsException("Must keep minimum balance ₹" + MIN_BALANCE);

        balance         -= amt;
        withdrawalsMade += 1;
        System.out.printf("Savings ➜ Withdrew ₹%.2f%n", amt);
    }

    @Override public double checkBalance() { return balance; }

    private void validatePositive(double amt) {
        if (amt <= 0) throw new InvalidAmountException("Amount must be > 0");
    }
}