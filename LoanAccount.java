package com.aurionpro.bank;

class LoanAccount implements AccountOperations {
    private double outstanding;
    public LoanAccount(double principal){ outstanding = principal; }

    @Override public void deposit(double amt) {
        throw new OperationNotAllowedException("Deposits not allowed in Loan Account. Use repayment portal.");
    }

    @Override public void withdraw(double amt) {
        validatePositive(amt);
        outstanding += amt;
        System.out.printf("Loan ➜ Disbursed ₹%.2f (Outstanding now ₹%.2f)%n", amt, outstanding);
    }

    @Override public double checkBalance(){ return outstanding; }

    private void validatePositive(double amt){
        if (amt <= 0) throw new InvalidAmountException("Amount must be > 0");
    }
}
