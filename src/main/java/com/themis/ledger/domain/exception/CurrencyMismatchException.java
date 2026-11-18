package com.themis.ledger.domain.exception;

import java.util.UUID;

public final class CurrencyMismatchException extends LedgerException{
    public CurrencyMismatchException(UUID id, String expected, String actual){
        super("Currency mismatch for account " + id + " : expected " + expected + " but found " + actual);
    }
}
