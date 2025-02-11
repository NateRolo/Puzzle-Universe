package ca.bcit.comp2522.gameproject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Represents a player's score in the game.
 * <p>
 * This class tracks various statistics about a player's performance including
 * when they played, number of games, and success rates at different attempts.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public class Score
{
    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectAttempts;
    
    final LocalDateTime currentTime;
    final DateTimeFormatter formatter;
    final String formattedDateTime;

    /**
     * Constructs a new Score object.
     */
    public Score()
    {
        this.currentTime = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = currentTime.format(formatter);

        this.numGamesPlayed = 0;
        this.numCorrectFirstAttempt = 0;
        this.numCorrectSecondAttempt = 0;
        this.numIncorrectAttempts = 0;
    }

    /**
     * Gets the date and time when the game was played.
     *
     * @return the date and time played
     */
    public final LocalDateTime getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Gets the number of games played.
     *
     * @return number of games played
     */
    public final int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Gets the number of correct answers on first attempt.
     *
     * @return number of correct first attempts
     */
    public final int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    /**
     * Gets the number of correct answers on second attempt.
     *
     * @return number of correct second attempts
     */
    public final int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    /**
     * Gets the number of incorrect answers after two attempts.
     *
     * @return number of incorrect attempts after two tries
     */
    public final int getNumIncorrectAttempts()
    {
        return numIncorrectAttempts;
    }

    /**
     * Increments the number of games played.
     */
    public final void incrementNumGamesPlayed()
    {
        this.numGamesPlayed++;
    }

    /**
     * Increments the number of correct first attempts.
     */
    public final void incrementNumCorrectFirstAttempt()
    {
        this.numCorrectFirstAttempt++;
    }

    /**
     * Increments the number of correct second attempts.
     */
    public final void incrementNumCorrectSecondAttempt()
    {
        this.numCorrectSecondAttempt++;
    }

    /**
     * Increments the number of incorrect attempts after two tries.
     */
    public final void incrementNumIncorrectAttempts()
    {
        this.numIncorrectAttempts++;
    }
}