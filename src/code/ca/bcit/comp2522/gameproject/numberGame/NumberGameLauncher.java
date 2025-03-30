package ca.bcit.comp2522.gameproject.numbergame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ca.bcit.comp2522.gameproject.Playable;

/**
 * A GUI-based number placement game where players must arrange numbers in ascending order.
 * <p>
 * Players are presented with a 4x5 grid and must place randomly generated numbers
 * in ascending order from left to right. The game continues until either:
 * - All 20 positions are filled successfully (win)
 * - A number cannot be placed anywhere on the board (loss)
 * </p>
 * 
 * @author Nathan O
 * @version 1.0 2025
 */
public class NumberGameLauncher extends
                                AbstractGame implements
                                NumberGameInterface,
                                Playable
{
    private static final int    MAX_NUMBER        = 100;
    private static final int    MIN_NUMBER        = 1;
    private static final int    DEFAULT_ATTEMPTS  = 5;
    private static final String PROMPT_MESSAGE    = "Enter a number between " +
                                                    MIN_NUMBER +
                                                    " and " +
                                                    MAX_NUMBER +
                                                    ": ";
    private static final int    BOARD_SIZE        = 20;
    private static final int    GRID_ROWS         = 4;
    private static final int    GRID_COLS         = 5;
    private static final int    GRID_GAP          = 5;
    private static final int    EMPTY_CELL        = 0;
    private static final int    INITIAL_VALUE     = 0;
    private static final int    SLEEP_INTERVAL    = 100;
    private static final String EMPTY_BUTTON_TEXT = "[ ]";

    private final Scanner scanner;
    
    private int           targetNumber;
    private int           lastGuess;
    private JFrame        frame;
    private JButton[]     buttons;
    private JLabel        statusLabel;
    private int           successfulPlacements;

    /**
     * Constructs a new NumberGameLauncher with default settings.
     * Initializes the GUI and game state.
     */
    public NumberGameLauncher()
    {
        super(DEFAULT_ATTEMPTS);
        this.scanner = new Scanner(System.in);
        createGUI();
        initializeGame();
    }

    /*
     * Creates and initializes the game's graphical user interface.
     * Sets up the game board, status display, and control buttons.
     */
    private void createGUI()
    {
        frame = new JFrame("20-Number Challenge");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create grid panel
        JPanel gridPanel = new JPanel(new GridLayout(GRID_ROWS,
                                                     GRID_COLS,
                                                     GRID_GAP,
                                                     GRID_GAP));
        buttons = new JButton[BOARD_SIZE];

        for(int i = 0; i < BOARD_SIZE; i++)
        {
            buttons[i] = new JButton(EMPTY_BUTTON_TEXT);
            final int position = i;
            buttons[i].addActionListener(e -> placeNumber(position));
            gridPanel.add(buttons[i]);
        }

        statusLabel = new JLabel("Next number: ");
        frame.add(statusLabel,
                  BorderLayout.NORTH);
        frame.add(gridPanel,
                  BorderLayout.CENTER);

        JButton newGameButton = new JButton("Try Again");
        newGameButton.addActionListener(e -> startNewGame());
        frame.add(newGameButton,
                  BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Initializes or resets the game state to starting conditions.
     * Generates new random numbers and updates the display.
     */
    @Override
    public void initializeGame()
    {
        super.initializeGame();
        targetNumber         = generateRandomNumber();
        lastGuess            = INITIAL_VALUE;
        board                = new int[BOARD_SIZE];
        successfulPlacements = INITIAL_VALUE;
        currentNumber        = generateNumber();
        updateButtons();
        statusLabel.setText("Next number: " + currentNumber);
    }

    /**
     * Generates a random number within the specified range.
     * 
     * @return a random number between MIN_NUMBER and MAX_NUMBER
     */
    private static int generateRandomNumber()
    {
        return new Random().nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
    }

    /**
     * Processes a single turn of the game.
     * Displays game status, gets user guess, and provides feedback.
     * 
     * @return true if the game should continue, false otherwise
     */
    @Override
    public boolean processTurn()
    {
        displayGameStatus();

        int userGuess = getUserGuess();
        if(! isValidGuess(userGuess))
        {
            return true;
        }

        lastGuess = userGuess;
        incrementAttempt();

        if(userGuess == targetNumber)
        {
            handleWin();
            return false;
        }

        provideHint(userGuess);
        return ! isGameComplete();
    }

    /*
     * Displays the current game status including attempt count.
     */
    private void displayGameStatus()
    {
        System.out.printf("Attempt %d/%d%n",
                          getCurrentAttempt() + 1,
                          getMaxAttempts());
    }

    /*
     * Gets a valid integer guess from the user.
     * 
     * @return the user's guess as an integer
     */
    private int getUserGuess()
    {
        System.out.print(PROMPT_MESSAGE);
        while(! scanner.hasNextInt())
        {
            System.out.println("Please enter a valid number!");
            scanner.next();
            System.out.print(PROMPT_MESSAGE);
        }
        return scanner.nextInt();
    }

    /*
     * Validates if the user's guess is within the allowed range.
     * 
     * @param guess the user's guess to validate
     * @return true if the guess is valid, false otherwise
     */
    private static boolean isValidGuess(final int guess)
    {
        if(guess < MIN_NUMBER || guess > MAX_NUMBER)
        {
            System.out.println("Number must be between " + MIN_NUMBER + " and " + MAX_NUMBER);
            return false;
        }
        return true;
    }

    /*
     * Handles the win condition when the user correctly guesses the target number.
     * Updates game statistics and prompts for a new game.
     */
    private void handleWin()
    {
        setGameWon(true);
        System.out.println("Congratulations! You won!");
        gamesWon++;
        gamesPlayed++;
        totalPlacements += successfulPlacements;
        showScore();
        int choice = JOptionPane.showConfirmDialog(frame,
                                                   "You won! Try again?",
                                                   "Game Over",
                                                   JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
        {
            startNewGame();
        }
        else
        {
            frame.dispose();
        }
    }

    /*
     * Provides a hint to the user based on their guess.
     * 
     * @param guess the user's guess to compare with the target number
     */
    private void provideHint(final int guess)
    {
        if(guess < targetNumber)
        {
            System.out.println("Too low!");
        }
        else
        {
            System.out.println("Too high!");
        }
    }

    /**
     * Starts a new game by resetting the board and generating a new number.
     * Updates the UI to reflect the new game state.
     */
    @Override
    public void startNewGame()
    {
        board                = new int[BOARD_SIZE];
        successfulPlacements = INITIAL_VALUE;
        currentNumber        = generateNumber();
        updateButtons();
        statusLabel.setText("Next number: " + currentNumber);
    }

    /**
     * Places the current number at the specified position on the board.
     * Updates the display and checks for win/loss conditions.
     *
     * @param position the board position where the number should be placed
     */
    @Override
    public void placeNumber(final int position)
    {
        if(! isValidPlacement(position))
        {
            return;
        }

        board[position] = currentNumber;
        successfulPlacements++;
        buttons[position].setText(String.valueOf(currentNumber));

        if(isBoardFull())
        {
            handleWin();
            return;
        }

        currentNumber = generateNumber();
        if(canPlaceCurrentNumber())
        {
            handleGameLost();
            return;
        }

        statusLabel.setText("Next number: " + currentNumber);
    }

    /**
     * Checks if a number can be legally placed at the specified position.
     * Validates that the placement maintains ascending order.
     *
     * @param position the position to check for valid placement
     * @return true if the current number can be placed at the position
     */
    @Override
    public boolean isValidPlacement(final int position)
    {
        if(board[position] != EMPTY_CELL) return false;

        // Check if placement maintains ascending order
        for(int i = 0; i < position; i++)
        {
            if(board[i] != EMPTY_CELL && board[i] > currentNumber) return false;
        }
        for(int i = position + 1; i < board.length; i++)
        {
            if(board[i] != EMPTY_CELL && board[i] < currentNumber) return false;
        }

        return true;
    }
    /**
     * Handles the game over state when the player has lost.
     * Updates game statistics, displays the score, and prompts the player
     * to either start a new game or exit.
     */
    private void handleGameLost()
    {
        gamesPlayed++;
        totalPlacements += successfulPlacements;
        showScore();
        int choice = JOptionPane.showConfirmDialog(frame,
                                                   "Game Over! Impossible to place " + currentNumber + ". Try again?",
                                                   "Game Over",
                                                   JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
        {
            startNewGame();
        }
        else
        {
            frame.dispose();
        }
    }

    /**
     * Displays the current game statistics.
     * Shows games won/lost and average placements per game.
     */
    @Override
    public void showScore()
    {
        double average = totalPlacements / (double)gamesPlayed;
        String message = String.format("You won %d out of %d games and lost %d out of %d games, " +
                                       "with %d successful placements, an average of %.2f per game",
                                       gamesWon,
                                       gamesPlayed,
                                       (gamesPlayed - gamesWon),
                                       gamesPlayed,
                                       totalPlacements,
                                       average);
        JOptionPane.showMessageDialog(frame,
                                      message,
                                      "Score",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * Checks if the current number can be placed anywhere on the board.
     * 
     * @return true if the current number cannot be placed, false otherwise
     */
    private boolean canPlaceCurrentNumber()
    {
        for(int i = 0; i < board.length; i++)
        {
            if(isValidPlacement(i)) return false;
        }
        return true;
    }

    /*
     * Updates the text displayed on all buttons to reflect the current board state.
     * Empty cells show the empty button text, filled cells show their number.
     */
    private void updateButtons()
    {
        for(int i = 0; i < buttons.length; i++)
        {
            if(board[i] == EMPTY_CELL)
            {
                buttons[i].setText(EMPTY_BUTTON_TEXT);
            }
            else
            {
                buttons[i].setText(String.valueOf(board[i]));
            }
        }
    }

    /**
     * Gets the current number that needs to be placed.
     * 
     * @return the current number
     */
    @Override
    public int getNextNumber()
    {
        return currentNumber;
    }

    /**
     * Determines if the game is over.
     * Game is over when the board is full or no valid placement exists.
     * 
     * @return true if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver()
    {
        return isBoardFull() || canPlaceCurrentNumber();
    }

    /**
     * Makes the game window visible and starts a new game.
     */
    public void show()
    {
        frame.setVisible(true);
        startNewGame();
    }

    /**
     * Starts the game and maintains the game loop until window is closed.
     */
    @Override
    public void play()
    {
        show();

        // Wait for the frame to be disposed
        while(frame != null && frame.isVisible())
        {
            try
            {
                Thread.sleep(SLEEP_INTERVAL);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread()
                      .interrupt();
                break;
            }
        }
    }


}
