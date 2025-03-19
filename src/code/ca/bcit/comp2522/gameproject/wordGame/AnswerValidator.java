package ca.bcit.comp2522.gameproject.wordGame;

import java.util.Scanner;

public class AnswerValidator
{
    private final Scanner scanner;
    private final Score   score;

    public AnswerValidator(Scanner scanner,
                           Score score)
    {
        validateDependencies(scanner,
                             score);
        this.scanner = scanner;
        this.score   = score;
    }

    public void validateAnswer(final String expectedAnswer)
    {
        if(expectedAnswer == null)
        {
            throw new IllegalArgumentException("Expected answer cannot be null");
        }

        final String userAnswer;
        final boolean answerIsCorrect;

        userAnswer = scanner.nextLine();
        answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

        if(answerIsCorrect)
        {
            System.out.println("CORRECT");
            score.incrementNumCorrectFirstAttempt();
            return;
        }

        handleSecondAttempt(expectedAnswer);
    }

    private void handleSecondAttempt(final String expectedAnswer)
    {
        final String userAnswer;
        final boolean answerIsCorrect;

        System.out.println("INCORRECT\nOne more guess:");
        userAnswer = scanner.nextLine();
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

    private static void validateDependencies(final Scanner scanner,
                                             final Score score)
    {
        if(scanner == null)
        {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        if(score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }
    }
}