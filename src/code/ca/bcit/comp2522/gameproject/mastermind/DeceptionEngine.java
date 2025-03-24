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
    private static final Random RANDOM                  = new Random();
    private static final double DECEPTION_CHANCE        = 0.3;
    private static final int    MAX_FEEDBACK_ADJUSTMENT = 1;

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
    static boolean shouldApplyDeception(final int deceptiveRoundsUsed,
                                        final int deceptiveRoundsAllowed)
    {
        validateDeceptionParameters(deceptiveRoundsUsed,
                                    deceptiveRoundsAllowed);

        return deceptiveRoundsUsed < deceptiveRoundsAllowed && RANDOM.nextDouble() < DECEPTION_CHANCE;
    }

    /**
     * Applies a controlled modification to the feedback.
     *
     * @param trueFeedback the original, true feedback
     * @return modified feedback marked as deceptive
     */
    static Feedback applyDeception(final Feedback trueFeedback)
    {
        if(trueFeedback == null)
        {
            throw new NullPointerException("True feedback cannot be null");
        }

        final int     correctPositionCount;
        final int     misplacedCount;
        final boolean adjustCorrectPosition;

        adjustCorrectPosition = RANDOM.nextBoolean();

        if(adjustCorrectPosition)
        {
            correctPositionCount = adjustFeedbackValue(trueFeedback.getCorrectPositionCount());
            misplacedCount       = trueFeedback.getMisplacedCount();
        }
        else
        {
            correctPositionCount = trueFeedback.getCorrectPositionCount();
            misplacedCount       = adjustFeedbackValue(trueFeedback.getMisplacedCount());
        }

        return new Feedback(correctPositionCount,
                            misplacedCount,
                            true);
    }

    /*
     * Adjusts a feedback value by ±1 while ensuring it remains non-negative.
     */
    private static int adjustFeedbackValue(final int originalValue)
    {
        final int adjustment;

        adjustment = RANDOM.nextBoolean() ? MAX_FEEDBACK_ADJUSTMENT : - MAX_FEEDBACK_ADJUSTMENT;

        return Math.max(0,
                        originalValue + adjustment);
    }

    /*
     * Validates parameters for deception calculation.
     */
    private static void validateDeceptionParameters(final int used,
                                                    final int allowed)
    {
        if(used < 0)
        {
            throw new IllegalArgumentException("Deceptive rounds used cannot be negative");
        }
        if(allowed < 0)
        {
            throw new IllegalArgumentException("Deceptive rounds allowed cannot be negative");
        }
        if(used > allowed)
        {
            throw new IllegalArgumentException("Used rounds cannot exceed allowed rounds");
        }
    }
}