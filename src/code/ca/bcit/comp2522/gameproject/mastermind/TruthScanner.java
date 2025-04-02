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
    private static final int ROUND_INCREMENT = 1;
    private static final int ROUND_MINIMUM = 0;
    private static final int DEFAULT_ROUND_SCAN_INITIATED = -1;
    private static final int DEFAULT_ROUND_SCANNED = -1;
    
    // Tracking scan usage for history
    private boolean truthScanUsedThisGame;
    private int     roundScanInitiatedIn = -1; // Round number when the 't' command was issued
    private int     roundScanned         = -1;
    
    /**
     * Constructs a new TruthScanner.
     * Initializes scan usage state.
     */
    TruthScanner()
    {
        resetTruthScanner(); 
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

        final int currentRoundNumber;
        final int targetRoundNumber;
        final Round selectedRound;
        final String scanResultDescription;

        currentRoundNumber = rounds.size() + ROUND_INCREMENT;
        targetRoundNumber = InputHandler.getRoundNumberForScan(rounds.size());

        if (targetRoundNumber <= ROUND_MINIMUM)
        {
             System.out.println("Truth Scan cancelled.");
             return null; // Scan cancelled by user
        }

        selectedRound = rounds.get(targetRoundNumber - ROUND_INCREMENT);

        // Perform the scan logic    
        if (selectedRound.isDeceptiveRound())
        {
            final Feedback trueFeedback;
            trueFeedback = new Feedback(secretCode, selectedRound.getGuess());

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
        this.roundScanInitiatedIn  = DEFAULT_ROUND_SCAN_INITIATED;
        this.roundScanned          = DEFAULT_ROUND_SCANNED;
    }
} 