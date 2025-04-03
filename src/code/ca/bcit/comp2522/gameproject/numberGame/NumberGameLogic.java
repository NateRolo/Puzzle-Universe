package ca.bcit.comp2522.gameproject.numbergame;

import java.util.Random;

/**
 * Implements the core logic and state management for the 20-Number Challenge
 * game.
 * This class handles number generation, placement validation, board state,
 * and win/loss conditions, independent of the user interface.
 *
 * @author Nathan O
 * @version 1.2 2025
 */
final class NumberGameLogic extends BoardGame

{
    private static final int         BOARD_SIZE        = 20;
    private static final int RANGE_OFFSET      = 1;
    private static final int UPPER_BOUND_SENTINEL = BoardGame.MAX_RANDOM_NUMBER +
                                                    1;
    private static final int NO_GAMES_PLAYED      = 0;
    private static final int POSITION_INCREMENT   = 1;

    private       int    successfulPlacementsThisGame;
    private final Random random;
    /**
     * Constructs a new NumberGameLogic instance.
     */
    NumberGameLogic()
    {
        super(BOARD_SIZE);
        initializeGame();
        this.random          = new Random();
    }

    /**
     * Gets the number of successful placements in the current game round.
     *
     * @return The number of placements made in this game.
     */
    public int getSuccessfulPlacementsThisGame()
    {
        return this.successfulPlacementsThisGame;
    }

    /**
     * Gets the total number of games played during the session.
     *
     * @return the number of games played
     */
    public int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    /**
     * Gets the total number of games won by the player during the session.
     *
     * @return the number of games won
     */
    public int getGamesWon()
    {
        return this.gamesWon;
    }

    /**
     * Gets the total number of guesses (placements) made by the player across
     * all games.
     *
     * @return the total number of placements
     */
    public int getTotalPlacements()
    {
        return this.totalPlacements;
    }

    /**
     * Attempts to place the current number at the specified position.
     * Does nothing if the placement is invalid. Checks win/loss conditions.
     *
     * @param position The 0-based index where the number should be placed.
     * @return true if the number was successfully placed, false otherwise.
     */
    boolean placeNumberOnBoard(final int position)
    {
        if(isGameOver() || isGameWon())
        {
            System.out.println("[Logic] Placement attempted but game is already over.");
            return false;
        }

        if(isValidPlacement(position))
        {
            this.board[position] = this.currentNumber;
            this.successfulPlacementsThisGame++;

            this.totalPlacements++;
            System.out.printf("[Logic] Placed %d at %d. Placements this game: %d / Total: %d%n",
                              this.currentNumber,
                              position,
                              this.successfulPlacementsThisGame,
                              this.totalPlacements);
                              
            if(isBoardFull())
            {
                setGameWon(true);

                this.gamesWon++;
                System.out.println("[Logic] Board full. Game Won.");
            }
            else
            {
                this.currentNumber = generateNumber();
                System.out.printf("[Logic] Generated next number: %d%n",
                                  this.currentNumber);
            }

            return true;
        }
        else
        {
            System.out.printf("[Logic] Invalid placement: %d at position %d%n",
                              this.currentNumber,
                              position);
            return false;
        }
    }

