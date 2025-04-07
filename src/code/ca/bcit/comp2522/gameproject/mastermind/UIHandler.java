package ca.bcit.comp2522.gameproject.mastermind;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ca.bcit.comp2522.gameproject.mastermind.GameHistoryManager.GameSessionRecord;

/**
 * Handles all console input and output for the Mastermind game.
 * Separates UI concerns from the main game logic.
 *
 * @author Nathan O
 * @version 1.6 2025
 */
final class UIHandler
{
    /** 
     * Represents options available in the main menu.
     * <p>
     * This enum defines all possible actions a player can take from the main menu
     * of the Mastermind game. Each option is associated with a numeric value that
     * corresponds to the user input expected from the console.
     * </p>
     */
    enum MainMenuOption
    {
        PLAY_GAME(1), 
        VIEW_HISTORY(2), 
        EXIT(3), 
        UNKNOWN(-1);

        private final int value;

        /**
         * Constructs a menu option with the specified numeric value.
         *
         * @param value the integer value associated with this menu option
         */
        MainMenuOption(final int value)
        {
            this.value = value;
        }

        /**
         * Converts a numeric input to the corresponding menu option.
         * <p>
         * This method maps user input integers to their associated menu options.
         * If no matching option is found, returns UNKNOWN.
         * </p>
         *
         * @param value the integer input to convert
         * @return the matching MainMenuOption or UNKNOWN if no match exists
         */
        static MainMenuOption fromInt(final int value)
        {
            for(MainMenuOption option : values())
            {
                if(option.value == value)
                {
                    return option;
                }
            }
            return UNKNOWN;
        }
    }

    /** 
     * Represents options available in the history sub-menu.
     * <p>
     * This enum defines all possible actions a player can take from the history menu
     * of the Mastermind game. Each option is associated with a numeric value that
     * corresponds to the user input expected from the console. Options include viewing
     * all game records, filtering by outcome, or returning to the main menu.
     * </p>
     */
    enum HistoryMenuOption
    {
        VIEW_ALL(1), 
        VIEW_WON(2), 
        VIEW_LOST(3),
        BACK_TO_MAIN(4), 
        UNKNOWN(-1);

        private final int value;

        /**
         * Constructs a history menu option with the specified numeric value.
         *
         * @param value the integer value associated with this menu option
         */
        HistoryMenuOption(final int value)
        {
            this.value = value;
        }

        /**
         * Converts a numeric input to the corresponding history menu option.
         * <p>
         * This method maps user input integers to their associated history menu options.
         * If no matching option is found, returns UNKNOWN.
         * </p>
         *
         * @param value the integer input to convert
         * @return the matching HistoryMenuOption or UNKNOWN if no match exists
         */
        static HistoryMenuOption fromInt(final int value)
        {
            for(HistoryMenuOption option : values())
            {
                if(option.value == value)
                {
                    return option;
                }
            }
            return UNKNOWN;
        }
    }

    
    private static final String SEPARATOR_LINE              = "----------------------------------------";
    private static final String GAME_OVER_MESSAGE           = "Game Over! The secret code was: %s";
    private static final String WIN_MESSAGE                 = "Congratulations! You won in %d round(s)!";
    private static final String GAME_OVER_SEPARATOR         = "=========== GAME OVER ============";
    private static final String GUESS_SUMMARY_HEADER_FOOTER = "----------- Guess Summary --------------";

    
    private static final String RULES_TEMPLATE = """
                                                 =========== MASTERMIND RULES ===========
                                                 1. The computer will generate a secret code of %d digits (%d-%d).
                                                 2. You have %d attempts to guess the code correctly.
                                                 3. After each guess, you'll receive feedback:
                                                    - Number of digits in the correct position
                                                    - Number of correct digits in the wrong position

                                                 ----------SPECIAL MECHANICS-------------
                                                 - Deceptive Rounds: Up to %d rounds may give slightly altered feedback, it's up to you
                                                   to discern whether the feedback is truthful or not.
                                                 - Truth Scan: Once per game, you can reveal the true feedback of a
                                                   previous round. Use this wisely!

                                                 -------------HOW TO PLAY----------------
                                                 - Enter a %d-digit guess (digits %d-%d).
                                                 - Enter '%s' to use your Truth Scan.
                                                 - Enter '%s' to view a summary of your previous guesses.

                                                 ---------------EXAMPLE------------------
                                                 Secret Code: %s
                                                 Your Guess:  %s
                                                 Feedback: Correct positions: %d, Misplaced: %d
                                                 (%d is correct position, %d is right digit wrong position)

                                                 Are you ready to start? (yes/no): """;


