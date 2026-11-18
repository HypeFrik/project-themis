package com.themis.ledger.domain.exception;

import java.util.UUID;

public final class AccountNotFoundException extends LedgerException{
    public AccountNotFoundException(UUID id){
        super("Account " + id + " Not Found");
    }
}
