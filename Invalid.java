package com.aurionpro.bank;

@SuppressWarnings("serial")
class InvalidAmountException       extends RuntimeException { public InvalidAmountException      (String m){ super(m);} }
@SuppressWarnings("serial")
class InsufficientFundsException   extends RuntimeException { public InsufficientFundsException  (String m){ super(m);} }
@SuppressWarnings("serial")
class OperationNotAllowedException extends RuntimeException { public OperationNotAllowedException(String m){ super(m);} }

