package ca.bcit.comp2522.gameproject.mastermind;

import java.util.Random;

/**
 * Manages deceptive feedback in the Mastermind game.
 * <p>
 * This class determines when to apply deception and how to modify feedback in a
 * controlled way to maintain game fairness while adding challenge.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class DeceptionEngine
{
    private static final Random RANDOM               = new Random();
    private static final double DECEPTION_CHANCE     = 0.3;
    private static final int    MIN_DECEPTION_ROUNDS = 0;
    private static final String FALSE_GUESS_DIGITS = "1111";

    /**
     * Private constructor to prevent instantiation.
     */
    private DeceptionEngine()
    {
        // Utility class should not be instantiated
    }

    /**
     * Determines if deception should be applied based on game state.
     *
     * @param deceptiveRoundsUsed    current count of deceptive rounds
     * @param deceptiveRoundsAllowed maximum allowed deceptive rounds
     * @return true if deception should be applied, false otherwise
     */
    static final boolean shouldApplyDeception(final int deceptiveRoundsUsed,
                                              final int deceptiveRoundsAllowed)
    {
        validateDeceptionParameters(deceptiveRoundsUsed,
                                    deceptiveRoundsAllowed);

        final boolean shouldDeceive;
        shouldDeceive = deceptiveRoundsUsed < deceptiveRoundsAllowed &&
                        RANDOM.nextDouble() < DECEPTION_CHANCE;

        return shouldDeceive;
    }

    /**
     * Applies a controlled modification to the feedback.
     *
     * @param trueFeedback the original, true feedback
     * @return modified feedback marked as deceptive
     */
    static final Feedback applyDeception(final Feedback trueFeedback)
    {
        if(trueFeedback == null)
        {
            throw new NullPointerException("True feedback cannot be null");
        }

        Feedback falseFeedback;
        do
        {
            final SecretCode      falseCode;
            final PlayerGuessCode falseGuess;

            falseCode     = SecretCode.generateRandomCode(Code.CODE_LENGTH);
            falseGuess    = PlayerGuessCode.fromInput(FALSE_GUESS_DIGITS);
            falseFeedback = new Feedback(falseCode,
                                         falseGuess);
        } while(trueFeedback.equals(falseFeedback));

        return falseFeedback;
    }

    /*
     * Validates parameters for deception calculation.
     */
    private static final void validateDeceptionParameters(final int used,
                                                          final int allowed)
    {
        if(used < MIN_DECEPTION_ROUNDS)
        {
            throw new IllegalArgumentException("Deceptive rounds used cannot be negative");
        }
        if(allowed < MIN_DECEPTION_ROUNDS)
        {
            throw new IllegalArgumentException("Deceptive rounds allowed cannot be negative");
        }
        if(used > allowed)
        {
            throw new IllegalArgumentException("Used rounds cannot exceed allowed rounds");
        }
    }
}