package com.nickcaplan.oddschecker.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for the OddsValidator")
public class OddsValidatorTest {

    @DisplayName("SP is a valid odds value")
    @Test
    public void isValid_validSp_returnsTrue() {
        assertTrue(OddsValidator.isValid("SP"));
    }

    @DisplayName("SP/1 is not a valid odds value")
    @Test
    public void isValid_spTo1_returnsFalse() {
        assertFalse(OddsValidator.isValid("SP/1"));
    }

    @DisplayName("2/X is not a valid odds value")
    @Test
    public void isValid_2ToX_returnsFalse() {
        assertFalse(OddsValidator.isValid("2/X"));
    }

    @DisplayName("sp is not a valid odds value")
    @Test
    public void isValid_invalidSp_returnsTrue() {
        assertFalse(OddsValidator.isValid("sp"));
    }

    @DisplayName("XXX is not a valid odds value")
    @Test
    public void isValid_XXX_returnsTrue() {
        assertFalse(OddsValidator.isValid("XXX"));
    }

    @DisplayName("2/1 is a valid odds value")
    @Test
    public void isValid_2To1_returnsTrue() {
        assertTrue(OddsValidator.isValid("2/1"));
    }

    @DisplayName("2/-1 is not a valid odds value")
    @Test
    public void isValid_2ToMinus1_returnsFalse() {
        assertFalse(OddsValidator.isValid("2/-1"));
    }

    @DisplayName("-2/1 is not a valid odds value")
    @Test
    public void isValid_Minus2To1_returnsFalse() {
        assertFalse(OddsValidator.isValid("-2/1"));
    }

    @DisplayName("2/0 is not a valid odds value")
    @Test
    public void isValid_2To0_returnsFalse() {
        assertFalse(OddsValidator.isValid("2/0"));
    }

    @DisplayName("0/2 is not a valid odds value")
    @Test
    public void isValid_0To2_returnsFalse() {
        assertFalse(OddsValidator.isValid("2/0"));
    }

    @DisplayName("2/1/1 is not a valid odds value")
    @Test
    public void isValid_2To1to1_returnsFalse() {
        assertFalse(OddsValidator.isValid("2/1/1"));
    }

    @DisplayName("2*1 is not a valid odds value")
    @Test
    public void isValid_2Star1_returnsFalse() {
        assertFalse(OddsValidator.isValid("2*1"));
    }

    @DisplayName("Null value is not a valid odds value")
    @Test
    public void isValid_nullValue_returnsFalse() {
        assertFalse(OddsValidator.isValid(null));
    }

    @DisplayName("Blank string is not a valid odds value")
    @Test
    public void isValid_blankString_returnsFalse() {
        assertFalse(OddsValidator.isValid("  "));
    }

    @DisplayName("Empty string is not a valid odds value")
    @Test
    public void isValid_emptyString_returnsFalse()
    {
        assertFalse(OddsValidator.isValid(""));
    }
}
