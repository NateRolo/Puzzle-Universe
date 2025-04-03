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
 */
public class PlayerGuessCodeTest
{
    private static final int DEFAULT_EXPECTED_LENGTH = 4;

    @Test
    public void testFromInput_ValidGuess() throws InvalidGuessException
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
    public void testFromInput_ValidGuessWithHigherDigits() throws InvalidGuessException
    {
        // Assuming digits up to 8 are valid based on typical Mastermind rules.
        final String validInput = "8765";
        final PlayerGuessCode guessCode = PlayerGuessCode.fromInput(validInput);
        assertNotNull(guessCode,
                      "Guess code should not be null for valid input with higher digits");
        final List<Integer> expectedSequence = Arrays.asList(8,
                                                             7,
                                                             6,
                                                             5);
        assertEquals(expectedSequence,
                     guessCode.getDigits(),
                     "Code sequence should match the valid input: " +
                                                  validInput);
    }

    @Test
    public void testFromInput_InvalidLength_TooShort()
    {
        final String                invalidInput = "123";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for input too short");

        assertEquals("Guess must be exactly " +
                     DEFAULT_EXPECTED_LENGTH +
                     " digits long.",
                     exception.getMessage());
    }

    @Test
    public void testFromInput_InvalidLength_TooLong()
    {
        final String                invalidInput = "12345";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for input too long");
        assertEquals("Guess must be exactly " +
                     DEFAULT_EXPECTED_LENGTH +
                     " digits long.",
                     exception.getMessage());
    }

    @Test
    public void testFromInput_InvalidCharacters_NonNumeric()
    {
        final String                invalidInput = "12a4";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for non-numeric characters");
        assertEquals("Guess must contain only digits.",
                     exception.getMessage());
    }

    @Test
    public void testFromInput_InvalidCharacters_ZeroDigit()
    {
        // Assuming valid digits are usually 1-N
        final String                invalidInput = "1204";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for digit zero (if invalid)");
        // Adjust message based on actual validation rule
        assertEquals("Digits must be between 1 and 8.",
                     exception.getMessage());
    }

    @Test
    public void testFromInput_InvalidCharacters_DigitTooHigh()
    {
        // Assuming valid digits are 1-8
        final String                invalidInput = "1294";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for digit too high (e.g., 9)");
        assertEquals("Digits must be between 1 and 8.",
                     exception.getMessage());
    }


    @Test
    public void testgetDigits() throws InvalidGuessException
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

        try
        {
            actualSequence.add(9); // Try to modify the returned list
            assertNotEquals(actualSequence,
                            guessCode.getDigits(),
                            "Modifying returned list should not affect internal list if it's a copy");
        }
        catch(UnsupportedOperationException e)
        {
            // This is also acceptable if getDigits returns an
            // unmodifiable list
            System.out.println("getDigits returned an unmodifiable list, which is good.");
        }
    }

    @Test
    public void testFromInput_EmptyString()
    {
        final String                invalidInput = "";
        final InvalidGuessException exception    = assertThrows(InvalidGuessException.class,
                                                                () -> PlayerGuessCode.fromInput(invalidInput),
                                                                "Should throw InvalidGuessException for empty string");
        // Check message - likely length validation fails first
        assertEquals("Guess must be exactly " +
                     DEFAULT_EXPECTED_LENGTH +
                     " digits long.",
                     exception.getMessage());
    }

    @Test
    public void testFromInput_NullInput()
    {
        final NullPointerException exception = assertThrows(NullPointerException.class, // Or
                                                                                        // InvalidGuessException
                                                                                        // depending
                                                                                        // on
                                                                                        // implementation
                                                            () -> PlayerGuessCode.fromInput(null),
                                                            "Should throw an exception for null input");
        // No message check needed usually for NPE, or adjust if
        // InvalidGuessException is thrown
    }
}