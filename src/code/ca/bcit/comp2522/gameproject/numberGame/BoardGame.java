package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Arrays;


/**
 * Abstract base class for number-based games. Provides common functionality for
 * game initialization, state tracking, and validation of game rules.
 * 
 * @author Nathan O
 * @version 1.2 2025 
 */
abstract class BoardGame
{
    static final int BOARD_SIZE_MIN    = 0;
    static final int MAX_RANDOM_NUMBER = 1000;
    static final int MIN_RANDOM_NUMBER = 1;
    static final int EMPTY_CELL        = 0;
    static final int INITIAL_VALUE     = 0;
    
    private final int[]   board;

    private boolean gameWon;
    private int     currentNumber;
    private int     gamesWon;
    private int     totalPlacements;
    private int     gamesPlayed;

    /**
     * Constructs an BoardGame. Initializes game statistics.
     * 
     * @param boardSize Size of the game board.
     */
    BoardGame(final int boardSize)
    {
        validateBoardSize(boardSize);

        this.board           = new int[boardSize];
        this.gamesPlayed     = INITIAL_VALUE;
        this.gamesWon        = INITIAL_VALUE;
        this.totalPlacements = INITIAL_VALUE;
        this.gameWon         = false;
        this.currentNumber   = INITIAL_VALUE;
    }

    /**
     * Initializes the game state for a new round. Resets the game won status
     * and clears the board.
     */
    void playOneGame()
    {
        this.gameWon = false;
        Arrays.fill(this.board,
                    EMPTY_CELL);
        this.currentNumber = INITIAL_VALUE;
    }

    /**
     * Gets the value at a specific board position.
     * 
     * @param index The index of the cell.
     * @return The value at the specified index.
     */
    int getValueOfBoardPosition(final int boardPosition)
    {
        validateBoardPosition(boardPosition);

        return this.board[boardPosition];
    }

    /**
     * Sets the value at a specific board position.
     * 
     * @param index The index of the cell.
     * @param value The value to set.
     */
    void setValueOfBoardPosition(final int positionOnBoard,
                                 final int value)
    {
        validateBoardPosition(positionOnBoard);
        validateNumber(value);

        this.board[positionOnBoard] = value;
    }

    /**
     * Sets the current number to be placed.
     * 
     * @param number The new current number.
     */
    void setCurrentNumber(final int number)
    {
        validateNumber(number);
        
        this.currentNumber = number;
    }

    /**
     * Increments the games won counter.
     */
    void incrementGamesWon()
    {
        this.gamesWon++;
    }

    /**
     * Increments the total placements counter.
     */
    void incrementTotalPlacements()
    {
        this.totalPlacements++;
    }

    /**
     * Increments the games played counter.
     */
    void incrementGamesPlayed()
    {
        this.gamesPlayed++;
    }

    /**
     * Gets the current number.
     *
     * @return the current number
     */
    final int getCurrentNumber()
    {
        return this.currentNumber;
    }

    /**
     * Gets the number of games played.
     *
     * @return the number of games played
     */
    final int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    /**
     * Gets the number of games won.
     *
     * @return the number of games won
     */
    final int getGamesWon()
    {
        return this.gamesWon;
    }

    /**
     * Gets the number of total placements.
     *
     * @return the number of total placements
     */
    final int getTotalPlacements()
    {
        return this.totalPlacements;
    }

    /**
     * Gets whether the game was won.
     *
     * @return true if game was won, false otherwise
     */
    final boolean isGameWon()
    {
        return this.gameWon;
    }

    /**
     * Checks if the game board is completely filled (all cells occupied).
     *
     * @return true if no empty cells (EMPTY_CELL) remain, false otherwise
     */
    final boolean isBoardFull()
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
     * Returns a copy of the current game board. Used by subclasses or
     * controllers to get the state without modifying the original array
     * directly.
     *
     * @return A copy of the board array.
     */
    final int[] getBoard()
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
    final void setGameWon(final boolean won)
    {
        this.gameWon = won;
    }

    /*
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

    /*
     * Validates the boardIndex for the BoardGame constructor.
     * 
     * @param boardIndex the index of the cell.
     */
    private void validateBoardPosition(final int boardPosition)
    {
        if(boardPosition < BOARD_SIZE_MIN || boardPosition >= this.board.length)
        {
            throw new ArrayIndexOutOfBoundsException("Index " +
                                                     boardPosition +
                                                     " out of bounds for board size " +
                                                     this.board.length);
        }
    }

    /*
     * Validates the boardValue for the BoardGame constructor.
     * 
     * @param boardValue the value to set.
     */
    private void validateNumber(final int number)
    {
        if(number < MIN_RANDOM_NUMBER || number > MAX_RANDOM_NUMBER)
        {
            throw new IllegalArgumentException("Board value must be between " +
                                               MIN_RANDOM_NUMBER +
                                               " and " +
                                               MAX_RANDOM_NUMBER);
        }
    }
}
