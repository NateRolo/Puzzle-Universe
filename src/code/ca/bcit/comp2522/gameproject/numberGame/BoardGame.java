package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Arrays;


/**
 * Abstract base class for number-based games.
 * Provides common functionality for game initialization, state tracking,
 * and validation of game rules.
 * 
 * @author Nathan O
 * @version 1.1 2025
 */
abstract class BoardGame
{
    static final int BOARD_SIZE_MIN    = 0;
    static final int MAX_RANDOM_NUMBER = 1000;
    static final int MIN_RANDOM_NUMBER = 1;
    static final int EMPTY_CELL        = 0;
    static final int INITIAL_VALUE     = 0;

    private boolean gameWon;

    int[] board;
    int   currentNumber;
    int   gamesWon;
    int   totalPlacements;
    int   gamesPlayed;

    /**
     * Constructs an BoardGame. Initializes game statistics and the random
     * number generator.
     */
    BoardGame(final int boardSize)
    {
        validateBoardSize(boardSize);

        this.board           = new int[boardSize];
        this.gamesPlayed     = INITIAL_VALUE;
        this.gamesWon        = INITIAL_VALUE;
        this.totalPlacements = INITIAL_VALUE;
        this.gameWon         = false;
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
     * Initializes the game state for a new round.
     * Resets the game won status and clears the board.
     */
    void playOneGame()
    {
        this.gameWon = false;
        Arrays.fill(this.board,
                    EMPTY_CELL);
    }

    /**
     * Validates the boardSize for the BoardGame constructor.
     *
     * @param boardSize the number of cells on the board
     */
    private static void validateBoardSize(final int boardSize)
    {
        if(boardSize < BOARD_SIZE_MIN)
        {
            throw new IllegalArgumentException("Board size cannot be less than: " +
                                               BOARD_SIZE_MIN);
        }
    }
}
