package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Arrays;
import java.util.Random;

/**
 * Abstract base class for number-based games.
 * Provides common functionality for game initialization, state tracking,
 * and validation of game rules.
 * 
 * @author Nathan O
 * @version 1.1 2025
 */
abstract class AbstractGame implements
                            NumberGameInterface
{
    private static final int RANGE_OFFSET      = 1;
    static final int         BOARD_SIZE        = 20;
    static final int         MAX_RANDOM_NUMBER = 1000;
    static final int         MIN_RANDOM_NUMBER = 1;
    static final int         EMPTY_CELL        = 0;
    static final int         INITIAL_VALUE     = 0;

    static final int INVALID_NUMBER_SENTINEL = - 1;

    private final Random random;
    private boolean      gameWon;

    int[] board;
    int   currentNumber;
    int   gamesWon;
    int   totalPlacements;
    int   gamesPlayed;

    /**
     * Constructs an AbstractGame. Initializes game statistics and the random number generator.
     */
    AbstractGame()
    {
        this.board           = new int[BOARD_SIZE];
        this.random          = new Random();
        this.gamesPlayed     = INITIAL_VALUE;
        this.gamesWon        = INITIAL_VALUE;
        this.totalPlacements = INITIAL_VALUE;
        this.gameWon         = false;
    }

    /**
     * Initializes the game state for a new round.
     * Resets the game won status and clears the board.
     */
    @Override
    public void initializeGame()
    {
        this.gameWon = false;
        Arrays.fill(this.board,
                    EMPTY_CELL);
    }

    /**
     * Checks if the game is complete.
     * The game is considered complete when either the player has won
     * or the maximum number of placement attempts has been reached.
     *
     * @return true if the game is complete, false otherwise
     */
    boolean isGameComplete()
    {
        final boolean gameComplete;
        gameComplete = this.gameWon || this.gamesPlayed >= this.totalPlacements;

        return gameComplete;
    }

    /**
     * Sets the game's won status.
     *
     * @param won the won status (true if won, false otherwise)
     */
    void setGameWon(final boolean won)
    {
        this.gameWon = won;
    }

    /**
     * Gets whether the game was won.
     *
     * @return true if game was won, false otherwise
     */
    boolean isGameWon()
    {
        return this.gameWon;
    }

    /**
     * Generates a random number within the defined range [MIN, MAX].
     *
     * @return a random integer between MIN_RANDOM_NUMBER and MAX_RANDOM_NUMBER
     */
    int generateNumber()
    {
        final int range;
        final int randomIntInRange;

        range            = MAX_RANDOM_NUMBER - MIN_RANDOM_NUMBER + RANGE_OFFSET;
        randomIntInRange = this.random.nextInt(range) + MIN_RANDOM_NUMBER;

        return randomIntInRange;
    }

    /**
     * Checks if the game board is completely filled (all cells occupied).
     *
     * @return true if no empty cells (EMPTY_CELL) remain, false otherwise
     */
    boolean isBoardFull()
    {
        for(final int cellValue : this.board)
        {
            if(cellValue == EMPTY_CELL)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that numbers placed on the board are in strictly ascending
     * sequence, ignoring empty cells.
     *
     * @return true if the sequence of placed numbers is valid, false otherwise
     */
    boolean isValidSequence()
    {
        int lastNumber;
        lastNumber = INVALID_NUMBER_SENTINEL;

        for(final int cellValue : this.board)
        {
            if(cellValue != EMPTY_CELL)
            {

                if(lastNumber != INVALID_NUMBER_SENTINEL && cellValue <= lastNumber)
                {
                    return false;
                }
                lastNumber = cellValue;
            }
        }
        return true;
    }

    /**
     * Returns a copy of the current game board.
     * Used by subclasses or controllers to get the state without
     * modifying the original array directly.
     *
     * @return A copy of the board array.
     */
    int[] getBoard()
    {
        final int[] boardCopy;
        boardCopy = Arrays.copyOf(this.board,
                                  this.board.length);

        return boardCopy;
    }
}