package ca.bcit.comp2522.gameproject.wordgame;

import java.io.IOException;
import java.util.Scanner;

import ca.bcit.comp2522.gameproject.Playable;

/**
 * Launcher for the Word Game that implements the Playable interface.
 * <p>
 * This class manages the game flow, including asking questions, validating answers,
 * tracking scores, and handling game progression.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class WordGameLauncher implements Playable
{
    private static final   int QUESTIONS_PER_GAME    = 10;
    private static final   int QUESTION_TYPES        = 3;
    static final int CAPITAL_CITY_QUESTION = 0;
    static final int COUNTRY_QUESTION      = 1;
    static final int FACT_QUESTION         = 2;

    private static final Scanner scan;
    private static final World   world;

    private final Score           currentScore;
    private final AnswerChecker answerValidator;

    static
    {
        scan = new Scanner(System.in);

        try
        {
            world = new World();
        }
        catch(final IOException e)
        {
            throw new RuntimeException("Failed to initialize world data", e);
        }
    }

    /**
     * Constructs a new WordGameLauncher with a fresh score and answer validator.
     */
    public WordGameLauncher()
    {
        currentScore    = new Score();
        answerValidator = new AnswerChecker(scan,
                                              currentScore);
    }

    /**
     * Starts and manages the word game play session.
     * <p>
     * This method controls the main game loop, allowing the player to play
     * multiple games until they choose to stop.
     * </p>
     */
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

    /*
     * Plays a single game consisting of multiple questions.
     * <p>
     * This method runs through one complete game cycle, asking the configured
     * number of questions.
     * </p>
     */
    private void playOneGame()
    {
        for(int questionsAsked = 1; questionsAsked <= QUESTIONS_PER_GAME; questionsAsked++) 
        {
            System.out.printf("----------Question %d/10----------\n",
                              questionsAsked);
            askQuestion();
        }
    }

    /*
     * Determines if the player wants to play another game.
     * <p>
     * This method prompts the user and validates their response to ensure
     * it's either "yes" or "no".
     * </p>
     *
     * @return true if the player wants to play again, false otherwise
     */
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

    /*
     * Handles end-of-game operations.
     * <p>
     * This method displays the high score message and saves the current score to a file.
     * </p>
     */
    private void handleGameEnd()
    {
        try
        {
            Score.displayHighScoreMessage(currentScore);
            Score.appendScoreToFile(currentScore,
                                    "score.txt");
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Asks a single question and validates the player's answer.
     * <p>
     * This method selects a random country and question type, creates the appropriate
     * question, and processes the player's response.
     * </p>
     */
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
