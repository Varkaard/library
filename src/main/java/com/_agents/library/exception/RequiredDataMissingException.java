package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "required data missing")
public class RequiredDataMissingException extends RuntimeException {
    public RequiredDataMissingException(String attribute) {
        super("Required data from " + attribute + " is missing");
    }
}