    private static final int OPTION_PLAY_INT         = 1;
    private static final int OPTION_VIEW_HISTORY_INT = 2;
    private static final int OPTION_EXIT_INT         = 3;
    private static final int DEFAULT_MENU_CHOICE_INT = - 1;

    private static final int HISTORY_OPTION_ALL_INT  = 1;
    private static final int HISTORY_OPTION_WON_INT  = 2;
    private static final int HISTORY_OPTION_LOST_INT = 3;
    private static final int HISTORY_OPTION_BACK_INT = 4;


    private final Scanner inputScanner;

    /**
     * Constructs a ConsoleView with the specified Scanner.
     *
     * @param scanner The Scanner instance to use for input.
     */
    UIHandler(final Scanner scanner)
    {
        validateScanner(scanner);
        this.inputScanner = scanner;
    }

    /**
     * Displays a standard separator line.
     */
    void displaySeparator()
    {
        System.out.println(SEPARATOR_LINE);
    }

    /**
     * Displays a message to the console followed by a newline.
     *
     * @param message The message to display.
     */
    void displayMessage(final String message)
    {
        validateMessage(message);
        System.out.println(message);
    }

    /**
     * Displays an error message to the standard error stream.
     *
     * @param errorMessage The error message to display.
     */
    void displayError(final String errorMessage)
    {
        validateMessage(errorMessage);
        System.err.println(errorMessage);
    }

    /**
     * Prompts the user to press Enter to continue and waits for input.
     */
    void waitForEnter()
    {
        System.out.print("Press Enter to continue...");
        if(inputScanner.hasNextLine())
        {
            inputScanner.nextLine();
        }
        inputScanner.nextLine();
    }

    /**
     * Displays the welcome message for the game.
     */
    void displayWelcome()
    {
        System.out.println();
        displaySeparator();
        displayMessage("Welcome to Mastermind!");
        displaySeparator();
    }

    /**
     * Asks the user if they have played before.
     */
    void promptForPlayedBefore()
    {
        System.out.print("Have you played this version before? (yes/no): ");
    }

    /**
     * Displays the formatted game rules by formatting the internal template.
     *
     * @param codeLength             Length of the code.
     * @param digitMin               Minimum allowed digit.
     * @param digitMax               Maximum allowed digit.
     * @param maxRounds              Maximum guess attempts.
     * @param deceptiveRoundsAllowed Max deceptive rounds.
     * @param truthScanKey           Key for truth scan action.
     * @param guessSummaryKey        Key for summary action.
     * @param exampleSecret          Example secret code for rules.
     * @param exampleGuess           Example guess for rules.
     * @param exampleCorrectPos      Example correct positions for rules.
     * @param exampleMisplaced       Example misplaced digits for rules.
     */
    void displayRules(final int codeLength,
                      final int digitMin,
                      final int digitMax,
                      final int maxRounds,
                      final int deceptiveRoundsAllowed,
                      final String truthScanKey,
                      final String guessSummaryKey,
                      final String exampleSecret,
                      final String exampleGuess,
                      final int exampleCorrectPos,
                      final int exampleMisplaced)
    {
        final String formattedRules = String.format(RULES_TEMPLATE,
                                                    codeLength,
                                                    digitMin,
                                                    digitMax,
                                                    maxRounds,
                                                    deceptiveRoundsAllowed,
                                                    codeLength,
                                                    digitMin,
                                                    digitMax,
                                                    truthScanKey,
                                                    guessSummaryKey,
                                                    exampleSecret,
                                                    exampleGuess,
                                                    exampleCorrectPos,
                                                    exampleMisplaced,
                                                    exampleCorrectPos,
                                                    exampleMisplaced                                                                                                                                                                                                                                                                                             );
        System.out.println(formattedRules);
    }

    /**
     * Displays the initial instructions about code length and attempts.
     *
     * @param codeLength The length of the secret code.
     * @param maxRounds  The maximum number of guessing rounds.
     */
    void displayInitialInstructions(final int codeLength,
                                    final int maxRounds)
    {
        System.out.println();
        displaySeparator();
        displayMessage("Try to guess the " + codeLength + "-digit code.");
        displayMessage("You have " + maxRounds + " attempts.");
        displaySeparator();
    }

    /**
     * Displays the exit message.
     */
    void displayExitMessage()
    {
        System.out.println();
        displaySeparator();
        displayMessage("Exiting Mastermind. Goodbye!");
        displaySeparator();
        System.out.println();
    }


