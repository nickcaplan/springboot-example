package com.nickcaplan.oddschecker.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles exception mapping for {@link HttpMessageNotReadableAdvice}.
 */
@ControllerAdvice
public class HttpMessageNotReadableAdvice {
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidBetId(HttpMessageNotReadableException ex) {
        return "Invalid Bet ID supplied";
    }
}
