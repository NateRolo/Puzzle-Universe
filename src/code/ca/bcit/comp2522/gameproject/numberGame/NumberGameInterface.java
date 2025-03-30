package ca.bcit.comp2522.gameproject.numbergame;

/**
 * Defines the contract for the Number Game logic.
 * This interface outlines the essential operations required
 * for managing the game state, handling player actions,
 * and determining game outcomes in the 20-Number Challenge.
 *
 * @author Nathan O
 */
interface NumberGameInterface
{
    /**
     * Starts a new game by resetting the game state.
     */
    void startNewGame();

    /**
     * Places the current number at the specified position on the game board.
     *
     * @param position the board position where the number should be placed
     */
    void placeNumber(final int position);

    /**
     * Determines if the game is over (win or loss condition met).
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver();

    /**
     * Displays the current game statistics and score information (e.g., to console).
     */
    void showScore();

    /**
     * Gets the next number that needs to be placed on the board.
     *
     * @return the next number to be placed
     */
    int getNextNumber();

    /**
     * Checks if placing the current number at the specified position is valid
     * according to the game's ascending order rules.
     *
     * @param position the position to check for valid placement
     * @return true if the current number can be placed at the position,
     *         false otherwise
     */
    boolean isValidPlacement(final int position);

    /**
     * Initializes or resets the game state. Called before starting a new game.
     */
    void initializeGame();
}