    /**
     * Displays the main menu options.
     */
    void displayMainMenu()
    {
        System.out.println();
        displaySeparator();
        displayMessage("MASTERMIND MAIN MENU");
        displaySeparator();
        displayMessage(OPTION_PLAY_INT + ". Play Game");
        displayMessage(OPTION_VIEW_HISTORY_INT + ". View Game History");
        displayMessage(OPTION_EXIT_INT + ". Exit");
        displaySeparator();
    }

    /**
     * Prompts the user for their main menu choice and returns the corresponding enum.
     * Handles invalid input.
     *
     * @return The user's valid MainMenuOption choice.
     */
    MainMenuOption getMainMenuChoice()
    {
        int choiceInt;
        MainMenuOption choice;
        choiceInt = DEFAULT_MENU_CHOICE_INT;
        choice = MainMenuOption.UNKNOWN;

        while (choice == MainMenuOption.UNKNOWN) 
        {
            System.out.print("Enter your choice: ");
            try 
            {
                choiceInt = inputScanner.nextInt();
                choice = MainMenuOption.fromInt(choiceInt);

                if (choice == MainMenuOption.UNKNOWN) {
                    displayMessage("Invalid choice. Please enter " +
                                   MainMenuOption.PLAY_GAME.value + ", " +
                                   MainMenuOption.VIEW_HISTORY.value + ", or " +
                                   MainMenuOption.EXIT.value + ".");
                }
            } 
            catch (final InputMismatchException e) 
            {
                displayMessage("Invalid input. Please enter a number.");
                inputScanner.next(); 
                choiceInt = DEFAULT_MENU_CHOICE_INT; 
                choice = MainMenuOption.UNKNOWN;
            }
        }
        inputScanner.nextLine();
        return choice;
    }

    /**
     * Displays the history sub-menu options.
     */
    void displayHistorySubMenu()
    {
        System.out.println();
        displaySeparator();
        displayMessage("VIEW GAME HISTORY");
        displaySeparator();
        displayMessage(HISTORY_OPTION_ALL_INT + ". View All History");
        displayMessage(HISTORY_OPTION_WON_INT + ". View Won Games");
        displayMessage(HISTORY_OPTION_LOST_INT + ". View Lost Games");
        displayMessage(HISTORY_OPTION_BACK_INT + ". Back to Main Menu");
        displaySeparator();
    }

    /**
     * Prompts the user for their history menu choice and returns the
     * corresponding enum.
     * Handles invalid input.
     *
     * @return The user's valid HistoryMenuOption choice.
     */
    HistoryMenuOption getHistoryMenuChoice()
    {
        int choiceInt;
        HistoryMenuOption choice;

        choiceInt = DEFAULT_MENU_CHOICE_INT;
        choice    = HistoryMenuOption.UNKNOWN;

        while(choice == HistoryMenuOption.UNKNOWN)
        {
            System.out.print("Enter your choice: ");
            try
            {
                choiceInt = inputScanner.nextInt();
                choice    = HistoryMenuOption.fromInt(choiceInt);

                if(choice == HistoryMenuOption.UNKNOWN)
                {
                    displayMessage("Invalid choice. Please enter a number between " +
                                   HistoryMenuOption.VIEW_ALL.value +
                                   " and " +
                                   HistoryMenuOption.BACK_TO_MAIN.value +
                                   ".");
                }
            }
            catch(final InputMismatchException e)
            {
                displayMessage("Invalid input. Please enter a number.");
                inputScanner.next(); 
                choiceInt = DEFAULT_MENU_CHOICE_INT; 
                choice    = HistoryMenuOption.UNKNOWN;
            }
        }

        return choice;
    }

    /**
     * Displays the provided list of game history records.
     *
     * @param historyList The list of records to display. Can be null or empty.
     * @param historyType The type of history being displayed (used to determine
     *                    title).
     */
    void displayHistory(final List<GameSessionRecord> historyList,
                        final HistoryMenuOption historyType)
    {
        // Determine title based on the enum
        final String title;
        switch(historyType)
        {
            case VIEW_ALL -> title = "-------------- All Games ---------------";
            case VIEW_WON -> title = "-------------- Games Won ---------------";
            case VIEW_LOST -> title = "-------------- Games Lost ---------------";
            default -> title = "-------------- Unknown History View ---------------"; 
        }

        displayMessage("\n" + title);

        if(historyList == null || historyList.isEmpty())
        {
            displayMessage("No matching game history found.");
        }
        else
        {
            final StringBuilder historyOutput = new StringBuilder();
            for(final GameSessionRecord record : historyList)
            {
                historyOutput.append(SEPARATOR_LINE)
                             .append("\n");
                historyOutput.append(record.toString());
                historyOutput.append(SEPARATOR_LINE)
                             .append("\n\n");
            }
            historyOutput.append("End of history view.\n");
            System.out.print(historyOutput.toString());
        }
        displaySeparator();
        waitForEnter(); 
    }

