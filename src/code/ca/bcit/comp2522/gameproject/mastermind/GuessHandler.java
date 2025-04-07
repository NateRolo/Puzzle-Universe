package ca.bcit.comp2522.gameproject.mastermind;

import java.util.Scanner;


/**
 * Handles user input for the Mastermind game.
 * <p>
 * This class manages all player input, including guess validation, truth scan
 * requests, and round selection for scanning. It provides methods to collect
 * and validate user guesses, process special commands like truth scans, and
 * gather yes/no responses from the player. The class ensures all input is
 * properly formatted and within valid ranges before passing it to the game logic.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class GuessHandler
{
    private static final int MIN_ROUND_NUMBER = 1;

    private static final Scanner scan = new Scanner(System.in);

    /**
     * Gets and validates player input from the console.
     * 
     * Reads user input and returns the appropriate PlayerAction:
     * - TruthScanRequest if input matches truth scan command
     * - GuessSummaryRequest if input matches guess summary command
     * - PlayerGuessCode otherwise
     *
     * @return a PlayerAction based on the user's input
     */
    static PlayerAction getPlayerInput()
    {
        if(!scan.hasNextLine())
        {
            throw new java.util.NoSuchElementException("Input stream ended unexpectedly.");
        }
        final String input;

        input = scan.nextLine()
                    .trim()
                    .toUpperCase();

        if(input.equalsIgnoreCase(MastermindGame.INPUT_TRUTH_SCAN))
        {
            return new PlayerAction.TruthScanRequest();
        }
        else if(input.equalsIgnoreCase(MastermindGame.INPUT_GUESS_SUMMARY))
        {
            return new PlayerAction.GuessSummaryRequest();
        }

        final PlayerGuessCode guessCode;
        guessCode = PlayerGuessCode.fromInput(input);

        return guessCode;
    }

    /**
     * Gets the round number to reveal true feedback for.
     *
     * @param currentRound the current round number
     * @return the selected round number
     */
    static int getRoundNumberForScan(final int currentRound)
    {
        validateCurrentRound(currentRound);
        return getRoundNumberWithValidation(currentRound);
    }

    /**
     * Gets a yes/no response from the user via the console.
     * <p>
     * This method repeatedly prompts the user until a valid response is
     * received.
     * Valid responses are case-insensitive variations of "yes" or "no".
     * The input is trimmed of leading and trailing whitespace and converted to
     * lowercase for consistent processing.
     * </p>
     *
     * @return the user's response as a lowercase string (either "yes" or "no")
     */
    static String getYesNoResponse()
    {
        String response;
        do
        {
            response = scan.nextLine()
                           .trim()
                           .toLowerCase();
            if(!response.equalsIgnoreCase("yes") &&
               !response.equalsIgnoreCase("no"))
            {
                System.out.println("Please enter either 'yes' or 'no':");
            }
        } while(!response.equalsIgnoreCase("yes") &&
                !response.equalsIgnoreCase("no"));

        return response;
    }

    /*
     * Gets and validates the round number input from the user for a scan.
     * Continuously prompts until a valid round number within the allowed range
     * (1 to currentRound) is entered.
     *
     * @param currentRound the current round number to use as upper bound
     * @return the validated round number selected by the user
     */
    private static int getRoundNumberWithValidation(final int currentRound)
    {
        while(true)
        {
            final String input;
            final int    roundNumber;

            System.out.println("Enter round number to scan (1-" +
                               currentRound +
                               "):");
            input = scan.nextLine()
                        .trim();

            if(!isValidNumericInput(input))
            {
                System.out.println("Please enter a valid number.");
                continue;
            }

            roundNumber = Integer.parseInt(input);

            if(!isValidRoundNumber(roundNumber,
                                   currentRound))
            {
                System.out.printf("Please enter a number between %d and %d.%n",
                                  MIN_ROUND_NUMBER,
                                  currentRound);
                continue;
            }
            return roundNumber;
        }
    }

    /*
     * Checks if the input string contains only digits.
     *
     * @param input the string to validate
     * @return true if the input contains only digits, false otherwise
     */
    private static boolean isValidNumericInput(final String input)
    {
        final boolean inputIsValid;
        inputIsValid = input.matches("\\d+");

        return inputIsValid;
    }

    /*
     * Checks if the provided round number is within the valid range.
     * The valid range is from the minimum round number up to the current round.
     *
     * @param roundNumber the number to validate
     * @param currentRound the current round number (upper bound)
     * @return true if the round number is valid, false otherwise
     */
    private static boolean isValidRoundNumber(final int roundNumber,
                                                    final int currentRound)
    {
        final boolean isValid;
        isValid = roundNumber >= MIN_ROUND_NUMBER &&
                  roundNumber <= currentRound;
        return isValid;
    }

    

    /*
     * Validates the current round number.
     * Ensures the round number is not less than the minimum allowed.
     */
    private static final void validateCurrentRound(final int currentRound)
    {
        if(currentRound < MIN_ROUND_NUMBER)
        {
            throw new IllegalArgumentException("Current round must be positive");
        }
    }
}
