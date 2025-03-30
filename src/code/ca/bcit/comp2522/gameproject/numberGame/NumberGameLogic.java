package ca.bcit.comp2522.gameproject.numbergame;

/**
 * Implements the core logic and state management for the 20-Number Challenge game.
 * This class handles number generation, placement validation, board state,
 * and win/loss conditions, independent of the user interface.
 *
 * @author Nathan O 
 * @version 1.1 2025
 */
class NumberGameLogic extends AbstractGame implements NumberGameInterface
{
    private int successfulPlacementsThisGame;

    /**
     * Constructs a new NumberGameLogic instance.
     */
    NumberGameLogic()
    {
        super();
        initializeGame(); // Initialize state on creation
    }

    /**
     * Initializes or resets the game state for a new round.
     * Called by startNewGame and constructor.
     */
    @Override
    public final void initializeGame()
    {
        super.initializeGame(); 
        successfulPlacementsThisGame = 0;
        currentNumber = 0; // No number generated until startNewGame
    }

    /**
     * Starts a new game.
     * Records game played, resets state, generates the first number.
     */
    @Override
    public void startNewGame()
    {
        gamesPlayed++;
        initializeGame(); // Reset board, placements, etc.
        currentNumber = generateNumber(); // Generate the first number
        System.out.printf("[Logic] New Game Started (#%d). First number: %d%n", gamesPlayed, currentNumber);
    }

    /**
     * Attempts to place the current number at the specified position.
     * Does nothing if the placement is invalid.
     *
     * @param position The 0-based index where the number should be placed.
     * @return true if the number was successfully placed, false otherwise.
     */
    public boolean placeNumberOnBoard(final int position)
    {
        if (isGameOver() || isGameWon()) {
            System.out.println("[Logic] Placement attempted but game is already over.");
            return false;
        }

        if (isValidPlacement(position))
        {
            board[position] = currentNumber;
            successfulPlacementsThisGame++;
            totalPlacements++; // Increment total across all games
            System.out.printf("[Logic] Placed %d at %d. Placements this game: %d / Total: %d%n",
                              currentNumber, position, successfulPlacementsThisGame, totalPlacements);

            // Check for win immediately after placement
            if (isBoardFull()) {
                setGameWon(true);
                System.out.println("[Logic] Board full. Game Won.");
            } else {
                 // Generate the next number only if the game is not won
                currentNumber = generateNumber();
                System.out.printf("[Logic] Generated next number: %d%n", currentNumber);
                 // Loss condition is checked externally via isGameOver -> canPlaceCurrentNumber
            }
            return true; // Placement successful
        }
        else
        {
            System.out.printf("[Logic] Invalid placement: %d at position %d%n", currentNumber, position);
            return false; // Placement failed
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
        placeNumberOnBoard(position); // Delegate to the method with return value
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
        if (isGameWon()) // Already won (board became full)
        {
            return true;
        }
        if (isBoardFull()) // Board is full, ensure win state is set
        {
            setGameWon(true);
            return true;
        }
        // Loss condition: If board is not full, but current number cannot be placed
        boolean canPlace = canPlaceCurrentNumber();
        if (!canPlace) {
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
        // This implementation remains console-based as per interface.
        // GUI class will build dialog strings separately.
        System.out.println("--- [Logic] Game Statistics ---");
        System.out.printf("Games Played: %d%n", gamesPlayed);
        System.out.printf("Games Won: %d%n", gamesWon);
        int gamesLost = gamesPlayed - gamesWon;
        System.out.printf("Games Lost: %d%n", gamesLost);
        System.out.printf("Total Successful Placements (all games): %d%n", totalPlacements);

        if (gamesPlayed > 0)
        {
            double avgPlacements = (double) totalPlacements / gamesPlayed;
            System.out.printf("Average Placements per Game: %.2f%n", avgPlacements);
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
        return currentNumber;
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
        if (position < 0 || position >= BOARD_SIZE) return false; // Bounds check

        if (board[position] != EMPTY_CELL)
        {
            return false; // Slot not empty
        }

        int leftValue = -1; // Value lower than any valid number
        for (int i = position - 1; i >= 0; --i) {
            if (board[i] != EMPTY_CELL) {
                leftValue = board[i];
                break;
            }
        }
        if (currentNumber <= leftValue) {
             return false; // Must be strictly greater than left neighbor
        }

        int rightValue = MAX_RANDOM_NUMBER + 1; // Value higher than any valid number
         for (int i = position + 1; i < BOARD_SIZE; ++i) {
            if (board[i] != EMPTY_CELL) {
                rightValue = board[i];
                break;
            }
        }
        if (currentNumber >= rightValue) {
            return false; // Must be strictly smaller than right neighbor
        }

        return true; // All checks passed
    }

    /*
     * Checks if the current number can be placed in any valid empty slot.
     * Helper method for determining loss condition.
     *
     * @return true if there is at least one valid position, false otherwise.
     */
    private boolean canPlaceCurrentNumber()
    {
        // Need to handle the case where currentNumber is 0 (before first generation)
        if (currentNumber == 0) return true; // Or false? Assume true until a number exists.

        for (int i = 0; i < BOARD_SIZE; i++)
        {
            if (isValidPlacement(i)) // Checks empty and order rules
            {
                return true; // Found a valid spot
            }
        }
        return false; // No valid spots found
    }

     /**
     * Gets the number of successful placements in the current game round.
     *
     * @return The number of placements made in this game.
     */
    public int getSuccessfulPlacementsThisGame()
    {
        return successfulPlacementsThisGame;
    }

    // --- Getters for Score Information (used by GUI) ---

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getTotalPlacements() {
        return totalPlacements;
    }
} 