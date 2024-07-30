package com._agents.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "related object still exists")
public class RelatedObjectExistsException extends RuntimeException {
        public RelatedObjectExistsException(String object, Long id) {
            super("Related object " +object + " with id " + id + " still exists.");
        }
}
