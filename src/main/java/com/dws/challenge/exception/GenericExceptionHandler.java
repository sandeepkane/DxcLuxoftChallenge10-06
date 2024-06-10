package com.dws.challenge.exception;

import com.dws.challenge.domain.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({InsufficientFundsException.class, DuplicateTransactionIdException.class, ChallengeAppException.class})
    public ResponseEntity<Object> handleException(ChallengeAppException exception, WebRequest request) {
        String code = exception.getCode();
        String message = exception.getMessage();
        Response response = new Response(code, message);
        return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
