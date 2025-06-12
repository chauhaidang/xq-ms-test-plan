package com.xq.testplan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequirementAlreadyExistsException extends RuntimeException {
    public RequirementAlreadyExistsException(String message) {
        super(message);
    }
}
