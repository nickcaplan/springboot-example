package com.nickcaplan.oddschecker.exception;

/**
 * Indicates that no bet could be found for a given id.
 */
public class BetNotFoundException extends RuntimeException {
    public BetNotFoundException() {
        super("Bet not found for given ID");
    }
}
