package com.nickcaplan.oddschecker.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Handles exception mapping for {@link MethodArgumentTypeMismatchException}.
 */
@ControllerAdvice
public class MethodArgumentTypeMismatchAdvice {

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String methodMismatch(MethodArgumentTypeMismatchException ex) {
        return "Value provided: [" + ex.getValue() + "] for: [" + ex.getName() + "] must be of type: [" + ex.getRequiredType() + "]";
    }
}
