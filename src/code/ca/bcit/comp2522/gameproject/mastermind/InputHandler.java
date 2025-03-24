package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;
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
    private static final int CODE_LENGTH      = 4;
    private static final int MIN_DIGIT        = 1;
    private static final int MAX_DIGIT        = 6;
    private static final int MIN_ROUND_NUMBER = 1;

    // Message Constants
    private static final String YES_NO_PROMPT = "Error: Please answer 'yes' or 'no'";
    private static final String GUESS_PROMPT  = "Enter your guess (4 digits, each 1-6) or 'T' to use truth scan:";
    private static final String RETRY_PROMPT  = "Please try again.";

    // Input Validation
    private static final String DIGIT_RANGE_ERROR = "Each digit must be between %d and %d";
    private static final String LENGTH_ERROR      = "Guess must be exactly %d digits";
    private static final String ROUND_RANGE_ERROR = "Round number must be between %d and %d";

    // Regex Pattern
    private static final String VALID_DIGITS_PATTERN = "^[1-6]+$";

    private final Scanner scanner;

    /**
     * Constructs a new InputHandler with the specified scanner.
     *
     * @param scanner the Scanner to use for input
     */
    InputHandler(final Scanner scanner)
    {
        if(scanner == null)
        {
            throw new NullPointerException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }

    /**
     * Gets and validates the player's input.
     *
     * @return a PlayerInput object representing either a guess or truth scan request
     * @throws InvalidGuessException if the guess input is invalid
     */
    PlayerInput getPlayerInput()
    {
        do
        {
            System.out.println(GUESS_PROMPT);
            final String input = scanner.nextLine()
                                        .trim()
                                        .toUpperCase();

            if(input.equalsIgnoreCase("T"))
            {
                return PlayerInput.createTruthScanRequest();
            }

            try
            {
                validateGuessFormat(input);
                final List<Integer> digits;

                digits = parseGuess(input);
                return new PlayerInput(new PlayerGuess(digits));
            }
            catch(InvalidGuessException e)
            {
                System.out.println(RETRY_PROMPT);
            }
        } while(true);
    }

    /**
     * Asks if the player wants to use their truth scan.
     *
     * @return true if player wants to use truth scan, false otherwise
     */
    boolean promptForTruthScan()
    {
        final String TRUTH_SCAN_PROMPT;

        TRUTH_SCAN_PROMPT = "Would you like to use your truth scan? (yes/no):";

        return getYesNoResponse(TRUTH_SCAN_PROMPT).equals("yes");
    }

    /**
     * Gets the round number to reveal true feedback for.
     *
     * @param currentRound the current round number
     * @return the selected round number
     * @throws InvalidGuessException if input is invalid
     */
    int getRoundNumberForScan(final int currentRound)
    {
        validateCurrentRound(currentRound);
        return getRoundNumberWithValidation(currentRound);
    }

    /*
     * Validates the current round number.
     */
    private void validateCurrentRound(final int currentRound)
    {
        if(currentRound < MIN_ROUND_NUMBER)
        {
            throw new IllegalArgumentException("Current round must be positive");
        }
    }

    /*
     * Gets and validates the round number input.
     */
    private int getRoundNumberWithValidation(final int currentRound)
    {
        System.out.println("Enter round number to scan (1-" + currentRound + "):");

        try
        {
            final int roundNumber = Integer.parseInt(scanner.nextLine()
                                                            .trim());
            validateRoundNumberRange(roundNumber,
                                     currentRound);
            return roundNumber;
        }
        catch(NumberFormatException e)
        {
            throw new InvalidGuessException("Invalid round number format",
                                            e);
        }
    }

    /*
     * Validates the round number is within valid range.
     */
    private void validateRoundNumberRange(final int roundNumber,
                                          final int currentRound)
    {
        if(roundNumber < MIN_ROUND_NUMBER || roundNumber > currentRound)
        {
            throw new InvalidGuessException(String.format(ROUND_RANGE_ERROR,
                                                          MIN_ROUND_NUMBER,
                                                          currentRound));
        }
    }

    /*
     * Validates the format of the guess input string.
     */
    private void validateGuessFormat(final String input)
    {
        if(input == null || input.isEmpty())
        {
            throw new InvalidGuessException("Guess cannot be empty");
        }

        if(input.length() != CODE_LENGTH)
        {
            throw new InvalidGuessException(String.format(LENGTH_ERROR,
                                                          CODE_LENGTH));
        }

        if(! input.matches(VALID_DIGITS_PATTERN))
        {
            throw new InvalidGuessException(String.format(DIGIT_RANGE_ERROR,
                                                          MIN_DIGIT,
                                                          MAX_DIGIT));
        }
    }

    /*
     * Parses and validates the digits from the input string.
     */
    private List<Integer> parseGuess(final String input)
    {
        final List<Integer> digits;

        digits = new ArrayList<>();

        for(char c : input.toCharArray())
        {
            final int digit;
            digit = Character.getNumericValue(c);

            if(digit < MIN_DIGIT || digit > MAX_DIGIT)
            {
                throw new InvalidGuessException(String.format(DIGIT_RANGE_ERROR,
                                                              MIN_DIGIT,
                                                              MAX_DIGIT));
            }

            digits.add(digit);
        }

        return digits;
    }

    /*
     * Gets a yes/no response from user with validation.
     */
    private String getYesNoResponse(final String prompt)
    {
        String response;

        do
        {
            System.out.println(prompt);
            response = scanner.nextLine()
                              .trim()
                              .toLowerCase();

            if(! response.equals("yes") && ! response.equals("no"))
            {
                System.out.println(YES_NO_PROMPT);
            }
        } while(! response.equals("yes") && ! response.equals("no"));

        return response;
    }
}
