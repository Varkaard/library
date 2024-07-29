package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "book not found")
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id){
        super("Could not find book " + id);
    }
}
