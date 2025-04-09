package ca.bcit.comp2522.gameproject.wordgame;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ca.bcit.comp2522.gameproject.interfaces.RoundBased;

/**
 * Launcher for the Word Game that implements the Playable interface.
 * <p>
 * This class manages the game flow, including asking questions, validating
 * answers, tracking scores, and handling game progression.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class WordGame implements
                            RoundBased

{
    private static final int NUM_QUESTIONS_PER_GAME = 10;
    private static final int NUM_QUESTION_TYPES     = 3;
    private static final int NUM_NO_QUESTIONS_ASKED = 0;

    private static final String OPTION_YES = "yes";
    private static final String OPTION_NO  = "no";

    private static final String FILE_SCORE = "score.txt";

    static final int QUESTION_CAPITAL_CITY = 0;
    static final int QUESTION_COUNTRY      = 1;
    static final int QUESTION_FACT         = 2;

    private static final Scanner scan = new Scanner(System.in);

    private final World         world;
    private final Score         currentScore;
    private final AnswerChecker answerChecker;
    private final Set<Country>  countriesUsed;

    /**
     * Constructs a new WordGame, initializing required components including
     * world data.
     */
    public WordGame()
    {
        this.world         = new World();
        this.currentScore  = new Score();
        this.answerChecker = new AnswerChecker(scan,
                                               currentScore);
        this.countriesUsed = new HashSet<>();
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
        System.out.println("""
                           ----------------------------------------
                           WORLD TRIVIA GAME
                           ----------------------------------------
                           Test your knowledge about countries,
                           capitals, and facts.
                           """);

        do
        {
            resetCountriesUsed();
            playOneGame();
            currentScore.incrementNumGamesPlayed();
            currentScore.printScore();
        } while(shouldPlayAgain());

        concludeGame();
    }

    /**
     * Plays a single game consisting of multiple questions.
     * <p>
     * This method runs through one complete game cycle, asking the configured
     * number of questions.
     * </p>
     */
    @Override
    public void playOneGame()
    {
        for(int questionsAsked = NUM_NO_QUESTIONS_ASKED; questionsAsked <=
                                                         NUM_QUESTIONS_PER_GAME; questionsAsked++)
        {
            System.out.printf("----------Question %d/10----------\n",
                              questionsAsked);
            playOneRound();
        }

        System.out.println("----------GAME OVER----------");
    }

    /**
     * Asks a single question and validates the player's answer.
     * <p>
     * This method selects a random country and question type, creates the
     * appropriate question, and processes the player's response. It checks
     * if the country has already been used in the game and if so, it selects
     * a new country.
     * </p>
     */
    @Override
    public void playOneRound()
    {
        final int      questionStyle;
        final Question question;
        final String   questionPrompt;
        final String   questionAnswer;
        
        Country  thisCountry;
        thisCountry = world.getRandomCountry();

        while(countriesUsed.contains(thisCountry))
        {
            thisCountry = world.getRandomCountry();
        }

        questionStyle  = (int)(Math.random() * NUM_QUESTION_TYPES);
        question       = QuestionFactory.createQuestion(thisCountry,
                                                        questionStyle);
        questionPrompt = question.getPrompt();
        questionAnswer = question.getExpectedAnswer();

        countriesUsed.add(thisCountry);

        System.out.println(questionPrompt);
        answerChecker.checkAnswer(questionAnswer);
    }

    /**
     * Handles end-of-game operations.
     * <p>
     * This method displays the high score message and saves the current score
     * to a file.
     * </p>
     */
    @Override
    public void concludeGame()
    {
        try
        {
            Score.displayHighScoreMessage(currentScore,
                                          FILE_SCORE);
            Score.appendScoreToFile(currentScore,
                                    FILE_SCORE);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Determines if the player wants to play another game.
     * <p>
     * This method prompts the user and validates their response to ensure
     * it's either {@value #OPTION_YES} or {@value #OPTION_NO}.
     * </p>
     * @return true if the player wants to play again, false otherwise
     */
    private boolean shouldPlayAgain()
    {
        final boolean playAgainChoice;
        String        userChoice;

        do
        {
            System.out.printf("\nWould you like to play again? (%s or %s)",
                              OPTION_YES,
                              OPTION_NO);
            userChoice = scan.nextLine()
                             .toLowerCase();
            if(!userChoice.equalsIgnoreCase(OPTION_YES) &&
               !userChoice.equalsIgnoreCase(OPTION_NO))
            {
                System.out.printf("Invalid input. Please enter %s or %s.",
                                  OPTION_YES,
                                  OPTION_NO);
            }
        } while(!userChoice.equalsIgnoreCase(OPTION_YES) &&
                !userChoice.equalsIgnoreCase(OPTION_NO));

        playAgainChoice = userChoice.equalsIgnoreCase(OPTION_YES);
        return playAgainChoice;
    }

    /*
     * Resets the countries used set to an empty state.
     */
    private void resetCountriesUsed()
    {
        countriesUsed.clear();
    }
}
