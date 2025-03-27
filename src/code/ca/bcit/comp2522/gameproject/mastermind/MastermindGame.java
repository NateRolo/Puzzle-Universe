package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.bcit.comp2522.gameproject.Playable;

/**
 * Main controller for the Mastermind game.
 * <p>
 * This class coordinates all game components and manages the main game loop,
 * including handling player input, tracking rounds, and managing deception.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class MastermindGame implements
                                  Playable
{
    // Game Configuration
    private static final int MAX_ROUNDS           = 12;
    private static final int CODE_LENGTH          = 4;
    private static final int MAX_DECEPTIVE_ROUNDS = 3;

    // Counter Constants
    private static final int MIN_COUNT        = 0;
    private static final int MIN_ROUND_NUMBER = 1;
    private static final int INCREMENT        = 1;

    // Message Templates
    private static final String GAME_OVER_MESSAGE = "Game Over! The secret code was: %s";
    private static final String WIN_MESSAGE       = "Congratulations! You won in %d rounds!";

    private static final String RULES = """
                                        === MASTERMIND GAME RULES ===
                                        1. The computer will generate a secret code of 4 digits (1-6).
                                        2. You have 12 attempts to guess the code correctly.
                                        3. After each guess, you'll receive feedback:
                                           - Number of digits in the correct position
                                           - Number of correct digits in the wrong position

                                        SPECIAL MECHANICS:
                                        * Deceptive Rounds: Up to 3 rounds may give slightly altered feedback
                                          (marked with a '?')
                                        * Truth Scan: Once per game, you can reveal the true feedback of a
                                          previous round. Use this wisely!

                                        EXAMPLE:
                                        Secret Code: 1234
                                        Your Guess: 1356
                                        Feedback: Correct positions: 1, Misplaced: 1
                                        (1 is correct position, 3 is right digit wrong position)

                                        Are you ready to start? (yes/no): """;

    private final List<Round>  rounds;
    private final InputHandler inputHandler;
    private final SecretCode   secretCode;
    private final Scanner      scanner;

    private int     deceptiveRoundsUsed;
    private boolean truthScanUsed;

    /**
     * Constructs a new MastermindGame.
     */
    public MastermindGame()
    {
        scanner             = new Scanner(System.in);
        inputHandler        = new InputHandler(scanner);
        rounds              = new ArrayList<>();
        secretCode          = SecretCode.generateRandomCode(CODE_LENGTH);
        deceptiveRoundsUsed = 0;
        truthScanUsed       = false;
    }

    /**
     * Starts and manages the game session.
     */
    @Override
    public void play()
    {
        if(! handleGameIntroduction())
        {
            return;
        }

        playGameLoop();
        endGame();
    }

    /*
     * Handles the game introduction and rules explanation.
     */
    private boolean handleGameIntroduction()
    {
        System.out.println("Welcome to Mastermind!");
        System.out.println("Have you played this game before? (yes/no):");

        String response = getYesNoResponse("Please enter either 'yes' or 'no':");

        if(! response.equals("yes"))
        {
            System.out.println(RULES);
            String ready = getYesNoResponse("Please enter either 'yes' or 'no':");

            if(! ready.equals("yes"))
            {
                System.out.println("Maybe next time! Goodbye.");
                return false;
            }
        }

        System.out.println("\nTry to guess the " + CODE_LENGTH + "-digit code. You have " + MAX_ROUNDS + " attempts.");
        return true;
    }

    /*
     * Manages the main game loop.
     */
    private void playGameLoop()
    {
        while(!isGameOver())
        {
            playRound();
        }
    }

    /**
     * Gets a yes/no response from the user.
     *
     * @param promptMessage the message to display if input is invalid
     * @return the user's response (either "yes" or "no")
     */
    private String getYesNoResponse(final String promptMessage)
    {
        String response;
        do
        {
            response = scanner.nextLine()
                              .trim()
                              .toLowerCase();
            if(! response.equals("yes") && ! response.equals("no"))
            {
                System.out.println(promptMessage);
            }
        } while(! response.equals("yes") && ! response.equals("no"));

        return response;
    }

    /*
     * Plays a single round of the game.
     */
    private void playRound()
    {
        System.out.println("\nRound " + (rounds.size() + INCREMENT));

        final Code guess;
        final Feedback feedback;
        final Round round;

        guess = handlePlayerInput();

        if(guess == null)
        {
            return;
        }

        feedback = generateFeedback(guess);
        round    = new Round(rounds.size() + INCREMENT,
                                            guess,
                                            feedback,
                                            isDeceptiveRound(feedback));

        rounds.add(round);
        System.out.println(feedback);
    }

    /*
     * Handles player input including truth scan requests.
     */
    private Code handlePlayerInput()
    {
        Code guess = null;
        while(guess == null)
        {
            final PlayerGuessCode input = inputHandler.getPlayerInput();

            if(input.isTruthScanRequest())
            {
                if(! handleTruthScanRequest())
                {
                    continue;
                }
            }
            else
            {
                guess = input;
            }
        }
        return guess;
    }

    /*
     * Handles a truth scan request.
     */
    private boolean handleTruthScanRequest()
    {
        if(truthScanUsed)
        {
            System.out.println("You have already used your truth scan!");
            return false;
        }
        if(rounds.isEmpty())
        {
            System.out.println("No previous rounds to scan!");
            return false;
        }

        useTruthScan();
        return true;
    }

    /*
     * Evaluates a guess against the secret code.
     */
    private Feedback evaluateGuess(final Code guess)
    {
        final List<Integer> secretDigits;
        final List<Integer> guessDigits;
        final List<Integer> secretCopy;
        final List<Integer> guessCopy;
        int                 correctPosition;
        int                 misplaced;

        secretDigits = secretCode.getDigits();
        guessDigits  = guess.getDigits();

        correctPosition = MIN_COUNT;
        misplaced       = MIN_COUNT;

        // Count correct positions
        for(int i = 0; i < CODE_LENGTH; i++)
        {
            if(secretDigits.get(i)
                           .equals(guessDigits.get(i)))
            {
                correctPosition++;
            }
        }

        secretCopy = new ArrayList<>(secretDigits);
        guessCopy  = new ArrayList<>(guessDigits);

        for(int i = 0; i < CODE_LENGTH; i++)
        {
            if(secretCopy.contains(guessCopy.get(i)))
            {
                misplaced++;
                secretCopy.remove(guessCopy.get(i));
            }
        }

        // Adjust for double counting
        misplaced = misplaced - correctPosition;

        return new Feedback(correctPosition,
                            misplaced,
                            false);
    }

    /*
     * Handles the truth scan feature.
     */
    private void useTruthScan()
    {
        final int   roundNumber;
        final Round round;

        roundNumber = inputHandler.getRoundNumberForScan(rounds.size());
        round       = rounds.get(roundNumber - INCREMENT);

        if(round.isDeceptiveRound())
        {
            final Feedback trueFeedback = evaluateGuess(round.getGuess());
            System.out.println("True feedback for round " + roundNumber + ":");
            System.out.println(trueFeedback);
        }
        else
        {
            System.out.println("This round was not deceptive!");
        }

        truthScanUsed = true;
    }

    /*
     * Checks if the game is over (win or max rounds reached).
     */
    private boolean isGameOver()
    {
        if(rounds.isEmpty())
        {
            return false;
        }

        final Round    lastRound;
        final Feedback lastFeedback;
        
        lastRound    = rounds.get(rounds.size() - 1);
        lastFeedback = lastRound.getFeedback();

        return lastFeedback.getCorrectPositionCount() == CODE_LENGTH || rounds.size() >= MAX_ROUNDS;
    }
    
    /*
     * Handles game end conditions and displays final message.
     */
    private void endGame()
    {
        if(rounds.isEmpty())
        {
            System.out.println("Game ended without any guesses.");
            return;
        }

        final Round lastRound;
        final Feedback lastFeedback;
        
        lastRound = rounds.get(rounds.size() - INCREMENT);
        lastFeedback = lastRound.getFeedback();

        if(lastFeedback.getCorrectPositionCount() == CODE_LENGTH)
        {
            System.out.println(String.format(WIN_MESSAGE,
                                             rounds.size()));
        }
        else
        {
            System.out.println(String.format(GAME_OVER_MESSAGE,
                                             secretCode));
        }

        System.out.println("Deceptive rounds used: " + deceptiveRoundsUsed);
    }

    /*
     * Generates feedback for the current round.
     */
    private Feedback generateFeedback(final Code guess)
    {
        final Feedback trueFeedback;
        final boolean  isDeceptive;
        
        trueFeedback = evaluateGuess(guess);
        isDeceptive = DeceptionEngine.shouldApplyDeception(deceptiveRoundsUsed,
                                                          MAX_DECEPTIVE_ROUNDS);

        if(isDeceptive)
        {
            deceptiveRoundsUsed++;
            return DeceptionEngine.applyDeception(trueFeedback);
        }

        return trueFeedback;
    }

    /*
     * Checks if a feedback represents a deceptive round.
     */
    private boolean isDeceptiveRound(final Feedback feedback)
    {
        return DeceptionEngine.shouldApplyDeception(deceptiveRoundsUsed,
                                                    MAX_DECEPTIVE_ROUNDS);
    }
}