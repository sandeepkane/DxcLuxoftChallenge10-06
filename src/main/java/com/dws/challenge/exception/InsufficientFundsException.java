package com.dws.challenge.exception;

public class InsufficientFundsException extends ChallengeAppException {
    public InsufficientFundsException(String code, String message) {
        super(code, message);
    }
}
