package ca.bcit.comp2522.gameproject.wordgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
final class Score
{
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final int    DEFAULT_GAMES_PLAYED           = 0;
    private static final int    DEFAULT_CORRECT_FIRST_GUESSES  = 0;
    private static final int    DEFAULT_CORRECT_SECOND_GUESSES = 0;
    private static final int    DEFAULT_INCORRECT_TWO_TIMES    = 0;
    private static final int    DEFAULT_SCORE                  = 0;
    private static final double DEFAULT_AVERAGE                = 0.0;

    private static final int GAMES_PLAYED_MIN = 0;
    private static final int GUESS_COUNT_MIN  = 0;

    private static final int CORRECT_FIRST_GUESS_SCORE  = 2;
    private static final int CORRECT_SECOND_GUESS_SCORE = 1;

    // Constants for parsing score files
    private static final String DATE_TIME_PREFIX       = "Date and Time:";
    private static final int    LINE_PARSE_SPLIT_LIMIT = 2;
    private static final int    PARSED_VALUE_INDEX     = 1;
    private static final int    START_INDEX            = 0;
    private static final int    GAMES_PLAYED_OFFSET    = 1;
    private static final int    CORRECT_FIRST_OFFSET   = 2;
    private static final int    CORRECT_SECOND_OFFSET  = 3;
    private static final int    INCORRECT_OFFSET       = 4;

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;
    private int score;

    final String formattedDateTime;

    /**
     * Constructs a Score object with specific values.
     *
     * @param dateTime              the date and time when the score was
     *                              recorded
     * @param gamesPlayed           the number of games played
     * @param numCorrectFirstGuess  the number of correct answers on first
     *                              attempt
     * @param numCorrectSecondGuess the number of correct answers on second
     *                              attempt
     * @param twoIncorrectAttempts  the number of questions with two incorrect
     *                              attempts
     */
    Score(final LocalDateTime dateTime,
          final int gamesPlayed,
          final int numCorrectFirstGuess,
          final int numCorrectSecondGuess,
          final int twoIncorrectAttempts)
    {
        validateDateTime(dateTime);
        validateGamesPlayed(gamesPlayed);
        validateGuessCount(numCorrectFirstGuess,
                           "First guess count");
        validateGuessCount(numCorrectSecondGuess,
                           "Second guess count");
        validateGuessCount(twoIncorrectAttempts,
                           "Incorrect attempts count");

        formattedDateTime       = dateTime.format(formatter);
        this.numGamesPlayed     = gamesPlayed;
        numCorrectFirstAttempt  = numCorrectFirstGuess;
        numCorrectSecondAttempt = numCorrectSecondGuess;
        numIncorrectTwoAttempts = twoIncorrectAttempts;

        this.score = (numCorrectFirstGuess * CORRECT_FIRST_GUESS_SCORE) +
                     (numCorrectSecondGuess * CORRECT_SECOND_GUESS_SCORE);
    }

    /**
     * Constructs a new Score object with default values.
     */
    Score()
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
    String getCurrentTime()
    {
        return formattedDateTime;
    }

    /**
     * Gets the number of games played.
     *
     * @return number of games played
     */
    int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Gets the number of correct answers on first attempt.
     *
     * @return number of correct first attempts
     */
    int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    /**
     * Gets the number of correct answers on second attempt.
     *
     * @return number of correct second attempts
     */
    int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    /**
     * Gets the number of incorrect answers after two attempts.
     *
     * @return number of incorrect attempts after two tries
     */
    int getNumIncorrectTwoAttempts()
    {
        return numIncorrectTwoAttempts;
    }

    /**
     * Gets the total score.
     *
     * @return the current score
     */
    int getScore()
    {
        return score;
    }

    /**
     * Increments the number of games played.
     */
    void incrementNumGamesPlayed()
    {
        this.numGamesPlayed++;
    }

    /**
     * Increments the number of correct first attempt and adds
     * 2 points to score.
     */
    void incrementNumCorrectFirstAttempt()
    {
        this.numCorrectFirstAttempt++;
        this.score += CORRECT_FIRST_GUESS_SCORE;
    }

    /**
     * Increments the number of correct second attempts and adds
     * 1 point to score.
     */
    void incrementNumCorrectSecondAttempt()
    {
        this.numCorrectSecondAttempt++;
        this.score += CORRECT_SECOND_GUESS_SCORE;
    }

    /**
     * Increments the number of incorrect attempts after two tries.
     */
    void incrementNumIncorrectTwoAttempts()
    {
        this.numIncorrectTwoAttempts++;
    }

    /**
     * Formats a Score object into a list of strings.
     *
     * @param score the Score object to format
     * @return a List of strings representing the formatted score
     */
    private static List<String> formatScore(Score score)
    {
        validateScore(score);

        final List<String> scoreAsList;
        scoreAsList = new ArrayList<>();

        scoreAsList.add("Date and Time: " + score.formattedDateTime);
        scoreAsList.add("Games Played: " + score.numGamesPlayed);
        scoreAsList.add("Correct First Attempts: " +
                        score.numCorrectFirstAttempt);
        scoreAsList.add("Correct Second Attempts: " +
                        score.numCorrectSecondAttempt);
        scoreAsList.add("Incorrect Attempts: " + score.numIncorrectTwoAttempts);
        scoreAsList.add("Score: " + score.score + " points");

        return scoreAsList;
    }

