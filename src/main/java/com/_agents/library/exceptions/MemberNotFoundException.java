package com._agents.library.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String username) {
        super("Could not find member " + username);
    }
}
