package ca.bcit.comp2522.gameproject.wordgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static final int SCORE_CORRECT_FIRST_GUESS  = 2;
    private static final int SCORE_CORRECT_SECOND_GUESS = 1;

    // Constants for parsing score files
    private static final String DATE_TIME_PREFIX       = "Date and Time:";
    private static final int    LINE_PARSE_SPLIT_LIMIT = 2;
    private static final int    INDEX_PARSED_VALUE     = 1;
    private static final int    INDEX_START            = 0;
    private static final int    OFFSET_GAMES_PLAYED    = 1;
    private static final int    OFFSET_CORRECT_FIRST   = 2;
    private static final int    OFFSET_CORRECT_SECOND  = 3;
    private static final int    OFFSET_INCORRECT       = 4;

    private static final String DIR_SRC = "src";
    private static final String DIR_RES = "res";

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;
    private int totalPoints;

    final String formattedDateTime;

    /**
     * Constructs a Score object with specific values for game performance
     * tracking.
     * <p>
     * This constructor initializes a Score object with detailed statistics
     * about a player's
     * performance in the word game. It calculates the
     * total score based on the weighted values of correct guesses.
     * </p>
     * <p>
     * The score is calculated as follows:
     * - Each correct first attempt is worth {@value #SCORE_CORRECT_FIRST_GUESS}
     * points
     * - Each correct second attempt is worth
     * {@value #SCORE_CORRECT_SECOND_GUESS} point
     * - Incorrect attempts do not contribute to the score but are tracked for
     * statistics
     * </p>
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

        this.totalPoints = (numCorrectFirstGuess * SCORE_CORRECT_FIRST_GUESS) +
                           (numCorrectSecondGuess * SCORE_CORRECT_SECOND_GUESS);
    }

    /**
     * Constructs a new Score object to track performance for a new game
     * session.
     * <p>
     * This constructor delegates to the main constructor, providing default
     * values
     * and the current {@link LocalDateTime}.
     * </p>
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
        return totalPoints;
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
        this.totalPoints += SCORE_CORRECT_FIRST_GUESS;
    }

    /**
     * Increments the number of correct second attempts and adds
     * 1 point to score.
     */
    void incrementNumCorrectSecondAttempt()
    {
        this.numCorrectSecondAttempt++;
        this.totalPoints += SCORE_CORRECT_SECOND_GUESS;
    }

    /**
     * Increments the number of incorrect attempts after two tries.
     */
    void incrementNumIncorrectTwoAttempts()
    {
        this.numIncorrectTwoAttempts++;
    }


    /**
     * Appends a Score object to a file in the resources directory.
     * <p>
     * This method takes a Score object and converts it to a formatted list of
     * strings
     * using the formatScore method, then writes these strings to the specified
     * file.
     * The file is located in the "src/res" directory. If the file doesn't
     * exist,
     * it will be created; if it exists, the score will be appended to it.
     * </p>
     *
     * @param score the Score object to append, must not be null
     * @param fileString  the name of the file within the resources directory
     * @throws IOException if there is an error writing to the file
     */
    static void appendScoreToFile(final Score score,
                                  final String fileString) throws IOException
    {
        validateScore(score);
        validateFileString(fileString);

        final Path         filePath;
        final String       fullFilePathString;
        final List<String> scoreAsList;

        filePath       = Paths.get(DIR_SRC,
                                   DIR_RES,
                                   fileString);
        fullFilePathString = filePath.toString();

        scoreAsList = formatScore(score);

        FileManager.writeToResource(scoreAsList,
                                    fullFilePathString);
    }

    /**
     * Prints the score information to the console.
     */
    void printScore()
    {
        final List<String> scoreList;
        scoreList = formatScore(this);

        scoreList.forEach(System.out::println);
    }

    /**
     * Reads scores from a file and returns them as a list of Score objects.
     * <p>
     * This method parses the file content line by line, looking for score
     * entries
     * that start with the date/time prefix. For each score entry found, it
     * extracts
     * the date/time, games played, correct first attempts, correct second
     * attempts,
     * and incorrect attempts, then creates a new Score object with these
     * values.
     * </p>
     *
     * @param fileString the path to the file containing scores to be read
     * @return a List of Score objects constructed from the file data
     * @throws IOException if there is an error reading from the file or if the
     *                     file format is invalid.
     */
    static List<Score> readScoresFromFile(final String fileString) throws IOException
    {
        validateFileString(fileString);

        final Path   filePath;
        final String fullFilePathString;
        final List<String> scoresLines;
        final List<Score>  scores;

        filePath       = Paths.get(DIR_SRC,
                                   DIR_RES,
                                   fileString);
        fullFilePathString = filePath.toString();

        scoresLines = FileManager.readLinesFromResource(fullFilePathString);

        validateList(scoresLines);

        // parse file and create score object from stream
        scores = IntStream.range(INDEX_START,
                                 scoresLines.size())
                          .filter(startIndex -> scoresLines.get(startIndex)
                                                           .startsWith(DATE_TIME_PREFIX))
                          .mapToObj(startIndex -> new Score(LocalDateTime.parse(scoresLines.get(startIndex)
                                                                                           .split(": ",
                                                                                                  LINE_PARSE_SPLIT_LIMIT)[INDEX_PARSED_VALUE],
                                                                                formatter),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             OFFSET_GAMES_PLAYED)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[INDEX_PARSED_VALUE]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             OFFSET_CORRECT_FIRST)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[INDEX_PARSED_VALUE]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             OFFSET_CORRECT_SECOND)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[INDEX_PARSED_VALUE]),
                                                            Integer.parseInt(scoresLines.get(startIndex +
                                                                                             OFFSET_INCORRECT)
                                                                                        .split(": ",
                                                                                               LINE_PARSE_SPLIT_LIMIT)[INDEX_PARSED_VALUE])))
                          .toList();
        return scores;
    }

    /**
     * Displays a message comparing the current score with the highest score
     * from previous games.
     * <p>
     * This method reads all scores from the score file, calculates the average
     * score per game
     * for each record, and determines if the current score is a new high score.
     * It then displays
     * an appropriate congratulatory message if the player achieved a new high
     * score, or informs
     * them of the existing high score if they did not.
     * </p>
     *
     * @param currentScore the Score object to compare against historical high
     *                     scores
     * @throws IOException if there is an error reading from the score file or
     *                     if the file cannot be accessed
     */
    static void displayHighScoreMessage(final Score currentScore,
                                        final String fileString) throws IOException
    {
        validateScore(currentScore);
        validateFileString(fileString);

        final List<Score> allScores;
        final double      currentAverage;

        Score  highestScore;
        double highestAverage;

        allScores      = readScoresFromFile(fileString);
        currentAverage = calculateAverageScore(currentScore);
        highestScore   = null;
        highestAverage = DEFAULT_AVERAGE;

        for(final Score score : allScores)
        {
            final double average;
            average = calculateAverageScore(score);

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
        else if(currentAverage == highestAverage)
        {
            System.out.printf("You MATCHED the current high score of %.2f points per game from %s%n",
                              currentAverage,
                              highestScore.formattedDateTime);
        }
        else
        {
            System.out.printf("You did not beat the high score of %.2f points per game from %s%n",
                              highestAverage,
                              highestScore.formattedDateTime);
        }
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
     * Formats a Score object into a list of strings for display or storage.
     * <p>
     * This method converts all the score statistics (date/time, games played,
     * correct attempts, incorrect attempts, and total score) into a formatted
     * list of strings. Each string represents one line of score information
     * with appropriate labels.
     * </p>
     *
     * @param score the Score object to format, must not be null
     * 
     * @return a List of strings representing the formatted score information,
     * with each element containing one aspect of the score data
     */
    private static List<String> formatScore(final Score score)
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
        scoreAsList.add("Score: " + score.totalPoints + " points");

        return scoreAsList;
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
        return (double)score.totalPoints / score.numGamesPlayed;
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
    private static void validateFileString(final String fileString)
    {
        if(fileString == null || fileString.isEmpty())
        {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if(Files.notExists(Paths.get(DIR_SRC, DIR_RES, fileString)))
        {
            throw new IllegalArgumentException("File path does not exist in res: " +
                                               fileString);
        }
    }

    /*
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