package ca.bcit.comp2522.gameproject.mastermind;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import ca.bcit.comp2522.gameproject.Replayable;
import ca.bcit.comp2522.gameproject.mastermind.GameHistoryManager.GameSessionRecord;


/**
 * Main controller for the Mastermind game.
 * <p>
 * This class coordinates all game components and manages the main game loop,
 * including handling player input, tracking rounds, managing deception,
 * and interacting with game history.
 * </p>
 *
 * @author Nathan O
 * @version 1.4 2025
 */
public final class MastermindGame implements Replayable
{
    private static final int          MAX_ROUNDS          = 12;
    private static final int          CODE_LENGTH         = 4;
    private static final TruthScanner TRUTH_SCANNER       = new TruthScanner();
    private static final String       YES                 = "yes";
    private static final int          DEFAULT_MENU_CHOICE = - 1;
    private static final int          ROUND_INCREMENT     = 1;

    private static final String SEPARATOR_LINE      = "----------------------------------------";
    private static final String GAME_OVER_SEPARATOR = "=========== GAME OVER ============";
    private static final String NEW_GAME_SEPARATOR  = "+++++++++++ NEW GAME +++++++++++";
    private static final String HISTORY_SEPARATOR   = "*********** HISTORY ************";

    private static final String GAME_OVER_MESSAGE = "Game Over! The secret code was: %s";
    private static final String WIN_MESSAGE       = "Congratulations! You won in %d rounds!";
    private static final String WON_OUTCOME       = "Won";
    private static final String LOST_OUTCOME      = "Lost";

    private static final String RULES = """
                                        === MASTERMIND GAME RULES ===
                                        1. The computer will generate a secret code of 4 digits (1-6).
                                        2. You have 12 attempts to guess the code correctly.
                                        3. After each guess, you'll receive feedback:
                                           - Number of digits in the correct position
                                           - Number of correct digits in the wrong position

                                        SPECIAL MECHANICS:
                                        * Deceptive Rounds: Up to 3 rounds may give slightly altered feedback, it's up to you
                                          to discern whether the feedback is truthful or not.
                                        * Truth Scan: Once per game, you can reveal the true feedback of a
                                          previous round. Use this wisely!

                                        HOW TO PLAY:
                                        - Enter a 4-digit guess (digits 1-6).
                                        - Enter 't' to use your Truth Scan.
                                        - Enter 'g' to view a summary of your previous guesses.

                                        EXAMPLE:
                                        Secret Code: 1234
                                        Your Guess:  1356
                                        Feedback: Correct positions: 1, Misplaced: 1
                                        (1 is correct position, 3 is right digit wrong position)

                                        Are you ready to start? (yes/no): """;

    private static final int OPTION_PLAY         = 1;
    private static final int OPTION_VIEW_HISTORY = 2;
    private static final int OPTION_EXIT         = 3;

    private static final int HISTORY_OPTION_ALL  = 1;
    private static final int HISTORY_OPTION_WON  = 2;
    private static final int HISTORY_OPTION_LOST = 3;
    private static final int HISTORY_OPTION_BACK = 4;

    private final GameHistoryManager gameHistoryManager;
    private final Scanner            inputScanner;

    private List<Round> rounds;
    private SecretCode  secretCode;
    private String      truthScanInfoForHistory;

    /**
     * Constructs a new MastermindGame.
     */
    public MastermindGame()
    {
        this.gameHistoryManager = new GameHistoryManager();
        this.inputScanner       = new Scanner(System.in);
    }

    /**
     * Presents the main menu and manages the overall application flow.
     */
    @Override
    public void play()
    {
        int choice;
        do
        {
            choice = showMainMenu();

            switch(choice)
            {
                case OPTION_PLAY -> playOneGame();
                case OPTION_VIEW_HISTORY -> handleViewHistoryMenu();
                case OPTION_EXIT -> {
                    System.out.println("\n" + SEPARATOR_LINE);
                    System.out.println("Exiting Mastermind. Goodbye!");
                    System.out.println(SEPARATOR_LINE + "\n");
                }
                default -> System.err.println("Unexpected main menu choice.");
            }
        } while(choice != OPTION_EXIT);
    }

    /**
     * Handles playing a complete game session, including intro and saving.
     */
    @Override
    public void playOneGame()
    {
        if(!handleGameIntroduction())
        {
            return;
        }

        initializeNewGame();
        playGameLoop();
        endGame();
    }

