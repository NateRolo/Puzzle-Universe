package ca.bcit.comp2522.gameproject.numberGame;

import java.util.Random;


//magic numbers all over file
abstract class AbstractGame
{
    protected int[]  board;
    protected int    currentNumber;
    protected int    gamesPlayed;
    protected int    gamesWon;
    protected int    totalPlacements;
    protected Random random;

    AbstractGame()
    {
        board           = new int[20];
        random          = new Random();
        gamesPlayed     = 0;
        gamesWon        = 0;
        totalPlacements = 0;
    }

    protected int generateNumber()
    {
        return random.nextInt(1000) + 1;
    }

    protected boolean isBoardFull()
    {
        for(int num : board)
        {
            if(num == 0) return false;
        }
        return true;
    }

    protected boolean isValidSequence()
    {
        for(int i = 1; i < board.length; i++)
        {
            if(board[i] != 0 && board[i - 1] != 0 && board[i] < board[i - 1])
            {
                return false;
            }
        }
        return true;
    }
}