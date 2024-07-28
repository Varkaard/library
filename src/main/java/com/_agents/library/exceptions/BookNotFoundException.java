package com._agents.library.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id){
        super("Could not find book " + id);
    }
}
