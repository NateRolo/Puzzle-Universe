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
final class PlayerGuessCode extends
                                   Code implements
                                   PlayerAction
{
    private static final String VALID_DIGIT_PATTERN = "[1-6]";
    private static final int    CHARACTER_INCREMENT = 1;

    /*
     * Private constructor to enforce the use of the factory method.
     * Initializes the code sequence for the guess.
     *
     * @param digits The validated sequence of digits representing the guess.
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
     * @return A new PlayerGuessCode instance.
     */
    static PlayerGuessCode fromInput(final String input)
    {
        validateInput(input);

        final List<Integer> digits;
        digits = new ArrayList<>(CODE_LENGTH);

        // Iterate through input string to validate and convert characters
        for(int i = 0; i < CODE_LENGTH; i++)
        {
            final char   thisCharacter;
            final String thisCharacterAsString;

            thisCharacter         = input.charAt(i);
            thisCharacterAsString = String.valueOf(thisCharacter);

            if(!thisCharacterAsString.matches(VALID_DIGIT_PATTERN))
            {
                final String message = String.format("Invalid character '%c' at position %d." +
                                                     "Only digits 1-6 are allowed.",
                                                     thisCharacter,
                                                     i + CHARACTER_INCREMENT);
                throw new InvalidGuessException(message);
            }
            digits.add(Character.getNumericValue(thisCharacter));
        }
        return new PlayerGuessCode(digits);
    }

    /*
     * Private helper method to validate the basic properties of the input
     * string.
     *
     * @param input The raw input string.
     * @param codeLength The expected length of the code.
     */
    private static void validateInput(final String input)
    {
        if(input == null)
        {
            throw new InvalidGuessException("Input cannot be null.");
        }

        if(input.length() != Code.getCodeLength())
        {
            final String message;

            message = String.format("Invalid input length. Expected %d digits, but got %d.",
                                    Code.getCodeLength(),
                                    input.length());
            throw new InvalidGuessException(message);
        }
    }


}