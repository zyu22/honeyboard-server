package com.honeyboard.api.exception;

public
class DuplicateTeamMemberException extends RuntimeException {
    public
    DuplicateTeamMemberException(String message) {
        super(message);
    }
}
