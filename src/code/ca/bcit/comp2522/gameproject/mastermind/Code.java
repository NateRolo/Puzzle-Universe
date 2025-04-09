package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sequence of digits in the {@code Mastermind} game.
 * <p>
 * This class serves as the base representation for both secret codes and player
 * guesses, storing and managing a sequence of digits.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
abstract class Code
{
    static final int CODE_LENGTH = 4;
    static final int DIGIT_MIN   = 1;
    static final int DIGIT_MAX   = 6;

    // Example values for the game rules.
    static final String EXAMPLE_GUESS             = "1356";
    static final String EXAMPLE_SECRET            = "1234";
    static final int    EXAMPLE_CORRECT_POSITIONS = 1;
    static final int    EXAMPLE_MISPLACED         = 1;

    private final List<Integer> digits;

    /**
     * Constructs a new {@code Code} with the specified sequence of digits.
     *
     * @param digits the sequence of digits that make up the code
     */
    Code(final List<Integer> digits)
    {
        validateDigits(digits);
        this.digits = new ArrayList<>(digits);
    }

    /**
     * Returns a defensive copy of the code's digits.
     *
     * @return a new List containing the code's digits
     */
    List<Integer> getDigits()
    {
        return new ArrayList<>(digits);
    }

    /**
     * Returns the code length as an int.
     * 
     * @return {@value #CODE_LENGTH} as an int
     */
    static final int getCodeLength()
    {
        return CODE_LENGTH;
    }

    /**
     * Checks if the current code is equal to another code.
     * 
     * @param other the other code to compare with
     * @return true if the codes are equal, false otherwise
     */
    @Override
    public boolean equals(final Object other)
    {
        final Code    otherCode;
        final boolean codesAreEqual;

        if(other == null)
        {
            throw new NullPointerException("Other cannot be null");
        }

        if(this == other)
        {
            return true;
        }

        if(!(other instanceof Code))
        {
            throw new IllegalArgumentException("Other is not a Code");
        }

        otherCode     = (Code)other;
        codesAreEqual = digits.equals(otherCode.digits);

        return codesAreEqual;
    }

    /**
     * Returns the hash code of the code's digits.
     * 
     * @return the hash code of the code's digits
     */
    @Override
    public int hashCode()
    {
        return digits.hashCode();
    }

    /**
     * Returns a string representation of the code's digits.
     * 
     * @return a string representation of the code's digits
     */
    @Override
    public String toString()
    {
        return digits.toString();
    }

    /**
     * Validates the provided list of digits for nullity, emptiness, correct
     * size, and valid digit range. Also checks for null elements within the
     * list.
     *
     * @param digits the list of digits to validate
     */
    private static final void validateDigits(final List<Integer> digits)
    {
        if(digits == null)
        {
            throw new IllegalArgumentException("Digits list cannot be null");
        }

        if(digits.size() != CODE_LENGTH)
        {
            throw new IllegalArgumentException(String.format("Digits list must contain exactly %d digits, but found %d.",
                                                             CODE_LENGTH,
                                                             digits.size()));
        }

        // Check individual digit validity and null elements
        for(final Integer num : digits)
        {
            if(num == null)
            {
                throw new IllegalArgumentException("Digits list cannot contain null elements.");
            }
            if(num < DIGIT_MIN || num > DIGIT_MAX)
            {
                throw new IllegalArgumentException(String.format("Invalid code digit: %d. Must be between %d and %d.",
                                                                 num,
                                                                 DIGIT_MIN,
                                                                 DIGIT_MAX));
            }
        }
    }
}
