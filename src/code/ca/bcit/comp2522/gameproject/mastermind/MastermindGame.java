package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import ca.bcit.comp2522.gameproject.Playable;

/**
 * Main controller for the Mastermind game.
 * <p>
 * This class coordinates all game components and manages the main game loop,
 * including handling player input, tracking rounds, and managing deception.
 * </p>
 *
 * @author Nathan O
 * @version 1.3 2025
 */
public final class MastermindGame implements
                                  Playable
{
    private static final int          MAX_ROUNDS    = 12;
    private static final int          CODE_LENGTH   = 4;
    private static final TruthScanner TRUTH_SCANNER = new TruthScanner();
    private static final String       YES           = "yes";

    private static final int INCREMENT = 1;

    private static final String SEPARATOR_LINE      = "----------------------------------------";
    private static final String GAME_OVER_SEPARATOR = "=========== GAME OVER ============";
    private static final String NEW_GAME_SEPARATOR  = "+++++++++++ NEW GAME +++++++++++";

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

    private List<Round> rounds;
    private SecretCode  secretCode;

    /**
     * Constructs a new MastermindGame.
     */
    public MastermindGame()
    {
    }

    /**
     * Starts and manages the game session, allowing for multiple games.
     */
    @Override
    public void play()
    {
        if(! handleGameIntroduction())
        {
            return;
        }

        do
        {
            initializeNewGame();
            playGameLoop();
            endGame();
        } while(askPlayAgain());

        System.out.println("\n" + SEPARATOR_LINE);
        System.out.println("Returning to main menu...");
        System.out.println(SEPARATOR_LINE + "\n");

        InputHandler.closeScanner();
    }

    /*
     * Initializes the state for a new game.
     * Resets rounds, generates a new secret code, and resets counters.
     */
    private void initializeNewGame()
    {
        rounds     = new ArrayList<>();
        secretCode = SecretCode.generateRandomCode(CODE_LENGTH);
        Round.resetDeceptiveRounds();
        TRUTH_SCANNER.resetTruthScanner();
        System.out.println("\n" + NEW_GAME_SEPARATOR);
    }

    /*
     * Handles the initial game introduction and rules display.
     *
     * @return true if the player is ready to start, false otherwise.
     */
    private static boolean handleGameIntroduction()
    {
        final String response;
        final String ready;

        System.out.println("\n" + SEPARATOR_LINE);
        System.out.println("Welcome to Mastermind!");
        System.out.println(SEPARATOR_LINE);
        System.out.print("Have you played this version before? (yes/no): ");

        response = InputHandler.getYesNoResponse();

        if(! response.equalsIgnoreCase(YES))
        {
            System.out.println(RULES);
            ready = InputHandler.getYesNoResponse();

            if(! ready.equalsIgnoreCase(YES))
            {
                System.out.println("\nMaybe next time! Goodbye.\n");
                return false;
            }
        }

        System.out.println("\n" + SEPARATOR_LINE);
        System.out.println("Try to guess the " + CODE_LENGTH + "-digit code.");
        System.out.println("You have " + MAX_ROUNDS + " attempts.");
        System.out.println(SEPARATOR_LINE);
        return true;
    }

    /*
     * Manages the main loop of the game, playing rounds until the game is over.
     */
    private void playGameLoop()
    {
        while(! isGameOver())
        {
            playRound();
        }
    }

    /*
     * Plays a single round of the game.
     * Handles player input (guess, scan, summary) and processes the guess.
     */
    private final void playRound()
    {
        final int roundNumber = rounds.size() + INCREMENT;
        System.out.printf("%n--- Round %d of %d ---%n",
                          roundNumber,
                          MAX_ROUNDS);

        final PlayerAction action = handlePlayerInput();

        if(action instanceof PlayerGuessCode playerGuess)
        {
            processGuess(playerGuess);
        }
        else if(action instanceof TruthScanRequest)
        {
            System.out.println("(Continuing round after Truth Scan...)");
        }
        else if(action instanceof GuessSummaryRequest)
        {
            System.out.println("(Continuing round after Guess Summary...)");
        }
    }

    /*
     * Processes a player's guess asynchronously.
     * Creates feedback and a new round object in a background thread,
     * then waits for the result before adding it to the history and displaying
     * feedback.
     *
     * @param guess The player's guess code for the current round.
     */
    private void processGuess(final PlayerGuessCode guess)
    {
        final int                      nextRoundNumber;
        final CompletableFuture<Round> roundFuture;

        nextRoundNumber = rounds.size() + INCREMENT;

        roundFuture = CompletableFuture.supplyAsync(() ->
        {
            final Feedback actualFeedback;
            final Round    calculatedRound;

            actualFeedback  = new Feedback(secretCode,
                                           guess);
            calculatedRound = new Round(nextRoundNumber,
                                        guess,
                                        actualFeedback);
            return calculatedRound;
        });

        try
        {
            final Round    thisRound;
            final Feedback displayFeedback;

            thisRound = roundFuture.join();
            rounds.add(thisRound);

            displayFeedback = thisRound.getFeedback();
            System.out.println("\nFeedback: " + displayFeedback);
        }
        catch(final CompletionException e)
        {
            System.err.println("Error during feedback calculation: " +
                               e.getCause());
        }
        catch(final Exception e)
        {
            System.err.println("Unexpected error processing guess: " +
                               e.getMessage());
        }
    }

    /*
     * Handles player input within a round.
     * Loops until a valid action (guess, scan, summary) is received.
     * Processes scan and summary requests directly.
     *
     * @return The PlayerAction representing the player's choice (only
     * PlayerGuessCode exits loop).
     */
    private final PlayerAction handlePlayerInput()
    {
        while(true)
        {
            final PlayerAction input;
            input = InputHandler.getPlayerInput();

            if(input instanceof TruthScanRequest)
            {
                handleTruthScanAction();
            }
            else if(input instanceof PlayerGuessCode guess)
            {
                return guess;
            }
            else if(input instanceof GuessSummaryRequest)
            {
                handleGuessSummaryAction();
            }
            else
            {
                System.err.println("Input error detected. Please try again or restart.");
            }
        }
    }

    /*
     * Handles the action when a Truth Scan is requested by the player.
     * Invokes the truth scanner and prints appropriate messages.
     */
    private final void handleTruthScanAction()
    {
        System.out.println("\n--- Truth Scan Requested ---");
        final boolean scanSuccess = TRUTH_SCANNER.handleTruthScanRequest(rounds,
                                                                         secretCode);
        if(scanSuccess)
        {
            System.out.println("--- Truth Scan Complete ---");
        }
        else
        {
            System.out.println("--- Truth Scan Failed ---");
        }
    }

    /*
     * Handles the action when a Guess Summary is requested by the player.
     * Calls the method to print the summary.
     */
    private final void handleGuessSummaryAction()
    {
        printGuessSummary();
    }

    /*
     * Checks if the guess in a given round matches the secret code.
     *
     * @param round The round to check.
     * 
     * @return true if the guess is correct, false otherwise.
     */
    private final boolean isCorrectGuess(final Round round)
    {
        final Feedback actualFeedback;
        final boolean  isCorrectGuess;

        actualFeedback = new Feedback(secretCode,
                                      round.getGuess());
        isCorrectGuess = actualFeedback.getCorrectPositionCount() ==
                         CODE_LENGTH;

        return isCorrectGuess;
    }

    /*
     * Determines if the game has ended.
     * The game ends if the last guess was correct or max rounds are reached.
     *
     * @return true if the game is over, false otherwise.
     */
    private final boolean isGameOver()
    {
        if(rounds.isEmpty())
        {
            return false;
        }

        final Round   lastRound;
        final boolean maxRoundsReached;

        lastRound = rounds.get(rounds.size() - INCREMENT);

        if(isCorrectGuess(lastRound))
        {
            return true;
        }

        maxRoundsReached = rounds.size() >= MAX_ROUNDS;

        return maxRoundsReached;
    }

    /*
     * Handles the end-of-game sequence.
     * Displays win/loss message, secret code, and deceptive round count.
     */
    private final void endGame()
    {
        System.out.println("\n" + GAME_OVER_SEPARATOR);

        if(rounds.isEmpty())
        {
            System.out.println("Game ended without any guesses.");
            System.out.println(GAME_OVER_SEPARATOR);
            return;
        }

        final Round lastRound;
        final int   roundsPlayed;

        lastRound    = rounds.get(rounds.size() - INCREMENT);
        roundsPlayed = rounds.size();

        if(isCorrectGuess(lastRound))
        {
            System.out.println(String.format(WIN_MESSAGE,
                                             roundsPlayed));
        }
        else
        {
            System.out.println(String.format(GAME_OVER_MESSAGE,
                                             secretCode));
        }
        System.out.println(GAME_OVER_SEPARATOR);
    }

    /*
     * Asks the player if they want to play another game.
     *
     * @return true if the player wants to play again, false otherwise.
     */
    private final boolean askPlayAgain()
    {
        System.out.print("\nPlay again? (yes/no): ");

        final String response;
        response = InputHandler.getYesNoResponse();

        return response.equalsIgnoreCase(YES);
    }

    /*
     * Prints a summary of all previous guesses and their feedback.
     * Displays the actual feedback for rounds where truth was revealed.
     */
    private final void printGuessSummary()
    {
        System.out.println("\n--- Guess Summary ---");
        if(rounds.isEmpty())
        {
            System.out.println("No guesses made yet.");
        }
        else
        {
            for(final Round round : rounds)
            {
                System.out.println(round.toString());
            }
        }
        System.out.println("--- End Summary ---");
    }
}