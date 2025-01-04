package com.honeyboard.api.exception;

public class DuplicateVideoException extends RuntimeException {
    public DuplicateVideoException(String message) {
        super(message);
    }
}