    /*
     * Displays the main menu and gets the user's choice.
     *
     * @return The valid menu choice (1, 2, or 3).
     */
    private int showMainMenu()
    {
        int choice = DEFAULT_MENU_CHOICE;

        System.out.println("\n" + SEPARATOR_LINE);
        System.out.println("MASTERMIND MAIN MENU");
        System.out.println(SEPARATOR_LINE);
        System.out.println(OPTION_PLAY + ". Play Game");
        System.out.println(OPTION_VIEW_HISTORY + ". View Game History");
        System.out.println(OPTION_EXIT + ". Exit");
        System.out.println(SEPARATOR_LINE);

        while(choice < OPTION_PLAY || choice > OPTION_EXIT)
        {
            System.out.print("Enter your choice: ");
            try
            {
                choice = inputScanner.nextInt();
                if(choice < OPTION_PLAY || choice > OPTION_EXIT)
                {
                    System.out.println("Invalid choice. Please enter " +
                                       OPTION_PLAY +
                                       ", " +
                                       OPTION_VIEW_HISTORY +
                                       ", or " +
                                       OPTION_EXIT +
                                       ".");
                }
            }
            catch(final InputMismatchException error)
            {
                System.out.println("Invalid input. Please enter a number.");
                inputScanner.next();
            }
        }
        inputScanner.nextLine();
        return choice;
    }

    /*
     * Handles the View History menu and displays selected game records.
     */
    private void handleViewHistoryMenu()
    {
        int choice;
        do
        {
            choice = showHistorySubMenu();
            List<GameSessionRecord> history;

            switch(choice)
            {
                case HISTORY_OPTION_ALL -> {
                    System.out.println("\n--- All Game History ---");
                    history = gameHistoryManager.loadGameHistory();
                    displayHistory(history);
                }
                case HISTORY_OPTION_WON -> {
                    System.out.println("\n--- Won Games History ---");
                    history = gameHistoryManager.loadGameHistory();
                    displayHistory(gameHistoryManager.filterHistoryByOutcome(history,
                                                                             WON_OUTCOME));
                }
                case HISTORY_OPTION_LOST -> {
                    System.out.println("\n--- Lost Games History ---");
                    history = gameHistoryManager.loadGameHistory();
                    displayHistory(gameHistoryManager.filterHistoryByOutcome(history,
                                                                             LOST_OUTCOME));
                }
                case HISTORY_OPTION_BACK -> {
                }
                default -> System.err.println("Unexpected history menu choice.");
            }
        } while(choice != HISTORY_OPTION_BACK);
    }

    /*
     * Displays the history sub-menu and gets the user's choice.
     *
     * @return The valid menu choice (1-4).
     */
    private int showHistorySubMenu()
    {
        int choice = DEFAULT_MENU_CHOICE;

        printHistorySubMenuOptions();

        while(choice < HISTORY_OPTION_ALL || choice > HISTORY_OPTION_BACK)
        {
            System.out.print("Enter your choice: ");
            try
            {
                choice = inputScanner.nextInt();
                if(choice < HISTORY_OPTION_ALL || choice > HISTORY_OPTION_BACK)
                {
                    System.out.println("Invalid choice. Please enter a number between " +
                                       HISTORY_OPTION_ALL +
                                       " and " +
                                       HISTORY_OPTION_BACK +
                                       ".");
                }
            }
            catch(final InputMismatchException e)
            {
                System.out.println("Invalid input. Please enter a number.");
                inputScanner.next();
            }
        }
        inputScanner.nextLine();
        return choice;
    }

    /**
     * Prints the history sub-menu options.
     */
    private static void printHistorySubMenuOptions()
    {
        System.out.println("\n" + HISTORY_SEPARATOR);
        System.out.println("VIEW GAME HISTORY");
        System.out.println(SEPARATOR_LINE);
        System.out.println(HISTORY_OPTION_ALL + ". View All History");
        System.out.println(HISTORY_OPTION_WON + ". View Won Games");
        System.out.println(HISTORY_OPTION_LOST + ". View Lost Games");
        System.out.println(HISTORY_OPTION_BACK + ". Back to Main Menu");
        System.out.println(SEPARATOR_LINE);
    }

