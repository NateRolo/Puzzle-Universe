package ca.bcit.comp2522.gameproject.wordGame;

import ca.bcit.comp2522.gameproject.Playable;

import java.io.IOException;
import java.util.Scanner;

public class WordGameLauncher implements Playable
{
    private static final int   QUESTIONS_PER_GAME    = 10;
    protected static final int QUESTION_TYPES        = 3;
    protected static final int CAPITAL_CITY_QUESTION = 0;
    protected static final int COUNTRY_QUESTION      = 1;
    protected static final int FACT_QUESTION         = 2;

    private static final Scanner scan;
    private static final World   world;

    private final Score           currentScore;
    private final AnswerValidator answerValidator;

    static
    {
        scan = new Scanner(System.in);

        try
        {
            world = new World();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Failed to load world data.");
        }
    }

    public WordGameLauncher()
    {
        currentScore    = new Score();
        answerValidator = new AnswerValidator(scan,
                                              currentScore);
    }

    @Override
    public void play()
    {
        System.out.println("Starting Word Game...");

        do
        {
            playOneGame();
            currentScore.incrementNumGamesPlayed();
            currentScore.printScore();
        } while(shouldPlayAgain());

        handleGameEnd();
    }

    private void playOneGame()
    {
        for(int questionsAsked = 1; questionsAsked <= QUESTIONS_PER_GAME; questionsAsked++) //magic number
        {
            System.out.printf("----------Question %d/10----------\n",
                              questionsAsked);
            askQuestion();
        }
    }

    private boolean shouldPlayAgain()
    {
        String playAgain;
        do
        {
            System.out.println("Would you like to play again? (yes/no)");
            playAgain = scan.nextLine()
                            .toLowerCase();
            if(!playAgain.equals("yes") && !playAgain.equals("no"))
            {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        } while(!playAgain.equals("yes") && !playAgain.equals("no"));

        return playAgain.equals("yes");
    }

    private void handleGameEnd()
    {
        try
        {
            Score.displayHighScoreMessage(currentScore);
            Score.appendScoreToFile(currentScore,
                                    "score.txt");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void askQuestion()
    {
        final Country thisCountry;
        final int     questionStyle;
        Question      question;

        thisCountry   = world.getRandomCountry();
        questionStyle = (int)(Math.random() * QUESTION_TYPES);
        question      = QuestionFactory.createQuestion(thisCountry,
                                                       questionStyle);

        System.out.println(question.getPrompt());
        answerValidator.validateAnswer(question.getExpectedAnswer());
    }

}
