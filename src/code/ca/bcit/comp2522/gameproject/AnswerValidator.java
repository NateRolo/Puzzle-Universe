package ca.bcit.comp2522.gameproject;

import java.util.Scanner;

public class AnswerValidator
{
    private final Scanner scanner;
    private final Score   score;

    public AnswerValidator(Scanner scanner,
                           Score score)
    {
        this.scanner = scanner;
        this.score   = score;
    }

    public void validateAnswer(String expectedAnswer)
    {
        String  userAnswer      = scanner.nextLine();
        boolean answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

        if(answerIsCorrect)
        {
            System.out.println("CORRECT");
            score.incrementNumCorrectFirstAttempt();
            return;
        }

        handleSecondAttempt(expectedAnswer);
    }

    private void handleSecondAttempt(String expectedAnswer)
    {
        System.out.println("INCORRECT\nOne more guess:");
        String  userAnswer      = scanner.nextLine();
        boolean answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

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
}