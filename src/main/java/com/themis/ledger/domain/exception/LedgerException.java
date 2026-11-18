package com.themis.ledger.domain.exception;

public sealed class LedgerException extends RuntimeException
        permits UnbalancedJournalException,
                AccountNotFoundException,
                CurrencyMismatchException,
                InsufficientFundsException{

    public  LedgerException(String message){
        super(message);
    }
}
