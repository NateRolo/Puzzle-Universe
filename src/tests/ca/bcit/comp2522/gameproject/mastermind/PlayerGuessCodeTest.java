package ca.bcit.comp2522.gameproject.mastermind;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for PlayerGuessCode.
 * Verifies the creation and validation of player guesses.
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public class PlayerGuessCodeTest
{
    private static final int DEFAULT_EXPECTED_LENGTH = 4;

    @Test
    public void testFromInputValidGuess()
    {
        final String validInput = "1234";
        final PlayerGuessCode guessCode = PlayerGuessCode.fromInput(validInput);
        assertNotNull(guessCode,
                      "Guess code should not be null for valid input");
        final List<Integer> expectedSequence = Arrays.asList(1,
                                                             2,
                                                             3,
                                                             4);
        assertEquals(expectedSequence,
                     guessCode.getDigits(),
                     "Code sequence should match the valid input");
    }


    @Test
    public void testFromInputInvalidLengthTooShort()
    {
        final String                invalidInput = "123";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for input too short");

        assertEquals("Invalid input length. Expected " +
                     DEFAULT_EXPECTED_LENGTH +
                     " digits, but got " +
                     invalidInput.length() +
                     ".",
                     exception.getMessage());
    }

    @Test
    public void testFromInputInvalidLengthTooLong()
    {
        final String                invalidInput = "12345";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for input too long");
        assertEquals("Invalid input length. Expected " +
                     DEFAULT_EXPECTED_LENGTH +
                     " digits, but got " +
                      invalidInput.length() +
                      ".",
                     exception.getMessage());
    }

    @Test
    public void testFromInputInvalidCharactersNonNumeric()
    {
        final String                invalidInput = "12a4";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for non-numeric characters");
        assertEquals("Invalid character 'a' at position 3.Only digits 1-6 are allowed.",
                     exception.getMessage());
    }

    @Test
    public void testFromInputZeroDigit()
    {
        // Assuming valid digits are usually 1-N
        final String                invalidInput = "1204";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for digit zero (if invalid)");
        // Adjust message based on actual validation rule
        assertEquals("Invalid character '0' at position 3.Only digits 1-6 are allowed.",
                     exception.getMessage());
    }

    @Test
    public void testFromInputDigitTooHigh()
    {
        // Assuming valid digits are 1-8
        final String invalidInput = "1294";
        final InvalidGuessException exception = assertThrows(InvalidGuessException.class,
                                                             () -> PlayerGuessCode.fromInput(invalidInput),
                                                             "Should throw InvalidGuessException for digit too high (e.g., 9)");
        assertEquals("Invalid character '9' at position 3.Only digits 1-6 are allowed.",
                     exception.getMessage());
    }

    @Test
    public void testGetDigits()
    {
        final String validInput = "5566";
        final PlayerGuessCode guessCode        = PlayerGuessCode.fromInput(validInput);
        final List<Integer>   expectedSequence = Arrays.asList(5,
                                                               5,
                                                               6,
                                                               6);
        final List<Integer>   actualSequence   = guessCode.getDigits();
        assertEquals(expectedSequence,
                     actualSequence,
                     "getDigits should return the correct internal list");
    }

    @Test
    public void testFromInputEmptyString()
    {
        final String                invalidInput = "";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for empty string");
        assertEquals("Invalid input length. Expected 4 digits, " +
                     "but got 0.",
                     exception.getMessage());
    }

    @Test
    public void testNullInput()
    {
        final InvalidGuessException exception1 = assertThrows(InvalidGuessException.class,
                                                            () -> PlayerGuessCode.fromInput(null),
                                                            "Should throw an exception for null input");

        

    }

}