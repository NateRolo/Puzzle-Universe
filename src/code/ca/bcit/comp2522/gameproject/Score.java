package ca.bcit.comp2522.gameproject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private static final int CORRECT_FIRST_GUESS_SCORE  = 2;
    private static final int CORRECT_SECOND_GUESS_SCORE = 1;
    private              int numGamesPlayed;
    private              int numCorrectFirstAttempt;
    private              int numCorrectSecondAttempt;
    private              int numIncorrectTwoAttempts;
    private              int score;

    final LocalDateTime     currentTime;
    final DateTimeFormatter formatter;
    final String            formattedDateTime;

    /**
     * Constructs a new Score object.
     */
    public Score()
    {
        this.currentTime  = LocalDateTime.now();
        formatter         = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = currentTime.format(formatter);

        this.numGamesPlayed          = 0;
        this.numCorrectFirstAttempt  = 0;
        this.numCorrectSecondAttempt = 0;
        this.numIncorrectTwoAttempts = 0;
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
    public final int getNumIncorrectTwoAttempts()
    {
        return numIncorrectTwoAttempts;
    }

    /**
     * Increments the number of games played.
     */
    public final void incrementNumGamesPlayed()
    {
        this.numGamesPlayed++;
    }

    /**
     * Increments the number of correct first attempt and adds
     * 2 points to score.
     */
    public final void incrementNumCorrectFirstAttempt()
    {
        this.numCorrectFirstAttempt++;
        this.score += CORRECT_FIRST_GUESS_SCORE;
    }

    /**
     * Increments the number of correct second attempts and adds
     * 1 point to score.
     */
    public final void incrementNumCorrectSecondAttempt()
    {
        this.numCorrectSecondAttempt++;
        this.score += CORRECT_SECOND_GUESS_SCORE;
    }

    /**
     * Increments the number of incorrect attempts after two tries.
     */
    public final void incrementNumIncorrectTwoAttempts()
    {
        this.numIncorrectTwoAttempts++;
    }


    public final List<String> formatScore()
    {
        final List<String> score;
        score = new ArrayList<>();

        score.add("Date and Time: " + this.formattedDateTime);
        score.add("Games Played: " + this.numGamesPlayed);
        score.add("Correct First Attempts: " + this.numCorrectFirstAttempt);
        score.add("Correct Second Attempts: " + this.numCorrectSecondAttempt);
        score.add("Incorrect Attempts: " + this.numIncorrectTwoAttempts);
        score.add("Score: " + this.score);

        return score;
    }

    public final void appendScoreToFile() throws IOException
    {
        final List<String> score;
        score = this.formatScore();

        FileManager.writeToResource(score);
    }

    public final void printScore()
    {
        this.formatScore().forEach(System.out::println);
    }
}