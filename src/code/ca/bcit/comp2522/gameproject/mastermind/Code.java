package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sequence of digits in the Mastermind game.
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
    private final List<Integer> digits;
    static final int MIN_DIGIT = 1;
    static final int MAX_DIGIT = 6;
    static final int CODE_LENGTH = 4;

    /**
     * Constructs a new Code with the specified sequence of digits.
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
     * Validates that the provided digits are not null or empty.
     *
     * @param digits the digits to validate
     */
    private static void validateDigits(final List<Integer> digits)
    {
        if (digits == null)
        {
            throw new IllegalArgumentException("Digits cannot be null");
        }
        if (digits.isEmpty())
        {
            throw new IllegalArgumentException("Digits cannot be empty");
        }

        for(Integer num: digits)
        {
            if(num < MIN_DIGIT || num > MAX_DIGIT)
            {
                throw new IllegalArgumentException("Invalid code digit:" + num);
            }
        }
    }

    @Override
    public boolean equals(final Object other)
    {
        if (other == null)
        {
            throw new NullPointerException("Other cannot be null");
        }

        if (this == other)
        {
            return true;
        }
        
        if (!(other instanceof Code))
        {
            throw new IllegalArgumentException("Other is not a Code");
        }

        final Code otherCode = (Code)other;
        return digits.equals(otherCode.digits);
    }

    @Override
    public int hashCode()
    {
        return digits.hashCode();
    }

    @Override
    public String toString()
    {
        return digits.toString();
    }
}
