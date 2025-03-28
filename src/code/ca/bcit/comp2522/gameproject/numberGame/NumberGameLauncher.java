package ca.bcit.comp2522.gameproject.numbergame;

import ca.bcit.comp2522.gameproject.Playable;

import java.awt.*;
import javax.swing.*;

public class NumberGameLauncher extends
                        AbstractGame
                        implements NumberGameInterface,
                                   Playable
{
    private JFrame    frame;
    private JButton[] buttons;
    private JLabel    statusLabel;
    private int       successfulPlacements;

    public NumberGameLauncher()
    {
        super();
        createGUI();
    }

    private void createGUI()
    {
        frame = new JFrame("20-Number Challenge");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Changed to DISPOSE_ON_CLOSE to work better with main menu
        frame.setLayout(new BorderLayout());

        // Create grid panel
        JPanel gridPanel = new JPanel(new GridLayout(4,
                                                     5,
                                                     5,
                                                     5));
        buttons = new JButton[20];

        for(int i = 0; i < 20; i++) // magic number
        {
            buttons[i] = new JButton("[ ]");
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

    @Override
    public void startNewGame()
    {
        board                = new int[20]; //magic number
        successfulPlacements = 0;
        currentNumber        = generateNumber();
        updateButtons();
        statusLabel.setText("Next number: " + currentNumber);
    }

    @Override
    public void placeNumber(int position)
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
            handleGameWon();
            return;
        }

        currentNumber = generateNumber();
        if(! canPlaceCurrentNumber())
        {
            handleGameLost();
            return;
        }

        statusLabel.setText("Next number: " + currentNumber);
    }

    @Override
    public boolean isValidPlacement(int position)
    {
        if(board[position] != 0) return false; //magic number

        // Check if placement maintains ascending order
        for(int i = 0; i < position; i++)
        {
            if(board[i] != 0 && board[i] > currentNumber) return false;
        }
        for(int i = position + 1; i < board.length; i++) //magic number
        {
            if(board[i] != 0 && board[i] < currentNumber) return false;
        }

        return true;
    }

    private void handleGameWon()
    {
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

    private boolean canPlaceCurrentNumber()
    {
        for(int i = 0; i < board.length; i++)
        {
            if(isValidPlacement(i)) return true;
        }
        return false;
    }

    private void updateButtons()
    {
        for(int i = 0; i < buttons.length; i++)
        {
            buttons[i].setText(board[i] == 0 ? "[ ]" : String.valueOf(board[i])); //magic number
        }
    }

    @Override
    public int getNextNumber()
    {
        return currentNumber;
    }

    @Override
    public boolean isGameOver()
    {
        return isBoardFull() || !canPlaceCurrentNumber();
    }

    public void show()
    {
        frame.setVisible(true);
        startNewGame();
    }

    @Override
    public void play()
    {
        show();

        // Wait for the frame to be disposed
        while(frame != null && frame.isVisible())
        {
            try
            {
                Thread.sleep(100);
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
