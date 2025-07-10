package com.aurionpro.bank;

class CurrentAccount implements AccountOperations {
    private double balance;
    public CurrentAccount(double opening){ balance = opening; }

    @Override public void deposit(double amt) {
        validatePositive(amt);
        balance += amt;
        System.out.printf("Current ➜ Deposited ₹%.2f%n", amt);
    }
    @Override public void withdraw(double amt) {
        validatePositive(amt);
        if (amt > balance)
            throw new InsufficientFundsException("Insufficient funds. Balance ₹" + balance);
        balance -= amt;
        System.out.printf("Current ➜ Withdrew ₹%.2f%n", amt);
    }
    @Override public double checkBalance(){ return balance; }

    private void validatePositive(double amt){
        if (amt <= 0) throw new InvalidAmountException("Amount must be > 0");
    }
}