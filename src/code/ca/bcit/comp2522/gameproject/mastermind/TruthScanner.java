package ca.bcit.comp2522.gameproject.mastermind;

import java.util.List;

/**
 * Handles the truth scan functionality in the Mastermind game.
 * <p>
 * This class encapsulates the logic for revealing true feedback
 * for potentially deceptive rounds.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class TruthScanner
{
    private static final int INCREMENT = 1;
    
    private boolean truthScanUsed;
    
    /**
     * Constructs a new TruthScanner.
     */
    TruthScanner()
    {
        this.truthScanUsed = false;
    }
    
    /**
     * Handles a truth scan request.
     * 
     * @param rounds the list of game rounds
     * @param secretCode the secret code
     */
    boolean handleTruthScanRequest(final List<Round> rounds,
                                         final SecretCode secretCode)
    {
        if (truthScanUsed)
        {
            System.out.println("You have already used your truth scan!");
            return false;
        }
        
        if (rounds.isEmpty())
        {
            System.out.println("No previous rounds to scan!");
            return false;
        }

        useTruthScan(rounds, secretCode);
        return true;
    }
    
    /*
     * Performs the truth scan on a selected round.
     * Prompts user for round, retrieves it, checks if deceptive, prints true
     * feedback if needed, marks truth revealed, and sets scan as used.
     *
     * @param rounds the list of game rounds
     * @param secretCode the secret code
     */
    private void useTruthScan(final List<Round> rounds,
                             final SecretCode secretCode)
    {
        final int   roundNumber;
        final Round selectedRound;

        roundNumber   = InputHandler.getRoundNumberForScan(rounds.size());
        selectedRound = rounds.get(roundNumber - INCREMENT);

        if (selectedRound.isDeceptiveRound())
        {
            final Feedback trueFeedback = new Feedback(secretCode, selectedRound.getGuess());
            System.out.println("True feedback for round " + roundNumber + ":");
            System.out.println(trueFeedback);
            selectedRound.revealTruth();
        }
        else
        {
            System.out.println("This round was not deceptive!");
        }
        
        truthScanUsed = true;
    }
    
    /**
     * Checks if the truth scan has been used.
     * 
     * @return true if the truth scan has been used, false otherwise
     */
    boolean isTruthScanUsed()
    {
        return truthScanUsed;
    }

    void resetTruthScanner()
    {
        this.truthScanUsed = false;
    }
} 