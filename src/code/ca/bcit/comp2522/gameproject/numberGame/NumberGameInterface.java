package ca.bcit.comp2522.gameproject.numbergame;

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
     * Determines if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver();

    /**
     * Displays the current game statistics and score information.
     */
    void showScore();

    /**
     * Gets the next number that needs to be placed on the board.
     *
     * @return the next number to be placed
     */
    int getNextNumber();

    /**
     * Checks if a number can be legally placed at the specified position.
     *
     * @param position the position to check for valid placement
     * @return true if the current number can be placed at the position, false otherwise
     */
    boolean isValidPlacement(final int position);
    
    /**
     * Processes a single game turn.
     * @return boolean indicating if game should continue
     */
    boolean processTurn();
    
    /**
     * Initializes or resets the game state.
     */
    void initializeGame();
    
    /**
     * Checks if the game is complete.
     * @return boolean indicating game completion status
     */
    boolean isGameComplete();
}