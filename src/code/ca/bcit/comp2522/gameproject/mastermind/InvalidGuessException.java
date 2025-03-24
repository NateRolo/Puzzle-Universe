package ca.bcit.comp2522.gameproject.mastermind;

/**
 * Custom exception for handling invalid guesses in Mastermind.
 * <p>
 * This exception is thrown when a player's guess does not meet the required
 * format or constraints of the game.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class InvalidGuessException extends RuntimeException
{
    /**
     * Constructs a new InvalidGuessException with the specified message.
     *
     * @param message detailed explanation of why the guess was invalid
     */
    InvalidGuessException(final String message)
    {
        super(message);
    }

    /**
     * Constructs a new InvalidGuessException with a message and cause.
     *
     * @param message detailed explanation of why the guess was invalid
     * @param cause   the underlying cause of the invalid guess
     */
    InvalidGuessException(final String message,
                         final Throwable cause)
    {
        super(message,
              cause);
    }
}
