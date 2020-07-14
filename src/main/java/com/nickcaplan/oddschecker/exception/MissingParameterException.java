package com.nickcaplan.oddschecker.exception;

/**
 * Indicates that a required parameter has not been provided.
 */
public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String missingParameter) {
        super("The following parameter is required but was not provided: [" + missingParameter + "]");
    }
}
