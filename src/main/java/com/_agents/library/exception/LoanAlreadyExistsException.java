package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "loan already exists")
public class LoanAlreadyExistsException extends RuntimeException {
    public LoanAlreadyExistsException(Long id) {
        super("Loan " + id + " already exists");
    }
}