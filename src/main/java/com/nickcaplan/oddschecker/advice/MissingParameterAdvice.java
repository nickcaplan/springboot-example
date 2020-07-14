package com.nickcaplan.oddschecker.advice;

import com.nickcaplan.oddschecker.exception.MissingParameterException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles exception mapping for {@link MissingParameterException}.
 */
@ControllerAdvice
public class MissingParameterAdvice
{
    @ResponseBody
    @ExceptionHandler(MissingParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String missingParameter(MissingParameterException ex) {
        return ex.getMessage();
    }
}
