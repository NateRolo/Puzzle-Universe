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
    private static final int MIN_COUNT = 0;
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
        evaluateGuess(secretCode, guessCode);

        this.correctPositionCount = evaluateGuess(secretCode, guessCode)[0];
        this.misplacedCount       = evaluateGuess(secretCode, guessCode)[1];
    }

    private static void validateCodes(final SecretCode secretCode,
                                  final PlayerGuessCode guessCode)
    {
        // throw exception
    }

    private static int[] evaluateGuess(final SecretCode secretCode,
                                      final PlayerGuessCode guessCode)
    {
        final List<Integer> secretCodeDigits;
        final List<Integer> guessCodeDigits;
        final List<Integer> secretCopy;
        final List<Integer> guessCopy;
        final int[] result;

        int correctPosition = 0;
        int misplaced = 0;

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
