package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "unique identifier modification is forbidden")
public class UniqueIdentifierModificationException extends RuntimeException {
    public UniqueIdentifierModificationException(Object uniqueIdentifier) {
        super("Modification of unique identifer " + uniqueIdentifier + " is forbidden");
    }
}
