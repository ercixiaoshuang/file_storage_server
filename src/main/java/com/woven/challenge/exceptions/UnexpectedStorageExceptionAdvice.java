package com.woven.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.woven.challenge.message.ResponseMessage;

public class UnexpectedStorageExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnexpectedStorageException.class)
    public ResponseEntity<ResponseMessage> handleUnexpectedStorage(UnexpectedStorageException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(exc.getMessage()));
    }
}
