package ca.bcit.comp2522.gameproject.mastermind;

/**
 * Marker interface representing a possible action taken by the player during
 * their turn in the Mastermind game. Implementations define specific actions
 * like making a guess or requesting information.
 *
 * @author Nathan O
 * @version 1.1 2025
 */
interface PlayerAction
{
    /**
     * Represents a player's request to perform a "Truth Scan" on a
     * previously played round to reveal its actual feedback, potentially
     * bypassing deception.
     */
    final class TruthScanRequest implements PlayerAction
    {
        /**
         * Constructs a TruthScanRequest.
         */
        public TruthScanRequest(){}
    }

    /**
     * Represents a player's request to view a summary of all guesses
     * made so far in the current game, along with their feedback.
     */
    final class GuessSummaryRequest implements PlayerAction
    {
        // Implicit default constructor
    }
}
