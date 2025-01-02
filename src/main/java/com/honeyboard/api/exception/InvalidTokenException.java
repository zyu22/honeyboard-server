package com.honeyboard.api.exception;

public
class InvalidTokenException extends RuntimeException {
    public
    InvalidTokenException(String message) {
        super(message);
    }
}
