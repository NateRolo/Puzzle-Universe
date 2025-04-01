package ca.bcit.comp2522.gameproject.mastermind;

import java.util.Scanner;


/**
 * Handles user input for the Mastermind game.
 * <p>
 * This class manages all player input, including guess validation, truth scan
 * requests, and round selection for scanning.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class InputHandler
{
    private static final int MIN_DIGIT        = 1;
    private static final int MAX_DIGIT        = 6;
    private static final int MIN_ROUND_NUMBER = 1;

    private static final Scanner scan = new Scanner(System.in);

    // Message Constants
    private static final String RETRY_PROMPT        = "Invalid guess, please try again.";
    private static final String TRUTH_SCAN_INPUT    = "t";
    private static final String GUESS_SUMMARY_INPUT = "g";

    // Input Validation
    private static final String DIGIT_RANGE_ERROR = "Each digit must be between %d and %d";
    private static final String LENGTH_ERROR      = "Guess must be exactly %d digits";
    

    // Regex Pattern
    private static final String VALID_DIGITS_PATTERN = "^[1-6]+$";

    /**
     * Gets and validates the player's input.
     *
     * @return a PlayerGuessCode object representing either a guess or truth
     *         scan request
     * @throws InvalidGuessException if the guess input is invalid
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

        if(input.equalsIgnoreCase(TRUTH_SCAN_INPUT))
        {
            return new PlayerAction.TruthScanRequest();
        }
        else if(input.equalsIgnoreCase(GUESS_SUMMARY_INPUT))
        {
            return new PlayerAction.GuessSummaryRequest();
        }

        try
        {
            final PlayerGuessCode guessCode;
            guessCode = PlayerGuessCode.fromInput(input);

            return guessCode;
        }
        catch(InvalidGuessException e)
        {
            System.out.println(e.getMessage());
            System.out.println(RETRY_PROMPT);
            return null;
        }
    }

    /**
     * Gets the round number to reveal true feedback for.
     *
     * @param currentRound the current round number
     * @return the selected round number
     * @throws InvalidGuessException if input is invalid
     */
    static int getRoundNumberForScan(final int currentRound)
    {
        validateCurrentRound(currentRound);
        return getRoundNumberWithValidation(currentRound);
    }

    /*
     * Validates the current round number.
     * Ensures the round number is not less than the minimum allowed.
     */
    private static void validateCurrentRound(final int currentRound)
    {
        if(currentRound < MIN_ROUND_NUMBER)
        {
            throw new IllegalArgumentException("Current round must be positive");
        }
    }

    /*
     * Gets and validates the round number input from the user for a scan.
     * Continuously prompts until a valid round number within the allowed range
     * (1 to currentRound) is entered.
     *
     * @param currentRound the current round number to use as upper bound
     * 
     * @return the validated round number selected by the user
     */
    private static int getRoundNumberWithValidation(final int currentRound)
    {
        while(true)
        {
            System.out.println("Enter round number to scan (1-" +
                               currentRound +
                               "):");
            final String input = scan.nextLine()
                                     .trim();

            if(! isValidNumericInput(input))
            {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try
            {
                final int roundNumber = Integer.parseInt(input);

                if(! isValidRoundNumber(roundNumber,
                                        currentRound))
                {
                    System.out.printf("Please enter a number between %d and %d.%n",
                                      MIN_ROUND_NUMBER,
                                      currentRound);
                    continue;
                }

                return roundNumber;
            }
            catch(NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /*
     * Checks if the input string contains only digits.
     *
     * @param input the string to validate
     * 
     * @return true if the input contains only digits, false otherwise
     */
    private static boolean isValidNumericInput(final String input)
    {
        return input.matches("\\d+");
    }

    /*
     * Checks if the provided round number is within the valid range.
     * The valid range is from the minimum round number up to the current round.
     *
     * @param roundNumber the number to validate
     * 
     * @param currentRound the current round number (upper bound)
     * 
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

    /**
     * Gets a yes/no response from the user.
     *
     * @return the user's response (either "yes" or "no")
     */
    static String getYesNoResponse()
    {
        String response;
        do
        {
            response = scan.nextLine()
                           .trim()
                           .toLowerCase();
            if(! response.equalsIgnoreCase("yes") &&
               ! response.equalsIgnoreCase("no"))
            {
                System.out.println("Please enter either 'yes' or 'no':");
            }
        } while(! response.equalsIgnoreCase("yes") &&
                ! response.equalsIgnoreCase("no"));

        return response;
    }
}
