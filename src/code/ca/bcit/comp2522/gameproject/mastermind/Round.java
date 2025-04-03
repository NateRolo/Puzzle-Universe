package ca.bcit.comp2522.gameproject.mastermind;

/**
 * Records the details of a single round in the Mastermind game.
 * <p>
 * This class encapsulates all information about a single turn, including the
 * round number, player's guess, and the feedback received.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class Round
{
    private static final int MIN_ROUND_NUMBER         = 1;
    private static final int DECEPTIVE_ROUNDS_ALLOWED = 3;
    private static final int INITIAL_DECEPTIVE_ROUNDS = 0;

    private static int deceptiveRoundsUsed;

    private final int             roundNumber;
    private final PlayerGuessCode guess;
    private final Feedback        trueFeedback;
    private final Feedback        falseFeedback;
    private final boolean         isDeceptiveRound;
    private boolean               truthRevealed;

   
    /**
     * Constructs a new Round with the specified details.
     * <p>
     * Creates a round object that stores both the player's guess and the corresponding feedback.
     * For deceptive rounds, both true and false feedback are stored, with the deception
     * being applied according to the game's rules. The constructor also tracks the number
     * of deceptive rounds used in the game.
     * </p>
     *
     * @param roundNumber  the sequential number of this round in the current game
     * @param guess        the player's guess code for this round
     * @param trueFeedback the accurate feedback based on comparing the guess with the secret code
     */
    Round(final int roundNumber,
          final PlayerGuessCode guess,
          final Feedback trueFeedback)
    {
        validateRoundData(roundNumber,
                          guess,
                          trueFeedback);

        this.roundNumber      = roundNumber;
        this.guess            = guess;
        this.trueFeedback     = trueFeedback;
        this.isDeceptiveRound = DeceptionEngine.shouldApplyDeception(deceptiveRoundsUsed,
                                                                     DECEPTIVE_ROUNDS_ALLOWED);
        if(this.isDeceptiveRound)
        {
            this.falseFeedback = DeceptionEngine.applyDeception(trueFeedback);
            incrementDeceptiveRounds();
        }
        else
        {
            this.falseFeedback = null;
        }
        this.truthRevealed = false;
    }

    /**
     * Gets the round number.
     *
     * @return the round number
     */
    int getRoundNumber()
    {
        return roundNumber;
    }

    /**
     * Gets the player's guess for this round.
     *
     * @return the Code object representing the guess
     */
    PlayerGuessCode getGuess()
    {
        return guess;
    }

    /**
     * Gets the feedback given for this round.
     *
     * @return the Feedback object
     */
    Feedback getFeedback()
    {
        if(this.isDeceptiveRound)
        {
            return falseFeedback;
        }
        return trueFeedback;
    }

    /**
     * Checks if this round's feedback was deceptive.
     *
     * @return true if the feedback was altered, false otherwise
     */
    boolean isDeceptiveRound()
    {
        return isDeceptiveRound;
    }

    /**
     * Marks this round as having its truth revealed by a scan.
     */
    void revealTruth()
    {
        if(this.isDeceptiveRound)
        {
            this.truthRevealed = true;
        }
    }

    /**
     * Increments the number of deceptive rounds used.
     */
    static void incrementDeceptiveRounds()
    {
        Round.deceptiveRoundsUsed++;
    }

    /**
     * Gets the number of deceptive rounds used.
     * 
     * @return the number of deceptive rounds used
     */
    static int getDeceptiveRoundsUsed()
    {
        return Round.deceptiveRoundsUsed;
    }

    /**
     * Resets the number of deceptive rounds used.
     */
    static void resetDeceptiveRounds()
    {
        Round.deceptiveRoundsUsed = INITIAL_DECEPTIVE_ROUNDS;
    }

    /*
     * Validates the round data ensuring all required fields are present and
     * valid.
     *
     * @param roundNumber the round number to validate
     * 
     * @param guess the guess to validate
     * 
     * @param feedback the feedback to validate
     */
    private static void validateRoundData(final int roundNumber,
                                          final Code guess,
                                          final Feedback feedback)
    {
        if(roundNumber < MIN_ROUND_NUMBER)
        {
            throw new IllegalArgumentException("Round number must be positive");
        }
        if(guess == null)
        {
            throw new NullPointerException("Guess cannot be null");
        }
        if(feedback == null)
        {
            throw new NullPointerException("Feedback cannot be null");
        }
    }

    /**
     * Provides a formatted string summarizing the round for display.
     * Shows true feedback if truth has been revealed, otherwise shows
     * the feedback originally presented to the player (potentially deceptive).
     *
     * @return A string summary of the round.
     */
    String getSummaryLine()
    {
        final StringBuilder result;

        result = new StringBuilder();
        result.append("Round ")
              .append(roundNumber)
              .append(": Guess = ")
              .append(guess)
              .append(", ");

        if(isDeceptiveRound)
        {
            if(truthRevealed)
            {
                // Show true feedback, mark as revealed
                result.append("Actual Feedback: ")
                      .append(trueFeedback)
                      .append(" (Truth Revealed)");
            }
            else
            {
                // Show deceptive feedback
                result.append(falseFeedback);                     
            }
        }
        else
        {
            // Not deceptive, just show true feedback
            result.append(trueFeedback);
        }

        return result.toString();
    }

    /**
     * Override toString to use the summary line for general printing
     * 
     * @return the summary line
     */
    @Override
    public String toString()
    {
        return getSummaryLine();
    }
}
