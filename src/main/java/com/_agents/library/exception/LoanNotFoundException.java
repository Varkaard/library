package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "loan not found")
public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long id){
        super("Could not find loan " + id);
    }
}
