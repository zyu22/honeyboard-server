package com.honeyboard.api.exception;

public
class TokenExpiredException extends RuntimeException {
    public
    TokenExpiredException(String message) {
        super(message);
    }
}
