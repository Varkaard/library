package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "member already exists")
public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String username) {
        super("Member " + username + " already exists");
    }
}