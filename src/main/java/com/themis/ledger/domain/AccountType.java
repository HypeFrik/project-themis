package com.themis.ledger.domain;

public enum AccountType {
    ASSET(true),
    EXPENSE(true),
    LIABILITY(false),
    EQUITY(false),
    REVENUE(false);

    private final boolean debit;
    AccountType(boolean debit){
        this.debit = debit;
    }

    public long signedDelta(Direction dir, long amount){
        boolean addBalance = (debit && dir == Direction.DEBIT) || (!debit && dir == Direction.CREDIT);
        return addBalance ? amount : Math.negateExact(amount);
    }
}
