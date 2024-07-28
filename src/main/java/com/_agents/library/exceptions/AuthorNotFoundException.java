package com._agents.library.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id){
        super("Could not find author " + id);
    }
}
