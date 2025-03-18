package ca.bcit.comp2522.gameproject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final int DEFAULT_GAMES_PLAYED           = 0;
    private static final int DEFAULT_CORRECT_FIRST_GUESSES  = 0;
    private static final int DEFAULT_CORRECT_SECOND_GUESSES = 0;
    private static final int DEFAULT_INCORRECT_TWO_TIMES    = 0;

    private static final int CORRECT_FIRST_GUESS_SCORE  = 2;
    private static final int CORRECT_SECOND_GUESS_SCORE = 1;

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;
    private int score;

    final String formattedDateTime;

    public Score(final LocalDateTime dateTime,
                 final int gamesPlayed,
                 final int numCorrectFirstGuess,
                 final int numCorrectSecondGuess,
                 final int twoIncorrectAttempts)
    {
        formattedDateTime       = dateTime.format(formatter);
        this.numGamesPlayed     = gamesPlayed;
        numCorrectFirstAttempt  = numCorrectFirstGuess;
        numCorrectSecondAttempt = numCorrectSecondGuess;
        numIncorrectTwoAttempts = twoIncorrectAttempts;

        this.score = (numCorrectFirstGuess * CORRECT_FIRST_GUESS_SCORE) +
                     (numCorrectSecondGuess * CORRECT_SECOND_GUESS_SCORE);
    }

    /**
     * Constructs a new Score object.
     */
    public Score()
    {
        this(LocalDateTime.now(),
             DEFAULT_GAMES_PLAYED,
             DEFAULT_CORRECT_FIRST_GUESSES,
             DEFAULT_CORRECT_SECOND_GUESSES,
             DEFAULT_INCORRECT_TWO_TIMES);
    }

    /**
     * Gets the date and time when the game was played.
     *
     * @return the date and time played
     */
    public final String getCurrentTime()
    {
        return formattedDateTime;
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

    public final int getScore()
    {
        return score;
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


    private static List<String> formatScore(Score score)
    {
        final List<String> scoreAsList;
        scoreAsList = new ArrayList<>();

        scoreAsList.add("Date and Time: " + score.formattedDateTime);
        scoreAsList.add("Games Played: " + score.numGamesPlayed);
        scoreAsList.add("Correct First Attempts: " + score.numCorrectFirstAttempt);
        scoreAsList.add("Correct Second Attempts: " + score.numCorrectSecondAttempt);
        scoreAsList.add("Incorrect Attempts: " + score.numIncorrectTwoAttempts);
        scoreAsList.add("Score: " + score.score + " points");

        return scoreAsList;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder;
        final List<String>  list = formatScore(this);

        builder = new StringBuilder();
        for(String str : list)
        {
            builder.append(str)
                   .append("\n");
        }

        return builder.toString();
    }

    public static void appendScoreToFile(Score score,
                                         String file) throws IOException
    {
        final List<String> scoreAsList;
        scoreAsList = formatScore(score);

        FileManager.writeToResource(scoreAsList,
                                    file);
    }

    public final void printScore()
    {
        formatScore(this).forEach(System.out::println);
    }

    public static List<Score> readScoresFromFile(final String filePath) throws IOException
    {
        final List<String> scoresLines;
        final List<Score>  scores;
        scoresLines = FileManager.readLinesFromResource(filePath);

        if(scoresLines.isEmpty())
        {
            scores = new ArrayList<Score>();
            return scores;
        }

        scores = IntStream.range(0,
                                 scoresLines.size())
                          .filter(i -> scoresLines.get(i)
                                                  .startsWith("Date and Time:"))
                          .mapToObj(i -> new Score(LocalDateTime.parse(scoresLines.get(i)
                                                                                  .split(": ",
                                                                                         2)[1],
                                                                       formatter),
                                                   Integer.parseInt(scoresLines.get(i + 1)
                                                                               .split(": ",
                                                                                      2)[1]),
                                                   Integer.parseInt(scoresLines.get(i + 2)
                                                                               .split(": ",
                                                                                      2)[1]),
                                                   Integer.parseInt(scoresLines.get(i + 3)
                                                                               .split(": ",
                                                                                      2)[1]),
                                                   Integer.parseInt(scoresLines.get(i + 4)
                                                                               .split(": ",
                                                                                      2)[1])))
                          .toList();
        return scores;
    }

    public static void displayHighScoreMessage(Score currentScore) throws IOException
    {
        final List<Score> allScores;
        final double      currentAverage;

        Score  highestScore;
        double highestAverage;

        allScores      = readScoresFromFile("score.txt");
        currentAverage = calculateAverageScore(currentScore);
        highestScore   = null;
        highestAverage = 0.0;

        for(Score score : allScores)
        {
            final double average = calculateAverageScore(score);
            if(average > highestAverage)
            {
                highestAverage = average;
                highestScore   = score;
            }
        }

        if(highestScore == null || currentAverage > highestAverage)
        {
            System.out.printf("CONGRATULATIONS! You are the new high score with an average of %.2f points per game",
                              currentAverage);
            if(highestScore != null)
            {
                System.out.printf("; the previous record was %.2f points per game on %s%n",
                                  highestAverage,
                                  highestScore.formattedDateTime);
            }
            else
            {
                System.out.println("!");
            }
        }
        else
        {
            System.out.printf("You did not beat the high score of %.2f points per game from %s%n",
                              highestAverage,
                              highestScore.formattedDateTime);
        }
    }

    private static double calculateAverageScore(Score score)
    {
        if(score.numGamesPlayed == 0)
        {
            return 0.0;
        }
        return (double)score.score / score.numGamesPlayed;
    }
}