package ca.bcit.comp2522.gameproject.mastermind;

import java.util.List;

/**
 * Represents a player's input in the Mastermind game.
 * <p>
 * This class handles both regular guesses and truth scan requests,
 * providing a unified way to process player input.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class PlayerGuessCode extends
                               Code implements PlayerAction
{
    /**
     * Constructs a PlayerGuessCode representing a guess.
     *
     * @param digits the sequence of digits that make up the guess
     */
    public PlayerGuessCode(final List<Integer> digits)
    {
        super(digits);
    }
}