    /*
     * Helper method to display a list of game session records.
     *
     * @param historyList The list of records to display.
     */
    private void displayHistory(final List<GameSessionRecord> historyList)
    {
        if(historyList == null || historyList.isEmpty())
        {
            System.out.println("No matching game history found.");
        }
        else
        {
            for(final GameSessionRecord record : historyList)
            {
                System.out.println(HISTORY_SEPARATOR);
                System.out.print(record.toString());
                System.out.println(HISTORY_SEPARATOR);
                System.out.println();
            }
            System.out.println("End of history view.");
        }
        System.out.println(SEPARATOR_LINE);
        System.out.print("Press Enter to continue...");
        inputScanner.nextLine();
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
        truthScanInfoForHistory = "Not Used";
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

        if(!response.equalsIgnoreCase(YES))
        {
            System.out.println(RULES);
            ready = InputHandler.getYesNoResponse();

            if(!ready.equalsIgnoreCase(YES))
            {
                System.out.println("\nMaybe next time! Returning to main menu...\n");
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
        while(!isGameOver())
        {
            playRound();
        }
    }

    /*
     * Plays a single round of the game.
     * Handles player input (guess, scan, summary) and processes the guess.
     */
    private void playRound()
    {
        final int roundNumber = rounds.size() + ROUND_INCREMENT;
        System.out.printf("%n--- Round %d of %d ---%n",
                          roundNumber,
                          MAX_ROUNDS);

        final PlayerAction action;
        action = handlePlayerInput();

        if(action instanceof PlayerGuessCode playerGuess)
        {
            processGuess(playerGuess);
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

        nextRoundNumber = rounds.size() + ROUND_INCREMENT;

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
    private PlayerAction handlePlayerInput()
    {
        while(true)
        {
            promptForInput();

            final PlayerAction input;
            try
            {
                input = InputHandler.getPlayerInput();
            }
            catch(final InvalidGuessException e)
            {
                System.err.println(e.getMessage());
                System.out.println("Please try again. Enter 4 digits (1-6), 't' for truth scan, or 'g' for summary.");
                continue;
            }

            if(input == null)
            {
                System.err.println("Received unexpected null input. Please try again.");
                continue;
            }

            if(input instanceof PlayerAction.TruthScanRequest)
            {
                handleTruthScanAction();
            }
            else if(input instanceof PlayerAction.GuessSummaryRequest)
            {
                handleGuessSummaryAction();
            }
            else if(input instanceof PlayerGuessCode guess)
            {
                return guess;
            }
            else
            {
                System.err.println("Input error detected. Please try again or restart.");
            }
        }
    }

    /*
     * Prints the prompt for player input.
     */
    private static void promptForInput()
    {
        System.out.println(SEPARATOR_LINE);
        System.out.print("Enter your guess: ");
    }

    /*
     * Handles the action when a Truth Scan is requested by the player.
     * Invokes the truth scanner and prints appropriate messages.
     * Updates the truthScanInfoForHistory field if successful.
     */
    private void handleTruthScanAction()
    {
        System.out.println("\n--- Truth Scan Requested ---");
        final String scanResultInfo = TRUTH_SCANNER.handleTruthScanRequestAndGetInfo(rounds,
                                                                                     secretCode);

        if(scanResultInfo != null)
        {
            this.truthScanInfoForHistory = scanResultInfo;
            System.out.println("--- Truth Scan Complete ---");
        }
        else
        {
            System.out.println("--- Truth Scan Failed or Cancelled ---");
        }
    }

    /*
     * Handles the action when a Guess Summary is requested by the player.
     * Calls the method to print the summary.
     */
    private void handleGuessSummaryAction()
    {
        printGuessSummary();
    }

    /*
     * Checks if the guess in a given round matches the secret code.
     *
     * @param round The round to check.
     * @return true if the guess is correct, false otherwise.
     */
    private boolean isCorrectGuess(final Round round)
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
    private boolean isGameOver()
    {
        if(rounds.isEmpty())
        {
            return false;
        }

        final Round   lastRound;
        final boolean maxRoundsReached;

        lastRound = rounds.get(rounds.size() - ROUND_INCREMENT);

        if(isCorrectGuess(lastRound))
        {
            return true;
        }

        maxRoundsReached = rounds.size() >= MAX_ROUNDS;

        return maxRoundsReached;
    }

    /*
     * Handles the end-of-game sequence.
     * Displays win/loss message, secret code, and saves the game history.
     */
    private void endGame()
    {
        System.out.println("\n" + GAME_OVER_SEPARATOR);

        final String        outcome;
        final LocalDateTime endTime = LocalDateTime.now();

        if(rounds.isEmpty())
        {
            System.out.println("Game ended without any guesses.");
            outcome = LOST_OUTCOME;
        }
        else
        {
            final Round lastRound;
            final int   roundsPlayed;

            lastRound    = rounds.get(rounds.size() - ROUND_INCREMENT);
            roundsPlayed = rounds.size();

            if(isCorrectGuess(lastRound))
            {
                System.out.printf(WIN_MESSAGE + "%n",
                                  roundsPlayed);
                outcome = WON_OUTCOME;
            }
            else
            {
                System.out.printf(GAME_OVER_MESSAGE + "%n",
                                  secretCode);
                outcome = LOST_OUTCOME;
            }
        }

        System.out.println(GAME_OVER_SEPARATOR);

        if(!rounds.isEmpty())
        {
            saveCurrentGameToHistory(endTime,
                                     outcome);
        }
    }

    /*
     * Helper method to collect game data and save it using GameHistoryManager.
     *
     * @param endTime The timestamp when the game ended.
     * @param outcome The result of the game ("Won" or "Lost").
     */
    private void saveCurrentGameToHistory(final LocalDateTime endTime,
                                          final String outcome)
    {
        final List<String>      roundDetails;
        final GameSessionRecord record;

        roundDetails = rounds.stream()
                             .map(Round::toString)
                             .collect(Collectors.toList());
        record       = new GameSessionRecord(endTime,
                                             roundDetails,
                                             this.truthScanInfoForHistory,
                                             outcome);

        System.out.println("Saving game history...");
        gameHistoryManager.saveGameHistory(record);
        System.out.println("Game history saved.");
    }

    /*
     * Prints a summary of all previous guesses and their feedback.
     * Displays the actual feedback for rounds where truth was revealed.
     */
    private void printGuessSummary()
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