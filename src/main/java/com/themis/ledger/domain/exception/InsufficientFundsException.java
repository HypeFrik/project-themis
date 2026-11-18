package com.themis.ledger.domain.exception;

import java.util.UUID;

public final class InsufficientFundsException extends LedgerException {
    public InsufficientFundsException(UUID id, long balance, long delta){
        super("Insufficient funds for " + id + " : balance = " + balance + ", delta = " + delta);
    }
}