    /**
     * Appends a Score object to a file.
     *
     * @param score the Score object to append
     * @param file  the path to the file
     * @throws IOException if there is an error writing to the file
     */
    static void appendScoreToFile(final Score score,
                                  final String file) throws IOException
    {
        validateScore(score);
        validateFilePath(file);

        final List<String> scoreAsList;
        scoreAsList = formatScore(score);

        FileManager.writeToResource(scoreAsList,
                                    file);
    }

    /**
     * Prints the score information to the console.
     */
    void printScore()
    {
        formatScore(this).forEach(System.out::println);
    }

    /**
     * Reads scores from a file and returns them as a list of Score objects.
     *
     * @param filePath the path to the file containing scores
     * @return a List of Score objects read from the file
     * @throws IOException if there is an error reading from the file
     */
    static List<Score> readScoresFromFile(final String filePath) throws IOException
    {
        validateFilePath(filePath);

        final List<String> scoresLines;
        final List<Score>  scores;

        scoresLines = FileManager.readLinesFromResource(filePath);

        validateList(scoresLines);

        scores = IntStream.range(START_INDEX,
                                 scoresLines.size())
                          .filter(startIndex -> scoresLines.get(startIndex)
                                                           .startsWith(DATE_TIME_PREFIX))
                          .mapToObj(startIndex -> new Score(LocalDateTime.parse(scoresLines.get(startIndex)
                                                                                           .split(": ",
                                                                                                  LINE_PARSE_SPLIT_LIMIT)[PARSED_VALUE_INDEX],
                                                                                formatter),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             GAMES_PLAYED_OFFSET)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[PARSED_VALUE_INDEX]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             CORRECT_FIRST_OFFSET)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[PARSED_VALUE_INDEX]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             CORRECT_SECOND_OFFSET)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[PARSED_VALUE_INDEX]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             INCORRECT_OFFSET)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[PARSED_VALUE_INDEX])))
                          .toList();
        return scores;
    }

    /**
     * Displays a message comparing the current score with the highest score.
     *
     * @param currentScore the Score object to compare
     * @throws IOException if there is an error reading from the score file
     */
    static void displayHighScoreMessage(final Score currentScore) throws IOException
    {
        validateScore(currentScore);

        final List<Score> allScores;
        final double      currentAverage;

        Score  highestScore;
        double highestAverage;

        allScores      = readScoresFromFile("score.txt");
        currentAverage = calculateAverageScore(currentScore);
        highestScore   = null;
        highestAverage = DEFAULT_AVERAGE;

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

    /*
     * Calculates the average score per game.
     *
     * @param score the Score object to calculate the average for
     * 
     * @return the average score per game
     */
    private static double calculateAverageScore(final Score score)
    {
        validateScore(score);

        if(score.numGamesPlayed == DEFAULT_GAMES_PLAYED)
        {
            return DEFAULT_SCORE;
        }
        return (double)score.score / score.numGamesPlayed;
    }

    /**
     * Returns a string representation of this Score object.
     *
     * @return a string containing all score information
     */
    @Override
    public String toString()
    {
        final StringBuilder builder;
        final List<String>  scoreAsList;
        final String        scoreAsString;

        scoreAsList = formatScore(this);

        builder = new StringBuilder();
        for(final String scoreLine : scoreAsList)
        {
            builder.append(scoreLine)
                   .append("\n");
        }

        scoreAsString = builder.toString();

        return scoreAsString;
    }

    /*
     * Validates that the date time is not null and not in the future.
     *
     * @param dateTime the LocalDateTime to validate
     */
    private static void validateDateTime(final LocalDateTime dateTime)
    {
        if(dateTime == null)
        {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        if(dateTime.isAfter(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("DateTime cannot be in the future");
        }
    }

    /*
     * Validates that the number of games played is not negative.
     *
     * @param gamesPlayed the number of games played to validate
     */
    private static void validateGamesPlayed(final int gamesPlayed)
    {
        if(gamesPlayed < GAMES_PLAYED_MIN)
        {
            throw new IllegalArgumentException("Games played cannot be negative");
        }
    }

    /*
     * Validates that a guess count is not negative.
     *
     * @param count the count to validate
     * 
     * @param guessType the type of guess for error messaging
     */
    private static void validateGuessCount(final int count,
                                           final String guessType)
    {
        if(count < GUESS_COUNT_MIN)
        {
            throw new IllegalArgumentException(guessType +
                                               " cannot be negative");
        }
    }

    /*
     * Validates that a Score object is not null.
     *
     * @param score the Score object to validate
     */
    private static void validateScore(final Score score)
    {
        if(score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }
    }

    /*
     * Validates that a file path is not null, empty, and exists.
     *
     * @param filePath the file path to validate
     */
    private static void validateFilePath(final String filePath)
    {
        if(filePath == null || filePath.isEmpty())
        {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if(Files.notExists(Paths.get(filePath)))
        {
            throw new IllegalArgumentException("File path does not exist");
        }
    }

    /**
     * Validates that a list is not null or empty.
     *
     * @param list the list to validate
     */
    private static void validateList(final List<String> list)
    {
        if(list == null || list.isEmpty())
        {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
    }

}