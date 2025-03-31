package ca.bcit.comp2522.gameproject.numbergame;

/**
 * Implements the core logic and state management for the 20-Number Challenge game.
 * This class handles number generation, placement validation, board state,
 * and win/loss conditions, independent of the user interface.
 *
 * @author Nathan O
 * @version 1.1 2025
 */
class NumberGameLogic extends
                      AbstractGame implements
                      NumberGameInterface
{
    private static final int UPPER_BOUND_SENTINEL = AbstractGame.MAX_RANDOM_NUMBER + 1;
    private static final int NO_GAMES_PLAYED      = 0;
    private static final int POSITION_INCREMENT = 1;

    private int successfulPlacementsThisGame;

    /**
     * Constructs a new NumberGameLogic instance.
     */
    NumberGameLogic()
    {
        super();

        initializeGame();
    }

    /**
     * Initializes or resets the game state for a new round.
     * Called by startNewGame and constructor.
     */
    @Override
    public final void initializeGame()
    {
        super.initializeGame();
        this.successfulPlacementsThisGame = INITIAL_VALUE;
        this.currentNumber                = INITIAL_VALUE;
    }

    /**
     * Starts a new game.
     * Records game played, resets state, generates the first number.
     */
    @Override
    public final void startNewGame()
    {
        this.gamesPlayed++;

        initializeGame();

        this.currentNumber = generateNumber();
        System.out.printf("[Logic] New Game Started (#%d). First number: %d%n",
                          this.gamesPlayed,
                          this.currentNumber);
    }

    /**
     * Attempts to place the current number at the specified position.
     * Does nothing if the placement is invalid. Checks win/loss conditions.
     *
     * @param position The 0-based index where the number should be placed.
     * @return true if the number was successfully placed, false otherwise.
     */
    final boolean placeNumberOnBoard(final int position)
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

    /**
     * Places the current number at the specified position.
     * Primarily for interface compliance; prefer placeNumberOnBoard for clearer feedback.
     *
     * @param position The 0-based index where the number should be placed.
     */
    @Override
    public void placeNumber(final int position)
    {

        placeNumberOnBoard(position);
    }


    /**
     * Checks if the game is over (win or loss condition met).
     * A game is over if the board is full (win) or if the current
     * number cannot be placed anywhere (loss).
     *
     * @return true if the game has concluded, false otherwise.
     */
    @Override
    public boolean isGameOver()
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
     */
    @Override
    public void showScore()
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

            final double avgPlacements = (double)this.totalPlacements / this.gamesPlayed;
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
    @Override
    public int getNextNumber()
    {
        return this.currentNumber;
    }

    /**
     * Checks if placing the {@code currentNumber} at the specified {@code position}
     * is valid according to the ascending order rule.
     *
     * @param position The 0-based index to check.
     * @return true if the placement is valid, false otherwise.
     */
    @Override
    public boolean isValidPlacement(final int position)
    {

        if(position < EMPTY_CELL || position >= BOARD_SIZE)
        {
            return false;
        }


        if(this.board[position] != EMPTY_CELL)
        {
            return false;
        }


        int leftValue;
        
        leftValue = INVALID_NUMBER_SENTINEL;

        for(int i = position - POSITION_INCREMENT; i >= EMPTY_CELL; i--)
        {
            if(this.board[i] != EMPTY_CELL)
            {
                leftValue = this.board[i];
                break;
            }
        }

        if(this.currentNumber <= leftValue)
        {
            return false;
        }


        int rightValue = UPPER_BOUND_SENTINEL;

        for(int i = position + POSITION_INCREMENT; i < BOARD_SIZE; i++)
        {
            if(this.board[i] != EMPTY_CELL)
            {
                rightValue = this.board[i];
                break;
            }
        }

        if(this.currentNumber >= rightValue)
        {
            return false;
        }


        return true;
    }

    /*
     * Checks if the current number can be placed in any valid empty slot.
     * Helper method for determining loss condition.
     * Returns true if there is at least one valid position, false otherwise.
     */
    private final boolean canPlaceCurrentNumber()
    {

        if(this.currentNumber == AbstractGame.INITIAL_VALUE)
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

    /**
     * Gets the number of successful placements in the current game round.
     *
     * @return The number of placements made in this game.
     */
    public final int getSuccessfulPlacementsThisGame()
    {
        return this.successfulPlacementsThisGame;
    }


    public final int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    public final int getGamesWon()
    {
        return this.gamesWon;
    }

    public final int getTotalPlacements()
    {
        return this.totalPlacements;
    }


    @Override
    final int[] getBoard()
    {
        return super.getBoard();
    }
}