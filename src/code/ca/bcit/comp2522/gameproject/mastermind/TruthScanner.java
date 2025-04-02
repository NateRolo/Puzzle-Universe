package ca.bcit.comp2522.gameproject.mastermind;

import java.util.List;

/**
 * Handles the truth scan functionality in the Mastermind game.
 * <p>
 * This class encapsulates the logic for revealing true feedback
 * for potentially deceptive rounds and tracks scan usage.
 * </p>
 *
 * @author Nathan O
 * @version 1.1 2025 // Version updated
 */
final class TruthScanner
{
    private static final int INCREMENT = 1;
    
    // Tracking scan usage for history
    private boolean truthScanUsedThisGame;
    private int     roundScanInitiatedIn = -1; // Round number when the 't' command was issued
    private int     roundScanned         = -1; // Round number targeted by the scan
    
    /**
     * Constructs a new TruthScanner.
     * Initializes scan usage state.
     */
    TruthScanner()
    {
        resetTruthScanner(); // Initialize state using reset method
    }
    
    /**
     * Handles a truth scan request, performs the scan, and returns info for history.
     * Checks if scan is available, prompts for target round, reveals truth if needed,
     * updates internal state, and returns a descriptive string or null.
     *
     * @param rounds The list of game rounds played so far.
     * @param secretCode The secret code for the current game.
     * @return A formatted string describing the scan action (e.g., "Used in Round 5, targeting Round 3 (Deceptive)")
     *         if the scan was successfully completed, otherwise returns null (e.g., if already used, no rounds, or cancelled).
     */
    final String handleTruthScanRequestAndGetInfo(final List<Round> rounds,
                                                  final SecretCode secretCode)
    {
        if (truthScanUsedThisGame)
        {
            System.out.println("You have already used your truth scan this game!");
            return null; // Scan failed (already used)
        }
        
        if (rounds.isEmpty())
        {
            System.out.println("No previous rounds to scan!");
            return null; // Scan failed (no rounds)
        }

        // Record the round number *when the scan command was initiated*
        final int currentRoundNumber = rounds.size() + INCREMENT;

        // Get the target round number from the user
        final int targetRoundNumber = InputHandler.getRoundNumberForScan(rounds.size());

        // Check for cancellation or invalid input from InputHandler (optional, assuming it returns <= 0 for cancel)
        if (targetRoundNumber <= 0)
        {
             System.out.println("Truth Scan cancelled.");
             return null; // Scan cancelled by user
        }

        // Retrieve the selected round (adjusting for 0-based index)
        final Round selectedRound = rounds.get(targetRoundNumber - INCREMENT);

        // Perform the scan logic
        final String scanResultDescription;
        if (selectedRound.isDeceptiveRound())
        {
            final Feedback trueFeedback = new Feedback(secretCode, selectedRound.getGuess());
            System.out.println("Revealing true feedback for round " + targetRoundNumber + ":");
            System.out.println(trueFeedback);
            selectedRound.revealTruth(); // Mark the round itself as truth revealed
            scanResultDescription = String.format("Used in Round %d, targeting Round %d (Deceptive - Truth Revealed)",
                                                currentRoundNumber,
                                                targetRoundNumber);
        }
        else
        {
            System.out.println("Round " + targetRoundNumber + " was not deceptive! Scan used, but no change.");
            // Even if not deceptive, the scan attempt counts as used
            scanResultDescription = String.format("Used in Round %d, targeting Round %d (Not Deceptive)",
                                                currentRoundNumber,
                                                targetRoundNumber);
        }

        // Mark scan as used for this game and store details
        this.truthScanUsedThisGame = true;
        this.roundScanInitiatedIn  = currentRoundNumber;
        this.roundScanned          = targetRoundNumber;

        return scanResultDescription; // Return the formatted info string
    }
    
    /**
     * Resets the truth scanner state for a new game.
     */
    final void resetTruthScanner()
    {
        this.truthScanUsedThisGame = false;
        this.roundScanInitiatedIn  = -1;
        this.roundScanned          = -1;
    }

    // Optional: Getters for the scan details if needed elsewhere, though currently only used for the history string
    /*
    final boolean isTruthScanUsedThisGame() {
        return truthScanUsedThisGame;
    }

    final int getRoundScanInitiatedIn() {
        return roundScanInitiatedIn;
    }

    final int getRoundScanned() {
        return roundScanned;
    }
    */

    // The old handleTruthScanRequest method is now effectively replaced by handleTruthScanRequestAndGetInfo
    // It can be removed or kept as private/deprecated if desired.
    /*
    boolean handleTruthScanRequest(final List<Round> rounds,
                                         final SecretCode secretCode)
    {
        // ... old logic ...
    }
    */
} 