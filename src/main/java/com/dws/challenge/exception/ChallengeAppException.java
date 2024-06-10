package com.dws.challenge.exception;

import lombok.Getter;

@Getter
public class ChallengeAppException extends RuntimeException {
    private final String code;
    private final String message;

    public ChallengeAppException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
