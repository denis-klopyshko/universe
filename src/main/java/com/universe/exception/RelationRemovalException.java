package com.universe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RelationRemovalException extends RuntimeException {
    public RelationRemovalException(String message) {
        super(message);
    }

    public RelationRemovalException(String message, Throwable cause) {
        super(message, cause);
    }
}