    /*
     * Checks if the current number can be placed in any valid empty slot.
     * Helper method for determining loss condition.
     * Returns true if there is at least one valid position, false otherwise.
     */
    private boolean canPlaceCurrentNumber()
    {
        if(this.currentNumber == BoardGame.INITIAL_VALUE)
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
     * position.
     * Returns INVALID_NUMBER_SENTINEL if no non-empty cell is found to the
     * left.
     *
     * @param position The 0-based index from where to search leftwards.
     * 
     * @return The value of the left neighbor or INVALID_NUMBER_SENTINEL.
     */
    private int findLeftNeighborValue(final int position)
    {
        for(int i = position - POSITION_INCREMENT; i >= EMPTY_CELL; i--)
        {
            if(this.board[i] != EMPTY_CELL)
            {
                return this.board[i]; // Found left neighbor
            }
        }
        return INVALID_NUMBER_SENTINEL; // No left neighbor found
    }

    /*
     * Finds the value of the nearest non-empty cell to the right of the given
     * position.
     * Returns UPPER_BOUND_SENTINEL if no non-empty cell is found to the right.
     *
     * @param position The 0-based index from where to search rightwards.
     * 
     * @return The value of the right neighbor or UPPER_BOUND_SENTINEL.
     */
    private int findRightNeighborValue(final int position)
    {
        for(int i = position + POSITION_INCREMENT; i < BOARD_SIZE; i++)
        {
            if(this.board[i] != EMPTY_CELL)
            {
                return this.board[i];
            }
        }
        return UPPER_BOUND_SENTINEL;
    }

    /**
     * Initializes or resets the game state for a new round.
     * Called by startNewGame and constructor.
     */
    @Override
    void initializeGame()
    {
        super.initializeGame();
        this.successfulPlacementsThisGame = INITIAL_VALUE;
        this.currentNumber                = INITIAL_VALUE;
    }

    /**
     * Starts a new game.
     * Records game played, resets state, generates the first number.
     */
    void startNewGame()
    {
        this.gamesPlayed++;

        initializeGame();

        this.currentNumber = generateNumber();
        System.out.printf("[Logic] New Game Started (#%d). First number: %d%n",
                          this.gamesPlayed,
                          this.currentNumber);
    }

    /**
     * Checks if the game is over (win or loss condition met).
     * A game is over if the board is full (win) or if the current
     * number cannot be placed anywhere (loss).
     *
     * @return true if the game has concluded, false otherwise.
     */
    boolean isGameOver()
    {
        if(isGameWon())
        {
            return true;
        }

        if(isBoardFull())
        {

            setGameWon(true);
            return true;
        }

        final boolean canPlace = canPlaceCurrentNumber();
        if(!canPlace)
        {
            System.out.println("[Logic] Game Over check: Cannot place current number.");
        }

        return !canPlace;
    }

    /**
     * Displays the current score statistics to the console.
     * 
     * The statistics include:
     * - Total number of games played
     * - Number of games won and lost
     * - Total successful number placements across all games
     * - Average number of placements per game (if at least one game has been completed)
     */
    void showScore()
    {
        System.out.println("--- [Logic] Game Statistics ---");
        System.out.printf("Games Played: %d%n",
                          this.gamesPlayed);
        System.out.printf("Games Won: %d%n",
                          this.gamesWon);

        final int gamesLost = this.gamesPlayed - this.gamesWon;
        System.out.printf("Games Lost: %d%n",
                          gamesLost);
        System.out.printf("Total Successful Placements (all games): %d%n",
                          this.totalPlacements);

        if(this.gamesPlayed > NO_GAMES_PLAYED)
        {
            final double avgPlacements;
            avgPlacements = (double)this.totalPlacements /
                                         this.gamesPlayed;
            System.out.printf("Average Placements per Game: %.2f%n",
                              avgPlacements);
        }
        else
        {
            System.out.println("Average Placements per Game: N/A (No games completed)");
        }
        System.out.println("-------------------------------");
    }

    /**
     * Gets the current number generated for placement.
     *
     * @return the current number.
     */
    int getNextNumber()
    {
        return this.currentNumber;
    }

    /**
     * Checks if placing the {@code currentNumber} at the specified
     * {@code position}
     * is valid according to the ascending order rule.
     *
     * @param position The 0-based index to check.
     * @return true if the placement is valid, false otherwise.
     */
    private boolean isValidPlacement(final int position)
    {
        final int leftNeighborValue;
        final int rightNeighborValue;

        // 1. Check basic position validity
        if(position < EMPTY_CELL || position >= BOARD_SIZE)
        {
            return false;
        }
        if(this.board[position] != EMPTY_CELL)
        {
            return false;
        }

        // 2. Check relative to left neighbor
        leftNeighborValue = findLeftNeighborValue(position);
        if(this.currentNumber <= leftNeighborValue)
        {
            return false;
        }

        // 3. Check relative to right neighbor
        rightNeighborValue = findRightNeighborValue(position);
        if(this.currentNumber >= rightNeighborValue)
        {
            return false;
        }

        return true;
    }

    /**
     * Returns a copy of the current game board.
     *
     * @return A copy of the board array.
     */
    @Override
    int[] getBoard()
    {
        return super.getBoard();
    }

    /**
     * Generates a random number within the defined range [MIN, MAX].
     *
     * @return a random integer between MIN_RANDOM_NUMBER and MAX_RANDOM_NUMBER
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