package com.themis.ledger.domain.exception;

public final class UnbalancedJournalException extends LedgerException{
    public UnbalancedJournalException(String message){
        super(message);
    }
}
