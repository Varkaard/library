package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "author not found")
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id){
        super("Could not find author " + id);
    }
}
