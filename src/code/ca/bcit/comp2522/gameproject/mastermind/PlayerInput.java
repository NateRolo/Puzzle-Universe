package ca.bcit.comp2522.gameproject.mastermind;

import java.util.List;

/**
 * Represents a player's input in the Mastermind game.
 * <p>
 * This class handles both regular guesses and truth scan requests,
 * providing a unified way to process player input.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class PlayerInput extends
                               Code
{
    private static final String NULL_DIGITS_ERROR = "Digits cannot be null";
    private static final String TRUTH_SCAN_ERROR  = "Cannot get digits from truth scan request";

    private final boolean isTruthScanRequest;

    /**
     * Constructs a PlayerInput representing a guess.
     *
     * @param digits the sequence of digits that make up the guess
     */
    public PlayerInput(final List<Integer> digits)
    {
        super(digits);
        this.isTruthScanRequest = false;
    }

    /**
     * Creates a PlayerInput representing a truth scan request.
     *
     * @return a new PlayerInput configured as a truth scan request
     */
    public static PlayerInput createTruthScanRequest()
    {
        return new PlayerInput();
    }

    /*
     * Private constructor for truth scan request.
     */
    private PlayerInput()
    {
        super(null);
        this.isTruthScanRequest = true;
    }

    /**
     * Gets the digits of the guess.
     *
     * @return the list of digits
     * @throws IllegalStateException if this input is a truth scan request
     */
    @Override
    public List<Integer> getDigits()
    {
        if(isTruthScanRequest)
        {
            throw new IllegalStateException(TRUTH_SCAN_ERROR);
        }
        return super.getDigits();
    }

    /**
     * Checks if this input is a truth scan request.
     *
     * @return true if this is a truth scan request, false if it's a guess
     */
    public boolean isTruthScanRequest()
    {
        return isTruthScanRequest;
    }
}