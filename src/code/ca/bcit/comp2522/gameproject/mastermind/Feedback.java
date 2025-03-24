package ca.bcit.comp2522.gameproject.mastermind;

/**
 * Stores and displays the result of a guess in Mastermind.
 * <p>
 * This class encapsulates the feedback given to a player after their guess,
 * including the number of correct digits in correct positions and the number
 * of correct digits in wrong positions.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class Feedback
{
    private static final int MIN_COUNT = 0;

    private final int     correctPositionCount;
    private final int     misplacedCount;
    private final boolean deceptive;

    /**
     * Constructs a new Feedback object.
     *
     * @param correctPositionCount number of digits in correct position
     * @param misplacedCount       number of correct digits in wrong positions
     * @param deceptive            whether this feedback is intentionally altered
     */
    Feedback(final int correctPositionCount,
             final int misplacedCount,
             final boolean deceptive)
    {
        validateCounts(correctPositionCount,
                       misplacedCount);
        this.correctPositionCount = correctPositionCount;
        this.misplacedCount       = misplacedCount;
        this.deceptive            = deceptive;
    }

    /**
     * Gets the count of digits in correct positions.
     *
     * @return number of correctly positioned digits
     */
    final int getCorrectPositionCount()
    {
        return correctPositionCount;
    }

    /**
     * Gets the count of correct digits in wrong positions.
     *
     * @return number of misplaced digits
     */
    final int getMisplacedCount()
    {
        return misplacedCount;
    }

    /**
     * Checks if this feedback has been intentionally altered.
     *
     * @return true if feedback is deceptive, false otherwise
     */
    final boolean isDeceptive()
    {
        return deceptive;
    }

    /*
     * Validates that the feedback counts are non-negative.
     *
     * @param correctPos number of correct positions to validate
     * @param misplaced  number of misplaced digits to validate
     */
    private void validateCounts(final int correctPos,
                                final int misplaced)
    {
        if(correctPos < MIN_COUNT)
        {
            throw new IllegalArgumentException("Correct position count cannot be negative");
        }
        if(misplaced < MIN_COUNT)
        {
            throw new IllegalArgumentException("Misplaced count cannot be negative");
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder result;

        result = new StringBuilder();
        result.append("Correct positions: ")
              .append(correctPositionCount)
              .append(", Misplaced: ")
              .append(misplacedCount);

        if(deceptive)
        {
            result.append(" (?)");
        }

        return result.toString();
    }
}
