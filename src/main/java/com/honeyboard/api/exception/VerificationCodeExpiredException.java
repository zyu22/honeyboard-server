package com.honeyboard.api.exception;

public
class VerificationCodeExpiredException extends RuntimeException {
    public
    VerificationCodeExpiredException(String message) {
        super(message);
    }
}
