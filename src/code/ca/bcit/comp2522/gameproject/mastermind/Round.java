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
    private static final int MIN_ROUND_NUMBER = 1;
    private static final int DECEPTIVE_ROUNDS_ALLOWED = 3;
    private static final int INITIAL_DECEPTIVE_ROUNDS = 0;

    private final int      roundNumber;
    private final PlayerGuessCode     guess;
    private final Feedback trueFeedback;
    private final Feedback falseFeedback;
    private final boolean  isDeceptiveRound;
    
    private static int deceptiveRoundsUsed;

    /**
     * Constructs a new Round with the specified details.
     *
     * @param roundNumber    the number of this round
     * @param guess          the player's guess for this round
     * @param trueFeedback       the feedback given for the guess
     */
    Round(final int roundNumber,
          final PlayerGuessCode guess,
          final Feedback trueFeedback)
    {
        validateRoundData(roundNumber,
                          guess,
                          trueFeedback);

        this.roundNumber    = roundNumber;
        this.guess          = guess;
        this.trueFeedback   = trueFeedback;
        this.isDeceptiveRound = DeceptionEngine.shouldApplyDeception(deceptiveRoundsUsed,
                                                                     DECEPTIVE_ROUNDS_ALLOWED);
        if(this.isDeceptiveRound)
        {
            this.falseFeedback  = DeceptionEngine.applyDeception(trueFeedback);
            incrementDeceptiveRounds();
        }
        else
        {
            this.falseFeedback = null;
        }
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

    static void incrementDeceptiveRounds()
    {
        Round.deceptiveRoundsUsed++;
    }

    public static final int getDeceptiveRoundsUsed()
    {
        return Round.deceptiveRoundsUsed;
    }

    static void resetDeceptiveRounds()
    {
        Round.deceptiveRoundsUsed = INITIAL_DECEPTIVE_ROUNDS;
    }

    /*
     * Validates the round data ensuring all required fields are present and valid.
     *
     * @param roundNumber the round number to validate
     * @param guess      the guess to validate
     * @param feedback   the feedback to validate
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

    @Override
    public String toString()
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
            result.append(trueFeedback);
        }
        else
        {
            result.append(falseFeedback);
        }

        return result.toString();
    }
}
