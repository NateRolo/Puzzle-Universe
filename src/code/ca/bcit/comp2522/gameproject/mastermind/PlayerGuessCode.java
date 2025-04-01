package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's input guess in the Mastermind game.
 * <p>
 * Instances are created via the static factory method {@code fromInput},
 * which handles input validation and conversion.
 * </p>
 *
 * @author Nathan O
 * @version 1.1 2025
 */
public final class PlayerGuessCode extends
                                   Code implements
                                   PlayerAction
{
    // Renamed constant for clarity, assuming digits 1-6 are valid
    private static final String VALID_DIGIT_PATTERN = "[1-6]";
    private static final int CHARACTER_INCREMENT = 1;

    /**
     * Private constructor to enforce creation via factory method.
     *
     * @param digits the validated sequence of digits for the guess.
     */
    private PlayerGuessCode(final List<Integer> digits)
    {
        super(digits);
    }

    /**
     * Creates a PlayerGuessCode from a raw input string after validation.
     * <p>
     * Validates the input string's length and ensures all characters are
     * digits.
     * Converts the valid string into a list of integers.
     * </p>
     *
     * @param input      The raw string input from the player.
     * @param codeLength The expected length of the code.
     * @return A new PlayerGuessCode instance.
     * @throws InvalidGuessException if the input is null, has incorrect length,
     *                               or contains non-digit characters.
     */
    public static PlayerGuessCode fromInput(final String input)
    {
        validateInput(input);

        if(input.length() != CODE_LENGTH)
        {
            
        }

        final List<Integer> digits;
        digits = new ArrayList<>(CODE_LENGTH);

        for(int i = 0; i < CODE_LENGTH; i++)
        {
            final char   thisCharacter;
            final String thisCharacterAsString;

            thisCharacter         = input.charAt(i);
            thisCharacterAsString = String.valueOf(thisCharacter);

            if(!thisCharacterAsString.matches(VALID_DIGIT_PATTERN))
            {
                final String message = String.format("Invalid character '%c' at position %d. Only digits 1-6 are allowed.",
                                                     thisCharacter,
                                                     i + CHARACTER_INCREMENT);
                throw new InvalidGuessException(message);
            }
            digits.add(Character.getNumericValue(thisCharacter));
        }
        return new PlayerGuessCode(digits);
    }

    private static final void validateInput(final String input) throws InvalidGuessException
    {
        if(input == null)
        {
            throw new InvalidGuessException("Input cannot be null.");
        }

        if(input.length() != Code.getCodeLength())
        {
            final String message;

            message = String.format("Invalid input length. Expected %d digits, but got %d.",
                                    CODE_LENGTH,
                                    input.length());
            throw new InvalidGuessException(message);
        }
    }

    
}