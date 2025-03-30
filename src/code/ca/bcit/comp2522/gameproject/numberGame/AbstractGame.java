package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Random;

/**
 * Abstract base class for number-based games.
 * Provides common functionality for game initialization, state tracking,
 * and validation of game rules.
 *
 * @author Nathan O
 * @version 1.0 2025
 */
abstract class AbstractGame implements
                            NumberGameInterface
{
    private static final int BOARD_SIZE           = 20;
    private static final int MAX_RANDOM_NUMBER    = 1000;
    private static final int MIN_RANDOM_NUMBER    = 1;
    private static final int EMPTY_CELL           = 0;
    private static final int SECOND_ELEMENT_INDEX = 1;
    private static final int INITIAL_VALUE        = 0;

    private final Random random;
    private final int    maxAttempts;
    private int          currentAttempt;
    private boolean      gameWon;

    int[] board;
    int   currentNumber;
    int   gamesWon;
    int   totalPlacements;
    int   gamesPlayed;

    /**
     * Constructs an AbstractGame.
     * 
     * @param maxAttempts the maximum number of attempts allowed.
     */
    AbstractGame(final int maxAttempts)
    {
        if(maxAttempts <= INITIAL_VALUE)
        {
            throw new IllegalArgumentException("Max attempts must be positive");
        }
        this.maxAttempts = maxAttempts;
        board            = new int[BOARD_SIZE];
        random           = new Random();
        gamesPlayed      = EMPTY_CELL;
        gamesWon         = EMPTY_CELL;
        totalPlacements  = EMPTY_CELL;
    }

    /**
     * Initializes the game state.
     * Resets the current attempt counter and game won status.
     */
    @Override
    public void initializeGame()
    {
        currentAttempt = INITIAL_VALUE;
        gameWon        = false;
    }

    /**
     * Gets the current attempt number.
     * 
     * @return current attempt
     */
    int getCurrentAttempt()
    {
        return currentAttempt;
    }

    /**
     * Increments the current attempt counter.
     */
    void incrementAttempt()
    {
        currentAttempt++;
    }

    /**
     * Sets the game's won status.
     * 
     * @param won the won status
     */
    void setGameWon(final boolean won)
    {
        this.gameWon = won;
    }

    /**
     * Checks if the game is complete.
     * The game is considered complete when either the player has won
     * or the maximum number of attempts has been reached.
     *
     * @return true if the game is complete, false otherwise
     */
    @Override
    public boolean isGameComplete()
    {
        return gameWon || currentAttempt >= maxAttempts;
    }

    /**
     * Gets whether the game was won.
     * 
     * @return true if game was won, false otherwise
     */
    boolean isGameWon()
    {
        return gameWon;
    }

    /**
     * Gets the maximum attempts allowed.
     * 
     * @return maximum attempts
     */
    int getMaxAttempts()
    {
        return maxAttempts;
    }

    /**
     * Generates a random number within the defined range.
     * 
     * @return a random number between MIN_RANDOM_NUMBER and MAX_RANDOM_NUMBER
     */
    int generateNumber()
    {
        return random.nextInt(MAX_RANDOM_NUMBER) + MIN_RANDOM_NUMBER;
    }

    /**
     * Checks if the game board is completely filled.
     * 
     * @return true if no empty cells remain, false otherwise
     */
    boolean isBoardFull()
    {
        for(int num : board)
        {
            if(num == EMPTY_CELL) return false;
        }
        return true;
    }

    /**
     * Validates that numbers on the board are in ascending sequence.
     * 
     * @return true if the sequence is valid, false otherwise
     */
    boolean isValidSequence()
    {
        for(int i = SECOND_ELEMENT_INDEX; i < board.length; i++)
        {
            if(board[i] != EMPTY_CELL &&
               board[i - SECOND_ELEMENT_INDEX] != EMPTY_CELL &&
               board[i] < board[i - SECOND_ELEMENT_INDEX])
            {
                return false;
            }
        }
        return true;
    }
}