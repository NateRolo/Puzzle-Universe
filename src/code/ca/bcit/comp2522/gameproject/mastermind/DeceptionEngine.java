package ca.bcit.comp2522.gameproject.mastermind;

import java.util.Random;

/**
 * Manages the generation and application of deceptive feedback in the
 * Mastermind game.
 * <p>
 * This utility class provides static methods to determine probabilistically
 * whether a given game round should feature deceptive (incorrect) feedback,
 * based on a predefined chance and a limit on the total number of deceptive
 * rounds allowed per game. It also contains the logic to generate this false
 * feedback, ensuring it differs from the actual feedback the player would have
 * received for their guess, thereby adding a layer of challenge and
 * uncertainty. The goal is to maintain game fairness while introducing
 * strategic doubt for the player.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class DeceptionEngine
{

    private static final Random RANDOM               = new Random();
    private static final double DECEPTION_CHANCE     = 0.3;
    private static final int    DECEPTION_MIN_DIGITS = 0;
    private static final String FALSE_GUESS_DIGITS   = "1111";

    /**
     * Private constructor to prevent instantiation.
     */
    private DeceptionEngine()
    {
        // Utility class should not be instantiated
    }

    /**
     * Determines if deception should be applied based on the current game
     * state.
     * <p>
     * This method uses a combination of deterministic and random factors to
     * decide whether to apply deception in the current round. It checks if the
     * game has already used its maximum allowed deceptive rounds, and if not,
     * applies a random chance (defined by DECEPTION_CHANCE) to determine if
     * this round should be deceptive.
     * </p>
     *
     * @param deceptiveRoundsUsed    current count of deceptive rounds already
     *                               used
     *                               in the game
     * @param deceptiveRoundsAllowed maximum number of deceptive rounds allowed
     *                               in the game
     * @return true if deception should be applied in this round, false
     *         otherwise
     */
    static boolean shouldApplyDeception(final int deceptiveRoundsUsed,
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
     * Applies a controlled modification to the feedback to create deception.
     * <p>
     * This method generates false feedback that differs from the true feedback
     * by creating a random secret code and using a predefined guess. It ensures
     * that the deceptive feedback is different from the actual feedback to
     * maintain the deceptive element of the game.
     * </p>
     *
     * @param trueFeedback the original, true feedback based on the player's
     *                     actual guess
     * @return a modified feedback object that contains deceptive information
     */
    static Feedback applyDeception(final Feedback trueFeedback)
    {
        if(trueFeedback == null)
        {
            throw new NullPointerException("True feedback cannot be null");
        }

        // Loop to ensure that randomly generated false feedback is not
        // accurate.
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
     * 
     * @param deceptiveRoundsUsed current count of deceptive rounds used
     * 
     * @param deceptiveRoundsAllowed maximum allowed deceptive rounds
     */
    private static void validateDeceptionParameters(final int deceptiveRoundsUsed,
                                                    final int deceptiveRoundsAllowed)
    {
        if(deceptiveRoundsUsed < DECEPTION_MIN_DIGITS)
        {
            throw new IllegalArgumentException("Deceptive rounds used cannot be negative");
        }
        if(deceptiveRoundsAllowed < DECEPTION_MIN_DIGITS)
        {
            throw new IllegalArgumentException("Deceptive rounds allowed cannot be negative");
        }
        if(deceptiveRoundsUsed > deceptiveRoundsAllowed)
        {
            throw new IllegalArgumentException("Used rounds cannot exceed allowed rounds");
        }
    }
}
