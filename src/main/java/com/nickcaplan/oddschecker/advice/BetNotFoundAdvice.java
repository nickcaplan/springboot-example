package com.nickcaplan.oddschecker.advice;

import com.nickcaplan.oddschecker.exception.BetNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles exception mapping for {@link BetNotFoundException}.
 */
@ControllerAdvice
public class BetNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(BetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String betNotFound(BetNotFoundException ex) {
        return ex.getMessage();
    }
}
