package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "maximum loans reached")
public class MaximumLoansReachedException extends RuntimeException {
    public MaximumLoansReachedException(int amount){
        super("Maximum loans of " + amount + " reached");
    }
}