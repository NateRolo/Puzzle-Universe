package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Random;

/**
 * Implements the core logic and state management for the 20-Number Challenge
 * game. This class handles number generation, placement validation, board
 * state, and win/loss conditions, independent of the user interface.
 *
 * @author Nathan O
 * @version 1.3 2025 
 */
final class NumberGameLogic extends
                            BoardGame

{
    private static final int BOARD_SIZE   = 20;
    private static final int RANGE_OFFSET = 1;

    private static final int NO_EMPTY_CELL_LEFT  = - 1;
    private static final int NO_EMPTY_CELL_RIGHT = BoardGame.MAX_RANDOM_NUMBER +
                                                   1;

    private static final int POSITION_INCREMENT = 1;

    private int          successfulPlacementsThisGame;
    private final Random random;

    /**
     * Constructs a new NumberGameLogic instance.
     */
    NumberGameLogic()
    {
        super(BOARD_SIZE);
        this.random = new Random();
    }

    /**
     * Starts a new game. Records game played, resets state, generates the first
     * number.
     */
    void startNewGame()
    {
        super.incrementGamesPlayed();

        playOneGame();

        super.setCurrentNumber(generateNumber());
    }

    /**
     * Gets the number of successful placements in the current game round.
     *
     * @return The number of placements made in this game.
     */
    int getSuccessfulPlacementsThisGame()
    {
        return this.successfulPlacementsThisGame;
    }

    /**
     * Attempts to place the current number at the specified position. Does
     * nothing if the placement is invalid. Checks win/loss conditions.
     *
     * @param position The 0-based index where the number should be placed.
     * @return true if the number was successfully placed, false otherwise.
     */
    boolean placeNumberOnBoard(final int position)
    {
        if(isGameOver() || super.isGameWon())
        {
            return false;
        }

        if(isValidPlacement(position))
        {
            // Get the current number from the parent class
            final int currentNum;
            currentNum = super.getCurrentNumber();

            // Place the number on the board at the specified position
            super.setValueOfBoardPosition(position,
                                          currentNum);

            this.successfulPlacementsThisGame++;

            // Update the total placements counter in the parent class
            super.incrementTotalPlacements();

            // Check if the board is now full after this placement
            if(super.isBoardFull())
            {
                super.setGameWon(true);
                super.incrementGamesWon();
            }
            else
            {
                super.setCurrentNumber(generateNumber());
            }

            // Return true to indicate successful placement
            return true;
        }
        else
        {
            // Return false if the placement was invalid
            return false;
        }
    }

    /**
     * Gets the current number generated for placement. Delegates to superclass.
     * 
     * @return the current number.
     */
    int getNextNumber()
    {
        return super.getCurrentNumber();
    }

    /**
     * Checks if the game is over (win or loss condition met). A game is over if
     * the board is full (win) or if the current number cannot be placed
     * anywhere (loss).
     *
     * @return true if the game has concluded, false otherwise.
     */
    boolean isGameOver()
    {
        if(super.isGameWon())
        {
            return true;
        }

        if(super.isBoardFull())
        {
            super.setGameWon(true);
            return true;
        }

        final boolean canPlace;
        canPlace = canPlaceCurrentNumber();

        return !canPlace;
    }

    /**
     * Initializes or resets the game state for a new round. Called by
     * startNewGame.
     */
    @Override
    void playOneGame()
    {
        super.playOneGame();
        this.successfulPlacementsThisGame = INITIAL_VALUE;
    }

    /*
     * Checks if the current number can be placed in any valid empty slot.
     * Helper method for determining loss condition. Returns true if there is at
     * least one valid position, false otherwise.
     */
    private boolean canPlaceCurrentNumber()
    {

        if(super.getCurrentNumber() == BoardGame.INITIAL_VALUE)
        {
            return true;
        }

        for(int i = 0; i < BOARD_SIZE; i++)
        {
            if(isValidPlacement(i))
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Finds the value of the nearest non-empty cell to the left of the given
     * position. Returns {@value #NO_EMPTY_CELL_LEFT} if no non-empty cell is
     * found to the left.
     * 
     * @param position The 0-based index from where to search leftwards.
     * @return The value of the left neighbor or {@value #NO_EMPTY_CELL_LEFT}.
     */
    private int findLeftNeighborValue(final int position)
    {
        int cellValue;
        for(int leftNeighbor = position - POSITION_INCREMENT; leftNeighbor >=
                                                              EMPTY_CELL; leftNeighbor--)
        {
            cellValue = super.getValueOfBoardPosition(leftNeighbor);

            if(cellValue != EMPTY_CELL)
            {
                // Found left neighbor
                return cellValue;
            }
        }
        // No left neighbor found
        return NO_EMPTY_CELL_LEFT;
    }

    /*
     * Finds the value of the nearest non-empty cell to the right of the given
     * position. Returns {@value #NO_EMPTY_CELL_RIGHT} if no non-empty cell is
     * found to the right.
     * 
     * @param position The 0-based index from where to search rightwards.
     * @return The value of the right neighbor or {@value #NO_EMPTY_CELL_RIGHT}.
     */
    private int findRightNeighborValue(final int position)
    {
        for(int rightNeighbor = position + POSITION_INCREMENT; rightNeighbor <
                                                               BOARD_SIZE; rightNeighbor++)
        {
            final int cellValue;
            cellValue = super.getValueOfBoardPosition(rightNeighbor);

            if(cellValue != EMPTY_CELL)
            {
                // Found right neighbor
                return cellValue;
            }
        }
        // No right neighbor found
        return NO_EMPTY_CELL_RIGHT;
    }

    /*
     * Checks if placing the current number at the specified position is valid
     * according to the ascending order rule.
     * 
     * @param position The 0-based index to check.
     * @return true if the placement is valid, false otherwise.
     */
    private boolean isValidPlacement(final int positionOnBoard)
    {
        final int leftNeighborValue;
        final int rightNeighborValue;
        final int currentNum;

        currentNum = super.getCurrentNumber();

        // 1. Check basic position validity
        if(positionOnBoard < EMPTY_CELL || positionOnBoard >= BOARD_SIZE)
        {
            return false;
        }

        if(super.getValueOfBoardPosition(positionOnBoard) != EMPTY_CELL)
        {
            return false;
        }

        // 2. Check relative to left neighbor
        leftNeighborValue = findLeftNeighborValue(positionOnBoard);
        if(currentNum <= leftNeighborValue)
        {
            return false;
        }

        // 3. Check relative to right neighbor
        rightNeighborValue = findRightNeighborValue(positionOnBoard);
        if(currentNum >= rightNeighborValue)
        {
            return false;
        }

        return true;
    }

    /*
     * Generates a random number within the defined range [MIN, MAX].
     * 
     * @return a random integer between {@value #MIN_RANDOM_NUMBER} and
     *         {@value #MAX_RANDOM_NUMBER}.
     */
    private int generateNumber()
    {
        final int range;
        final int randomIntInRange;

        range            = MAX_RANDOM_NUMBER - MIN_RANDOM_NUMBER + RANGE_OFFSET;
        randomIntInRange = this.random.nextInt(range) + MIN_RANDOM_NUMBER;

        return randomIntInRange;
    }
}
