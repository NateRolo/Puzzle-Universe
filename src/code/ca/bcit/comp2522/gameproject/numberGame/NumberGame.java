package ca.bcit.comp2522.gameproject.numbergame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ca.bcit.comp2522.gameproject.interfaces.Replayable;

/**
 * Provides the Graphical User Interface (GUI) for the 20-Number Challenge game.
 * Handles user interaction, displays the game board and status, and interacts
 * with the NumberGameLogic class for game state management and rules.
 * Implements the Replayable interface to be launchable from a main menu.
 *
 * @author Nathan O
 * @version 1.2 2025
 */
public final class NumberGame implements
                              Replayable
{
    private static final int    GRID_ROWS         = 4;
    private static final int    GRID_COLS         = 5;
    private static final int    GRID_GAP          = 5;
    private static final String EMPTY_BUTTON_TEXT = "[ ]";
    private static final String WINDOW_TITLE      = "20-Number Challenge";
    private static final String INITIAL_MESSAGE   = "Click 'Try Again' to start a new game.";
    private static final int    BOARD_SIZE        = GRID_ROWS * GRID_COLS;
    private static final int    NO_GAMES_PLAYED   = 0;
    private static final int    PLAY             = 0;
    private static final int    QUIT             = 1;


    private final Object          lockObject = new Object();
    private final NumberGameLogic gameLogic;

    private JFrame    frame;
    private JButton[] buttons;
    private JLabel    statusLabel;

    /**
     * Constructs a new NumberGameGUI instance.
     * Initializes the game logic and creates the user interface.
     */
    public NumberGame()
    {
        this.gameLogic = new NumberGameLogic();
    }

    /**
     * Implementation of the Playable interface.
     * Ensures the game window is visible and ready for interaction.
     * This method now blocks the calling thread until the game window is
     * closed.
     */
    @Override
    public void play()
    {
        try
        {            
            SwingUtilities.invokeAndWait(this::createAndShowGUI);

            // Wait until the window is closed
            synchronized(lockObject)
            {            
                while(frame != null && frame.isDisplayable())
                {
                    lockObject.wait(); 
                }
            }
        }
        catch(final InterruptedException |
              InvocationTargetException e)
        {
            System.err.println("Error while waiting for NumberGame to finish: " +
                               e.getMessage());
            Thread.currentThread()
                  .interrupt(); 

            concludeGame();
        }
        finally
        {            
            synchronized(lockObject)
            {
                frame = null; 
            }
        }
    }

    /**
     * Helper method to encapsulate GUI creation and display logic.
     * Intended to be called via SwingUtilities.invokeAndWait or invokeLater.
     */
    private void createAndShowGUI()
    {
        boolean wasJustCreated = false;
        if(frame == null)
        {
            createGUI();
            wasJustCreated = true;
        }

        if(frame != null)
        {
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();

            if(wasJustCreated)
            {
                showWelcomeMessage();
            }
        }
        else
        {
            System.err.println("Error: Frame is null even after createGUI attempt!");
        }
    }

    /**
     * Starts a new game by resetting the logic and updating the GUI.
     */
    @Override
    public void playOneGame()
    {
        gameLogic.startNewGame();
        updateGUIState();

        if(gameLogic.isGameOver())
        {
            handleGameLost(gameLogic.getNextNumber());
        }
    }

    /**
     * Concludes the game session by disposing of the main game window.
     * Implements the method required by the Replayable interface.
     * Note: This might be called externally, but closing the window via 'X'
     * is the primary way the game ends for the user.
     */
    @Override
    public void concludeGame()
    {
        SwingUtilities.invokeLater(() ->
        {
            if(frame != null)
            {
                frame.dispose(); 
            }
        });
    }

    /**
     * Initializes and sets up the main graphical user interface components for
     * the game.
     * This includes creating the main frame, status label, grid panel with
     * buttons,
     * setting layouts, and adding necessary event listeners.
     */
    private void createGUI()
    {
        final JPanel gridPanel;

        frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Add a window listener to detect when the game window is closed
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(final WindowEvent e)
            {
                // Check if at least one game was played before closing
                if(gameLogic.getGamesPlayed() > NO_GAMES_PLAYED)
                {
                    // Display the final score summary when the window is closed
                    showFinalScoreMessage();
                }
                // Notify the waiting thread (in play()) that the game window is closed            
                synchronized(lockObject)
                {
                    NumberGame.this.frame = null;
                    lockObject.notifyAll(); 
                                           
                }
            }
        });
        frame.setLayout(new BorderLayout(GRID_GAP,
                                         GRID_GAP));

        statusLabel = new JLabel(INITIAL_MESSAGE,
                                 JLabel.CENTER);
        frame.add(statusLabel,
                  BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(GRID_ROWS,
                                              GRID_COLS,
                                              GRID_GAP,
                                              GRID_GAP));
        buttons   = new JButton[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            final int position;
            position = i;

            buttons[position] = new JButton(EMPTY_BUTTON_TEXT);
            buttons[position].addActionListener(e -> handleButtonClick(position));
            buttons[position].setEnabled(false);
            gridPanel.add(buttons[position]);
        }
        frame.add(gridPanel,
                  BorderLayout.CENTER);

        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
    }

    /*
     * Handles the logic when a grid button is clicked.
     * Delegates placement attempt to game logic and updates GUI based on
     * outcome.
     *
     * @param position The 0-based index of the clicked button.
     */
    private void handleButtonClick(final int position)
    {
        final int     numberToPlace;
        final boolean placementSuccess;
        final boolean gameOver;
        final boolean gameWon;

        if(gameLogic.isGameOver() || gameLogic.isGameWon())
        {
            return;
        }

        numberToPlace    = gameLogic.getNextNumber();
        placementSuccess = gameLogic.placeNumberOnBoard(position);

        if(placementSuccess)
        {
            gameOver = gameLogic.isGameOver();
            gameWon  = gameLogic.isGameWon();

            updateGUIState();

            if(gameOver)
            {
                if(gameWon)
                {
                    handleGameWon();
                }
                else
                {
                    handleGameLost(gameLogic.getNextNumber());
                }
            }
        }
        else
        {
        }
    }


    /*
     * Updates the GUI elements (buttons, status label) to reflect the current
     * game state obtained from NumberGameLogic.
     */
    private void updateGUIState()
    {
        final int[]   currentBoard;
        final boolean gameOver;
        final boolean gameWon;
        final int     nextNumber;

        currentBoard = gameLogic.getBoard();
        gameOver     = gameLogic.isGameOver();
        gameWon      = gameLogic.isGameWon();
        nextNumber   = gameLogic.getNextNumber();

        if(gameOver)
        {
            if(gameWon)
            {
                statusLabel.setText("You Won! All numbers placed correctly! Click 'Try Again'?");
            }
            else
            {
                statusLabel.setText("Game Over! No valid moves. Click 'Try Again'?");
            }
        }
        else
        {
            statusLabel.setText(String.format("Next number: %d - Select a slot.",
                                              nextNumber));
        }

        for(int i = 0; i < BOARD_SIZE; i++)
        {
            if(currentBoard[i] != NumberGameLogic.EMPTY_CELL)
            {
                buttons[i].setText(String.valueOf(currentBoard[i]));
                buttons[i].setEnabled(false);
            }
            else
            {
                buttons[i].setText(EMPTY_BUTTON_TEXT);
                buttons[i].setEnabled(!gameOver);
            }
        }
    }

    /*
     * Shows an initial welcome message with instructions.
     * Offers "Try Again" (start game) and "Quit" options.
     */
    private void showWelcomeMessage()
    {
        final String[] options;
        final int      choice;

        options = new String[] {"Play", "Quit"};
        choice  = JOptionPane.showOptionDialog(frame,
                                               "Welcome to the 20-Number Challenge! Click 'Play' to start.",
                                               "Game Start",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.INFORMATION_MESSAGE,
                                               null,
                                               options,
                                               options[PLAY]);

        if(choice == PLAY)
        {
            playOneGame();
        }
        else
        {
            concludeGame();
        }
    }

    /*
     * Handles the display and actions for the game win condition.
     */
    private void handleGameWon()
    {
        final int choice;
        

        choice = JOptionPane.showConfirmDialog(frame,
                                               buildScoreString(true),
                                               "You Won!",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.INFORMATION_MESSAGE);
        if(choice == JOptionPane.YES_OPTION)
        {
            playOneGame();
        }
        else
        {
            concludeGame();
        }
    }

    /*
     * Handles the display and actions for the game loss condition.
     *
     * @param impossibleNumber The number that could not be placed.
     */
    private void handleGameLost(final int impossibleNumber)
    {
        final StringBuilder fullMsgBuilder;

        final String lossMsg;
        final String fullMsg;
        final int    choice;

        fullMsgBuilder = new StringBuilder();

        lossMsg = String.format("Impossible to place the next number: %d.",
                                impossibleNumber);

        fullMsgBuilder.append(lossMsg);
        fullMsgBuilder.append("\n");
        fullMsgBuilder.append(buildScoreString(false));
        fullMsgBuilder.append("\n");
        fullMsgBuilder.append("Do you want to play again?");
        fullMsg = fullMsgBuilder.toString();

        choice = JOptionPane.showConfirmDialog(frame,
                                               fullMsg,
                                               "Game Over!",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.WARNING_MESSAGE);

        if(choice == JOptionPane.YES_OPTION)
        {
            playOneGame();
        }
        else
        {
            concludeGame();
        }
    }

    /*
     * Shows a final message with the overall score when the window is closed.
     */
    private void showFinalScoreMessage()
    {
        JOptionPane.showMessageDialog(null,
                                      buildScoreString(gameLogic.isGameWon()),
                                      "Final Score",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * Builds a string summarizing the current score status using data from
     * gameLogic.
     *
     * @param gameJustWon Hint whether the last game ended in a win (for
     * phrasing).
     * 
     * @return A formatted string with score details.
     */
    private String buildScoreString(final boolean gameJustWon)
    {
        final int    played;
        final int    won;
        final int    totalPlaced;
        final int    lost;
        final String winLossRatio;
        final String placementInfo;
        final double avgPlacements;
        final String firstLine;
        final String scoreResults;

        played      = gameLogic.getGamesPlayed();
        won         = gameLogic.getGamesWon();
        totalPlaced = gameLogic.getTotalPlacements();
        lost        = played - won;

        winLossRatio = String.format("Won %d / Lost %d (Total %d)",
                                     won,
                                     lost,
                                     played);

        if(played > NO_GAMES_PLAYED)
        {
            avgPlacements = (double)totalPlaced / played;
            placementInfo = String.format("Avg %.2f placements/game (%d total placements).",
                                          avgPlacements,
                                          totalPlaced);
        }
        else
        {
            placementInfo = "No games completed yet.";
        }

        firstLine = gameJustWon ? "You Won! Game Stats:" : "Stats:";

        scoreResults = String.format("%s\n%s\n%s",
                             firstLine,
                             winLossRatio,
                             placementInfo);
        
        return scoreResults;
    }
}
