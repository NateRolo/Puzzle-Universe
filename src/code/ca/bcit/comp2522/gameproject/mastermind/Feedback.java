package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;

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
    private static final int RESULT_SIZE               = 2;
    private static final int CORRECT_POSITION          = 0;
    private static final int MISPLACED                 = 1;
    private static final int DEFAULT_CORRECT_POSITIONS = 0;
    private static final int DEFAULT_MISPLACED         = 0;

    private final int correctPositionCount;
    private final int misplacedCount;

    /**
     * Constructs a new Feedback object.
     */
    <S extends Code, G extends Code> Feedback(final S secretCode,
                                              final G guessCode)
    {
        validateCodes(secretCode,
                      guessCode);

        final int[] result;
        result                    = evaluateGuess(secretCode,
                                                  guessCode);
        this.correctPositionCount = result[CORRECT_POSITION];
        this.misplacedCount       = result[MISPLACED];
    }

    /**
     * Gets the count of digits in correct positions.
     *
     * @return number of correctly positioned digits
     */
    int getCorrectPositionCount()
    {
        return correctPositionCount;
    }

    /**
     * Gets the count of correct digits in wrong positions.
     *
     * @return number of misplaced digits
     */
    int getMisplacedCount()
    {
        return misplacedCount;
    }

    /**
     * Returns the feedback object as a formatted String.
     *
     * @return the feedback as a String
     */
    @Override
    public String toString()
    {
        final StringBuilder result;

        result = new StringBuilder();
        result.append("Correct positions: ")
              .append(correctPositionCount)
              .append(", Misplaced: ")
              .append(misplacedCount);

        return result.toString();
    }

    // comments
    private static void validateCodes(final Code secretCode,
                                      final Code guessCode)
    {
        if(secretCode == null || guessCode == null)
        {
            throw new IllegalArgumentException("Codes cannot be null");
        }

        final int secretCodeLength;
        final int guessCodeLength;

        secretCodeLength = secretCode.getDigits()
                                     .size();
        guessCodeLength  = guessCode.getDigits()
                                    .size();

        if(secretCodeLength != Code.CODE_LENGTH ||
           guessCodeLength != Code.CODE_LENGTH)
        {
            throw new IllegalArgumentException("Codes must be of length " +
                                               Code.CODE_LENGTH);
        }
    }

    // comments
    private static int[] evaluateGuess(final Code secretCode,
                                       final Code guessCode)
    {
        final List<Integer> secretCodeDigits;
        final List<Integer> guessCodeDigits;
        final List<Integer> secretCopy;
        final List<Integer> guessCopy;
        final int[]         result;

        int correctPosition;
        int misplaced;

        correctPosition = DEFAULT_CORRECT_POSITIONS;
        misplaced       = DEFAULT_MISPLACED;

        secretCodeDigits = secretCode.getDigits();
        guessCodeDigits  = guessCode.getDigits();

        // Calculate correct positions
        for(int i = 0; i < Code.CODE_LENGTH; i++)
        {
            final int secretCodeDigitAtThisIndex;
            final int guessCodeDigitAtThisIndex;

            secretCodeDigitAtThisIndex = secretCodeDigits.get(i);
            guessCodeDigitAtThisIndex = guessCodeDigits.get(i);

            if(secretCodeDigitAtThisIndex == guessCodeDigitAtThisIndex)
            {
                correctPosition++;
            }
        }

        secretCopy = new ArrayList<>(secretCodeDigits);
        guessCopy  = new ArrayList<>(guessCodeDigits);

        // Calculate total matches (including correct position) for misplaced count derivation
        for(int j = 0; j < Code.CODE_LENGTH; j++)
        {
            if(secretCopy.contains(guessCopy.get(j)))
            {
                misplaced++;
                secretCopy.remove(guessCopy.get(j));
            }
        }

        // Adjust misplaced: total matches minus those in the correct position
        misplaced = misplaced - correctPosition;

        result                   = new int[RESULT_SIZE];
        result[CORRECT_POSITION] = correctPosition;
        result[MISPLACED]        = misplaced;

        return result;
    }




}