package com._agents.library.exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long id){
        super("Could not find loan " + id);
    }
}
