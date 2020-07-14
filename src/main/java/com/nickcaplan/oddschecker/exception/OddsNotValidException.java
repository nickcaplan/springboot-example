package com.nickcaplan.oddschecker.exception;

/**
 * Indicates that the odds provided are not in a valid format.
 */
public class OddsNotValidException extends RuntimeException {
    public OddsNotValidException() {
        super("Invalid format of Odds");
    }
}
