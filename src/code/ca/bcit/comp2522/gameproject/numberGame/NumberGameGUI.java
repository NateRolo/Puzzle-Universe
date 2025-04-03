package ca.bcit.comp2522.gameproject.numbergame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ca.bcit.comp2522.gameproject.Playable;

/**
 * Provides the Graphical User Interface (GUI) for the 20-Number Challenge game.
 * Handles user interaction, displays the game board and status, and interacts
 * with the NumberGameLogic class for game state management and rules.
 * Implements the Playable interface to be launchable from a main menu.
 *
 * @author Nathan O
 * @version 1.1 2025
 */
public final class NumberGameGUI implements
                           Playable
{
    private static final int    GRID_ROWS         = 4;
    private static final int    GRID_COLS         = 5;
    private static final int    GRID_GAP          = 5;
    private static final String EMPTY_BUTTON_TEXT = "[ ]";
    private static final String WINDOW_TITLE      = "20-Number Challenge";
    private static final String INITIAL_MESSAGE   = "Click 'Try Again' to start a new game.";
    private static final int    BOARD_SIZE        = GRID_ROWS * GRID_COLS;
    private static final int    NO_GAMES_PLAYED   = 0;
    private static final int    TRY_AGAIN         = 0;

    private final NumberGameLogic gameLogic;

    private JFrame    frame;
    private JButton[] buttons;
    private JLabel    statusLabel;

    /**
     * Constructs a new NumberGameGUI instance.
     * Initializes the game logic and creates the user interface.
     */

    public NumberGameGUI()
    {
        this.gameLogic = new NumberGameLogic();
    }

    /**
     * Implementation of the Playable interface.
     * Ensures the game window is visible and ready for interaction.
     * This might be called from an external launcher/main menu.
     */
    @Override
    public void play()
    {
        SwingUtilities.invokeLater(() ->
        {
            boolean wasJustCreated;

            wasJustCreated = false;
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
                System.err.println("Error: Frame is null even after createGUI attempt in play()!");
            }
        });
    }

    /**
     * Initializes and sets up the main graphical user interface components for the game.
     * This includes creating the main frame, status label, grid panel with buttons,
     * setting layouts, and adding necessary event listeners.
     */
    private final void createGUI()
    {
        final JPanel gridPanel;

        frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(final WindowEvent e)
            {
                if(gameLogic.getGamesPlayed() > NO_GAMES_PLAYED)
                {
                    showFinalScoreMessage();
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
     * Starts a new game by resetting the logic and updating the GUI.
     */
    private void startNewGame()
    {
        gameLogic.startNewGame();
        updateGUIState();

        if(gameLogic.isGameOver())
        {
            handleGameLost(gameLogic.getNextNumber());
        }
    }

    /*
     * Handles the logic when a grid button is clicked.
     * Delegates placement attempt to game logic and updates GUI based on outcome.
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
            System.out.printf("[GUI] Invalid click on pos %d for number %d.%n",
                              position,
                              numberToPlace);
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
                buttons[i].setEnabled(! gameOver);
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

        options = new String[] {"Try Again", "Quit"};
        choice  = JOptionPane.showOptionDialog(frame,
                                               "Welcome to the 20-Number Challenge! Click 'Try Again' to start.",
                                               "Game Start",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.INFORMATION_MESSAGE,
                                               null,
                                               options,
                                               options[TRY_AGAIN]);

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
     * Handles the display and actions for the game win condition.
     */
    private void handleGameWon()
    {
        final int choice;

        System.out.println("[GUI] Handling Game Won.");
        gameLogic.showScore();

        choice = JOptionPane.showConfirmDialog(frame,
                                               buildScoreString(true),
                                               "You Won!",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.INFORMATION_MESSAGE);
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
     * Handles the display and actions for the game loss condition.
     *
     * @param impossibleNumber The number that could not be placed.
     */
    private void handleGameLost(final int impossibleNumber)
    {
        final String lossMsg;
        final String fullMsg;
        final int    choice;

        System.out.printf("[GUI] Handling Game Lost (Cannot place %d).%n",
                          impossibleNumber);
        gameLogic.showScore();

        lossMsg = String.format("Impossible to place the next number: %d.",
                                impossibleNumber);
        fullMsg = lossMsg + "\n" + buildScoreString(false);

        choice = JOptionPane.showConfirmDialog(frame,
                                               fullMsg,
                                               "Game Over!",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.WARNING_MESSAGE);

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
     * Shows a final message with the overall score when the window is closed.
     */
    private void showFinalScoreMessage()
    {
        System.out.println("[GUI] Window Closed - Showing Final Stats.");

        JOptionPane.showMessageDialog(null,
                                      buildScoreString(gameLogic.isGameWon()),
                                      "Final Score",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * Builds a string summarizing the current score status using data from gameLogic.
     *
     * @param gameJustWon Hint whether the last game ended in a win (for phrasing).
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

        firstLine = gameJustWon ? "You Won! Game Stats:" : "Game Over! Stats:";
        return String.format("%s\n%s\n%s",
                             firstLine,
                             winLossRatio,
                             placementInfo);
    }
}
