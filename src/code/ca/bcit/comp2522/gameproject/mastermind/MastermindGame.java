package ca.bcit.comp2522.gameproject.mastermind;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import ca.bcit.comp2522.gameproject.interfaces.RoundBased;
import ca.bcit.comp2522.gameproject.mastermind.GameHistoryManager.GameSessionRecord;
import ca.bcit.comp2522.gameproject.mastermind.UIHandler.HistoryMenuOption;
import ca.bcit.comp2522.gameproject.mastermind.UIHandler.MainMenuOption;

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
public final class MastermindGame implements
                                  RoundBased
{
    private static final int          MAX_ROUNDS      = 12;
    private static final int          ROUND_INCREMENT = 1;
    private static final TruthScanner TRUTH_SCANNER   = new TruthScanner();

    private static final String OUTCOME_WON         = "Won";
    private static final String OUTCOME_LOST        = "Lost";
    private static final String YES                 = "yes";
    static final String         INPUT_TRUTH_SCAN    = "t";
    static final String         INPUT_GUESS_SUMMARY = "g";

    private final GameHistoryManager gameHistoryManager;
    private final UIHandler          uiHandler;


    private List<Round> rounds;
    private SecretCode  secretCode;
    private String      truthScanInfoForHistory;

    /**
     * Constructs a new MastermindGame. Initializes the game history manager and UI handler.
     */
    public MastermindGame()
    {
        final Scanner            scannerForUIHandler;
        scannerForUIHandler = new Scanner(System.in);
        this.gameHistoryManager = new GameHistoryManager();
        this.uiHandler = new UIHandler(scannerForUIHandler);
    }

    /**
     * Presents the main menu and manages the overall application flow.
     */
    @Override
    public void play()
    {
        MainMenuOption choice;
        do
        {
            uiHandler.displayMainMenu();
            choice = uiHandler.getMainMenuChoice();

            switch(choice)
            {
                case PLAY_GAME -> playOneGame();
                case VIEW_HISTORY -> handleViewHistoryMenu();
                case EXIT -> uiHandler.displayExitMessage();
                default -> uiHandler.displayError("Unexpected main menu choice: " +
                                                  choice);
            }
        } while(choice != MainMenuOption.EXIT);
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

        setupNewGame();
        playGameLoop();
        concludeGame();
    }

    /*
     * Handles the View History menu and displays selected game records.
     */
    private void handleViewHistoryMenu()
    {
        HistoryMenuOption choice;
        do
        {
            uiHandler.displayHistorySubMenu();
            choice = uiHandler.getHistoryMenuChoice();
            List<GameSessionRecord> history;

            switch(choice)
            {
                case VIEW_ALL -> {
                    history = gameHistoryManager.loadGameHistory();
                    uiHandler.displayHistory(history,
                                             HistoryMenuOption.VIEW_ALL);
                }
                case VIEW_WON -> {
                    history = gameHistoryManager.loadGameHistory();
                    uiHandler.displayHistory(gameHistoryManager.filterHistoryByOutcome(history,
                                                                                       OUTCOME_WON),
                                             HistoryMenuOption.VIEW_WON);
                }
                case VIEW_LOST -> {
                    history = gameHistoryManager.loadGameHistory();
                    uiHandler.displayHistory(gameHistoryManager.filterHistoryByOutcome(history,
                                                                                       OUTCOME_LOST),
                                             HistoryMenuOption.VIEW_LOST);
                }
                case BACK_TO_MAIN -> {
                }
                default -> uiHandler.displayError("Unexpected history menu choice: " +
                                                  choice);
            }
        } while(choice != HistoryMenuOption.BACK_TO_MAIN);
    }

    /*
     * Initializes the state for a new game.
     * Resets rounds, generates a new secret code, and resets counters.
     */
    public void setupNewGame()
    {
        rounds     = new ArrayList<>();
        secretCode = SecretCode.generateRandomCode(Code.CODE_LENGTH);

        Round.resetDeceptiveRounds();
        TRUTH_SCANNER.resetTruthScanner();
        truthScanInfoForHistory = "Not Used";
    }

    /*
     * Handles the initial game introduction and rules display.
     *
     * @return true if the player is ready to start, false otherwise.
     */
    private boolean handleGameIntroduction()
    {
        uiHandler.displayWelcome();
        uiHandler.promptForPlayedBefore();
        final String response;

        response = GuessHandler.getYesNoResponse();

        if(!response.equalsIgnoreCase(YES))
        {
            displayFormattedRules();
            final String ready = GuessHandler.getYesNoResponse();

            if(!ready.equalsIgnoreCase(YES))
            {
                uiHandler.displayMessage("\nMaybe next time! Returning to main menu...\n");
                return false;
            }
        }

        uiHandler.displayInitialInstructions(Code.CODE_LENGTH,
                                             MAX_ROUNDS);
        return true;
    }

    /*
     * Manages the main loop of the game, playing rounds until the game is over.
     */
    private void playGameLoop()
    {
        while(!isGameOver())
        {
            playOneRound();
        }
    }

    /*
     * Plays a single round of the game.
     * Handles player input (guess, scan, summary) and processes the guess.
     */
    @Override
    public void playOneRound()
    {
        final int          roundNumber;
        final PlayerAction action;

        roundNumber = rounds.size() + ROUND_INCREMENT;
        uiHandler.displayRoundHeader(roundNumber,
                                     MAX_ROUNDS);

        action = handlePlayerInput();

        if(action instanceof PlayerGuessCode playerGuess)
        {
            processGuess(playerGuess);
        }
    }

    /*
     * Handles the end-of-game sequence.
     * Displays win/loss message, secret code, and saves the game history.
     */
    @Override
    public void concludeGame()
    {
        uiHandler.displayGameOverHeader();

        final String        outcome;
        final LocalDateTime endTime;

        endTime = LocalDateTime.now();

        if(rounds.isEmpty())
        {
            uiHandler.displayNoGuessesEndMessage();
            outcome = OUTCOME_LOST;
        }
        else
        {
            final Round lastRound;
            final int   roundsPlayed;

            lastRound    = rounds.get(rounds.size() - ROUND_INCREMENT);
            roundsPlayed = rounds.size();

            if(isCorrectGuess(lastRound))
            {
                uiHandler.displayWinMessage(roundsPlayed);
                outcome = OUTCOME_WON;
            }
            else
            {
                uiHandler.displayLossMessage(secretCode);
                outcome = OUTCOME_LOST;
            }
        }

        uiHandler.displaySeparator();

        if(!rounds.isEmpty())
        {
            saveCurrentGameToHistory(endTime,
                                     outcome);
        }
    }

    /*
     * Determines if the game has ended.
     * The game ends if the last guess was correct or max rounds are reached.
     *
     * @return true if the game is over, false otherwise.
     *
     */
    boolean isGameOver()
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
            uiHandler.displayFeedback(displayFeedback);
        }
        catch(final CompletionException e)
        {
            uiHandler.displayError("Error during feedback calculation: " +
                                   e.getCause());
        }
        catch(final Exception e)
        {
            uiHandler.displayError("Unexpected error processing guess: " +
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
            uiHandler.promptForGuess(INPUT_TRUTH_SCAN,
                                     INPUT_GUESS_SUMMARY);

            final PlayerAction input;
            try
            {
                input = GuessHandler.getPlayerInput();
            }
            catch(final InvalidGuessException e)
            {
                uiHandler.displayError(e.getMessage());
                uiHandler.displayMessage(String.format("Please try again. Enter %d digits (%d-%d), '%s' for truth scan, or '%s' for summary.",
                                                       Code.CODE_LENGTH,
                                                       Code.DIGIT_MIN,
                                                       Code.DIGIT_MAX,
                                                       INPUT_TRUTH_SCAN,
                                                       INPUT_GUESS_SUMMARY));
                continue;
            }

            if(input == null)
            {
                uiHandler.displayError("Received unexpected null input. Please try again.");
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
                uiHandler.displayError("Input error detected. Please try again or restart.");
            }
        }
    }

    /*
     * Handles the action when a Truth Scan is requested by the player.
     * Invokes the truth scanner and prints appropriate messages.
     * Updates the truthScanInfoForHistory field if successful.
     */
    private void handleTruthScanAction()
    {
        uiHandler.displayTruthScanRequested();
        final String scanResultInfo = TRUTH_SCANNER.handleTruthScanRequestAndGetInfo(rounds,
                                                                                     secretCode);

        if(scanResultInfo != null)
        {
            this.truthScanInfoForHistory = scanResultInfo;
            uiHandler.displayTruthScanResult(scanResultInfo);
            uiHandler.displayTruthScanComplete();
        }
        else
        {
            uiHandler.displayTruthScanFailed();
        }
    }

    /*
     * Handles the action when a Guess Summary is requested by the player.
     * Calls the method to print the summary.
     */
    private void handleGuessSummaryAction()
    {
        uiHandler.displayGuessSummarySeparator();
        if(rounds.isEmpty())
        {
            uiHandler.displayNoGuessesMessage();
        }
        else
        {
            for(final Round round : rounds)
            {
                uiHandler.displayGuessSummaryItem(round.toString());
            }
        }
    }

    /*
     * Checks if the guess in a given round matches the secret code.
     *
     * @param round The round to check.
     * 
     * @return true if the guess is correct, false otherwise.
     */
    private boolean isCorrectGuess(final Round round)
    {
        final Feedback actualFeedback;
        final boolean  isCorrectGuess;

        actualFeedback = new Feedback(secretCode,
                                      round.getGuess());
        isCorrectGuess = actualFeedback.getCorrectPositionCount() ==
                         Code.CODE_LENGTH;

        return isCorrectGuess;
    }

    /*
     * Helper method to collect game data and save it using GameHistoryManager.
     *
     * @param endTime The timestamp when the game ended.
     * 
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

        uiHandler.displaySavingHistory();
        gameHistoryManager.saveGameHistory(record);
        uiHandler.displaySaveComplete();
    }

    /*
     * Prints the rules of the game. Uses the RULES constant and the Code and
     * Round classes
     * to format the rules string.
     */
    private void displayFormattedRules()
    {
        uiHandler.displayRules(Code.CODE_LENGTH,
                               Code.DIGIT_MIN,
                               Code.DIGIT_MAX,
                               MAX_ROUNDS,
                               Round.DECEPTIVE_ROUNDS_ALLOWED,
                               INPUT_TRUTH_SCAN,
                               INPUT_GUESS_SUMMARY,
                               Code.EXAMPLE_SECRET,
                               Code.EXAMPLE_GUESS,
                               Code.EXAMPLE_CORRECT_POSITIONS,
                               Code.EXAMPLE_MISPLACED);
    }
}