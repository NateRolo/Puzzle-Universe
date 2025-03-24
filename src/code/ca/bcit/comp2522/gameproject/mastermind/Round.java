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

    private final int      roundNumber;
    private final Code     guess;
    private final Feedback feedback;
    private final boolean  deceptiveRound;

    /**
     * Constructs a new Round with the specified details.
     *
     * @param roundNumber    the number of this round
     * @param guess          the player's guess for this round
     * @param feedback       the feedback given for the guess
     * @param deceptiveRound whether this round's feedback was altered
     */
    Round(final int roundNumber,
          final Code guess,
          final Feedback feedback,
          final boolean deceptiveRound)
    {
        validateRoundData(roundNumber,
                          guess,
                          feedback);

        this.roundNumber    = roundNumber;
        this.guess          = guess;
        this.feedback       = feedback;
        this.deceptiveRound = deceptiveRound;
    }

    /**
     * Gets the round number.
     *
     * @return the round number
     */
    final int getRoundNumber()
    {
        return roundNumber;
    }

    /**
     * Gets the player's guess for this round.
     *
     * @return the Code object representing the guess
     */
    final Code getGuess()
    {
        return guess;
    }

    /**
     * Gets the feedback given for this round.
     *
     * @return the Feedback object
     */
    final Feedback getFeedback()
    {
        return feedback;
    }

    /**
     * Checks if this round's feedback was deceptive.
     *
     * @return true if the feedback was altered, false otherwise
     */
    final boolean isDeceptiveRound()
    {
        return deceptiveRound;
    }

    /*
     * Validates the round data ensuring all required fields are present and valid.
     *
     * @param roundNumber the round number to validate
     * @param guess      the guess to validate
     * @param feedback   the feedback to validate
     */
    private void validateRoundData(final int roundNumber,
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
              .append(", ")
              .append(feedback);

        if(deceptiveRound)
        {
            result.append(" [Deceptive Round]");
        }

        return result.toString();
    }
}
