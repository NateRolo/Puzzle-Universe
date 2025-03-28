package ca.bcit.comp2522.gameproject.wordgame;

import java.util.Scanner;

/**
 * Validates user answers in the word game.
 * <p>
 * This class handles the validation of user answers against expected answers,
 * providing two attempts for each question and tracking the score
 * accordingly.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class AnswerValidator
{
    private final Scanner scanner;
    private final Score   score;

    /**
     * Constructs a new AnswerValidator with the specified scanner and score
     * tracker.
     *
     * @param scanner the Scanner object used to read user input
     * @param score   the Score object used to track game statistics
     */
    AnswerValidator(final Scanner scanner,
                    final Score score)
    {
        validateDependencies(scanner,
                             score);
        this.scanner = scanner;
        this.score   = score;
    }

    /**
     * Validates a user's answer against the expected answer.
     * <p>
     * Provides two attempts for the user to guess correctly. Updates the score
     * based on whether the answer was correct on the first or second attempt,
     * or incorrect on both attempts.
     * </p>
     *
     * @param expectedAnswer the correct answer to validate against
     */
    void validateAnswer(final String expectedAnswer)
    {
        validateExpectedAnswer(expectedAnswer);

        final String  userAnswer;
        final boolean answerIsCorrect;

        userAnswer      = scanner.nextLine();
        answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

        if(answerIsCorrect)
        {
            System.out.println("CORRECT");
            score.incrementNumCorrectFirstAttempt();
            return;
        }
        handleSecondAttempt(expectedAnswer);
    }

    /**
     * Handles the second attempt for an incorrect answer.
     * <p>
     * Provides the user with one more chance to answer correctly and updates
     * the score accordingly.
     * </p>
     *
     * @param expectedAnswer the correct answer to validate against
     */
    private void handleSecondAttempt(final String expectedAnswer)
    {
        validateExpectedAnswer(expectedAnswer);

        final String  userAnswer;
        final boolean answerIsCorrect;

        System.out.println("INCORRECT\nOne more guess:");
        userAnswer      = scanner.nextLine();
        answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

        if(answerIsCorrect)
        {
            System.out.println("CORRECT");
            score.incrementNumCorrectSecondAttempt();
        }
        else
        {
            System.out.println("INCORRECT\nThe correct answer was " + expectedAnswer + "\n");
            score.incrementNumIncorrectTwoAttempts();
        }
    }

    /**
     * Validates that the required dependencies are not null.
     *
     * @param scanner the Scanner to validate
     * @param score   the Score to validate
     */
    private static void validateDependencies(final Scanner scanner,
                                             final Score score)
    {
        if(scanner == null)
        {
            throw new NullPointerException("Scanner cannot be null");
        }
        if(score == null)
        {
            throw new NullPointerException("Score cannot be null");
        }
    }

    /**
     * Validates that the expected answer is not null or empty.
     *
     * @param expectedAnswer the answer to validate
     */
    private static void validateExpectedAnswer(final String expectedAnswer)
    {
        if(expectedAnswer == null)
        {
            throw new NullPointerException("Expected answer cannot be null");
        }

        if(expectedAnswer.trim().isBlank())
        {
            throw new IllegalArgumentException("Expected answer cannot be empty");
        }
    }
}