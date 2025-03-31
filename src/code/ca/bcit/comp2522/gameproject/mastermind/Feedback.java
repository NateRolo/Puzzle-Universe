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
    private static final int RESULT_SIZE = 2;
    private static final int CORRECT_POSITION = 0;
    private static final int MISPLACED = 1;

    private final int     correctPositionCount;
    private final int     misplacedCount;

    /**
     * Constructs a new Feedback object.
     *
     */
    Feedback(final SecretCode secretCode,
             final PlayerGuessCode guessCode)
    {
        validateCodes(secretCode, guessCode);

        this.correctPositionCount = evaluateGuess(secretCode, guessCode)[CORRECT_POSITION];
        this.misplacedCount       = evaluateGuess(secretCode, guessCode)[MISPLACED];
    }

    private static void validateCodes(final SecretCode secretCode,
                                  final PlayerGuessCode guessCode)
    {
        if(secretCode == null || guessCode == null)
        {
            throw new IllegalArgumentException("Codes cannot be null");
        }

        final int secretCodeLength;
        final int guessCodeLength;

        secretCodeLength = secretCode.getDigits().size();
        guessCodeLength = guessCode.getDigits()
                                     .size();

        if(secretCodeLength != Code.CODE_LENGTH ||
           guessCodeLength != Code.CODE_LENGTH)
        {
            throw new IllegalArgumentException("Codes must be of length " + Code.CODE_LENGTH);
        }
    }

    private static int[] evaluateGuess(final SecretCode secretCode,
                                      final PlayerGuessCode guessCode)
    {
        final List<Integer> secretCodeDigits;
        final List<Integer> guessCodeDigits;
        final List<Integer> secretCopy;
        final List<Integer> guessCopy;
        final int[] result;

        int correctPosition;
        int misplaced;

        correctPosition = 0;
        misplaced = 0;

        secretCodeDigits = secretCode.getDigits();
        guessCodeDigits = guessCode.getDigits();

        for(int i = 0; i < Code.CODE_LENGTH; i++)
        {
            if(secretCodeDigits.get(i).equals(guessCodeDigits.get(i)))
            {
                correctPosition++;
            }
        }

        secretCopy = new ArrayList<>(secretCodeDigits);
        guessCopy = new ArrayList<>(guessCodeDigits);

        for(int j = 0; j < Code.CODE_LENGTH; j++)
        {
            if(secretCopy.contains(guessCopy.get(j)))
            {
                misplaced++;
                secretCopy.remove(guessCopy.get(j));
            }
        }

        misplaced = misplaced - correctPosition;

        result = new int[RESULT_SIZE];
        result[CORRECT_POSITION] = correctPosition;
        result[MISPLACED] = misplaced;

        return result;
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


}