package com.dws.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateTransactionIdException extends ChallengeAppException {
    public DuplicateTransactionIdException(String code, String message) {
        super(code, message);
    }
}
