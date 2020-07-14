package com.nickcaplan.oddschecker.validator;

import org.springframework.util.StringUtils;

/**
 * Validates that odds are in the given format.
 */
public class OddsValidator {
    private static final String STARTING_PRICE = "SP";

    /**
     * Checks whether the provided odds are in the correct format.
     *
     * @param oddsToValidate The odds to validate.
     * @return true if the odds are in the correct format.
     */
    public static boolean isValid(String oddsToValidate) {
        if (StringUtils.isEmpty(oddsToValidate)) {
            return false;
        }

        if (STARTING_PRICE.equals(oddsToValidate)) {
            return true;
        }

        String[] tokens = oddsToValidate.split("/");
        if (tokens.length != 2) {
            return false;
        }

        // Check tokens are both numbers greater than 0
        try {
            int firstToken = Integer.parseInt(tokens[0]);
            if (firstToken < 1) {
                return false;
            }
            int secondToken = Integer.parseInt(tokens[1]);
            if (secondToken < 1) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