    /**
     * Prints the header for a specific round.
     *
     * @param roundNumber The current round number.
     * @param maxRounds   The total number of rounds.
     */
    void displayRoundHeader(final int roundNumber,
                            final int maxRounds)
    {
        System.out.printf("%n--- Round %d of %d ---%n",
                          roundNumber,
                          maxRounds);
    }

    /**
     * Prompts the player to enter their guess.
     *
     * @param truthScanKey    The key for truth scan.
     * @param guessSummaryKey The key for guess summary.
     */
    void promptForGuess(final String truthScanKey,
                        final String guessSummaryKey)
    {
        displaySeparator();
        System.out.print("Enter your guess (or '" +
                         truthScanKey +
                         "' for truth scan, '" +
                         guessSummaryKey +
                         "' for summary): ");
    }

    /**
     * Displays the feedback for the player's guess.
     *
     * @param feedback The Feedback object to display.
     */
    void displayFeedback(final Feedback feedback)
    {
        displayMessage("\nFeedback: " + feedback.toString());
    }

    /**
     * Displays the separator for the guess summary.
     */
    void displayGuessSummarySeparator()
    {
        displayMessage("\n" + GUESS_SUMMARY_HEADER_FOOTER + "\n");
    }

    /**
     * Displays a single round's details within the summary.
     *
     * @param roundInfo The string representation of the round.
     */
    void displayGuessSummaryItem(final String roundInfo)
    {
        displayMessage(roundInfo);
    }

    /**
     * Displays a message when there are no guesses for the summary.
     */
    void displayNoGuessesMessage()
    {
        displayMessage("No guesses made yet.");
    }

    /**
     * Displays the header for a truth scan request.
     */
    void displayTruthScanRequested()
    {
        displayMessage("\n--- Truth Scan Requested ---");
    }

    /**
     * Displays the result of a truth scan.
     *
     * @param scanResultInfo The information string from the scan.
     */
    void displayTruthScanResult(final String scanResultInfo)
    {
        displayMessage(scanResultInfo);
    }

    /**
     * Displays the completion message for a truth scan.
     */
    void displayTruthScanComplete()
    {
        displayMessage("--- Truth Scan Complete ---");
    }

    /**
     * Displays a message when a truth scan fails or is cancelled.
     */
    void displayTruthScanFailed()
    {
        displayMessage("--- Truth Scan Failed or Cancelled ---");
    }

    /**
     * Displays the game over header.
     */
    void displayGameOverHeader()
    {
        System.out.println("\n" + GAME_OVER_SEPARATOR);
    }

    /**
     * Displays the winning message.
     *
     * @param roundsPlayed The number of rounds taken to win.
     */
    void displayWinMessage(final int roundsPlayed)
    {
        System.out.printf(WIN_MESSAGE + "%n",
                          roundsPlayed);
    }

    /**
     * Displays the game over message when the player loses.
     *
     * @param secretCode The secret code that was not guessed.
     */
    void displayLossMessage(final SecretCode secretCode)
    {
        System.out.printf(GAME_OVER_MESSAGE + "%n",
                          secretCode.toString());
    }

    /**
     * Displays a message indicating the game ended without guesses.
     */
    void displayNoGuessesEndMessage()
    {
        displayMessage("Game ended without any guesses.");
    }

    /**
     * Displays a message indicating game history is being saved.
     */
    void displaySavingHistory()
    {
        displayMessage("Saving game history...");
    }

    /**
     * Displays a message indicating game history saving is complete.
     */
    void displaySaveComplete()
    {
        displayMessage("Game history saved.");
    }

    /**
     * Validates that the scanner is not null.
     *
     * @param scanner The scanner to validate
     */
    private static void validateScanner(final Scanner scanner)
    {
        if(scanner == null)
        {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
    }

    /**
     * Validates that the message is not null.
     *
     * @param message The message to validate
     */
    private static void validateMessage(final String message)
    {
        if(message == null)
        {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }

    /**
     * Validates that the list is not null.
     *
     * @param list The list to validate
     */
    private static void validateList(final List<GameSessionRecord> list)
    {
        if(list == null)
        {
            throw new IllegalArgumentException("List cannot be null");
        }

        if(list.isEmpty())
        {
            throw new IllegalArgumentException("List cannot be empty");
        }
    }
}