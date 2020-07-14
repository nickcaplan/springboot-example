package com.nickcaplan.oddschecker.advice;

import com.nickcaplan.oddschecker.exception.OddsNotValidException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles exception mapping for {@link OddsNotValidException}.
 */
@ControllerAdvice
public class OddsNotValidAdvice
{
    @ResponseBody
    @ExceptionHandler(OddsNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String oddsNotValid(OddsNotValidException ex) {
        return ex.getMessage();
    }
}
