package ca.bcit.comp2522.gameproject.mastermind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages saving and loading Mastermind game history to a file.
 * Handles file I/O operations for game session records.
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class GameHistoryManager
{
    /**
     * Represents a single game session record.
     */
    static final class GameSessionRecord
    {
        private final LocalDateTime timestamp;
        private final List<String>  roundDetails;

        private final String truthScanInfo;
        private final String outcome;

        /**
         * Constructs a GameSessionRecord with the given timestamp, round
         * details, truth scan info, and outcome.
         *
         * @param timestamp     the timestamp of the game session
         * @param roundDetails  the round details of the game session
         * @param truthScanInfo the truth scan info of the game session
         * @param outcome       the outcome of the game session
         */
        GameSessionRecord(final LocalDateTime timestamp,
                          final List<String> roundDetails,
                          final String truthScanInfo,
                          final String outcome)
        {
            this.timestamp    = timestamp;
            this.roundDetails = new ArrayList<>(roundDetails);
            this.truthScanInfo = truthScanInfo;
            this.outcome       = outcome;
        }

        /**
         * Gets the timestamp of the game session.
         *
         * @return the timestamp of the game session
         */
        LocalDateTime getTimestamp()
        {
            return timestamp;
        }

        /**
         * Gets the round details of the game session.
         *
         * @return the round details of the game session
         */
        List<String> getRoundDetails()
        {
            return new ArrayList<>(roundDetails);
        }

        /**
         * Gets the truth scan info of the game session.
         *
         * @return the truth scan info of the game session
         */
        String getTruthScanInfo()
        {
            return truthScanInfo;
        }

        /**
         * Gets the outcome of the game session.
         *
         * @return the outcome of the game session
         */
        String getOutcome()
        {
            return outcome;
        }

        /**
         * Returns a formatted string representation of the game session record.
         * The string includes the timestamp in ISO date-time format, all round details
         * with proper indentation, truth scan information (if available), and the game outcome.
         * Each section is separated by newlines for better readability.
         *
         * @return a formatted multi-line string representation of the complete game session record
         */
        @Override
        public String toString()
        {
            final StringBuilder sb;
            sb = new StringBuilder();
            sb.append("Timestamp: ")
              .append(timestamp.format(DateTimeFormatter.ISO_DATE_TIME))
              .append("\n");
            sb.append("Rounds:\n");
            roundDetails.forEach(detail -> sb.append("  ")
                                             .append(detail)
                                             .append("\n"));
            if(truthScanInfo != null && !truthScanInfo.isEmpty())
            {
                sb.append("Truth Scan: ")
                  .append(truthScanInfo)
                  .append("\n");
            }
            sb.append("Outcome: ")
              .append(outcome)
              .append("\n");
            return sb.toString();
        }
    }

    private static final String HISTORY_FILE_PATH = "src" +
                                                    java.io.File.separator +
                                                    "res" +
                                                    java.io.File.separator +
                                                    "mastermind_history.txt";
    private static final String GAME_START_MARKER = "=== GAME START ===";
    private static final String GAME_END_MARKER   = "=== GAME END ===";
    private static final String TIMESTAMP_PREFIX  = "Timestamp: ";
    private static final String ROUNDS_HEADER     = "Rounds:";
    private static final String TRUTH_SCAN_PREFIX = "Truth Scan: ";
    private static final String OUTCOME_PREFIX    = "Outcome: ";

    // Use the standard ISO format which handles 'T' separator and fractional seconds
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Represents the transient state while parsing a single game session record
     * from the history file.
     */
    private static final class ParsingState
    {
        private LocalDateTime timestamp;
        private List<String>  roundDetails;
        private String        truthScanInfo;
        private String        outcome;

        /*
         * Initializes a new parsing state with empty values.
         */
        private ParsingState()
        {
            reset();
        }

        /*
         * Resets the state fields to their initial empty values.
         */
        private void reset()
        {
            this.timestamp     = null;
            this.roundDetails  = new ArrayList<>();
            this.truthScanInfo = null;
            this.outcome       = null;
        }

        /*
         * Checks if the current state contains enough valid information to
         * build a GameSessionRecord.
         * 
         * @return true if timestamp, roundDetails, and outcome are present,
         * false otherwise.
         */
        private boolean isValidForRecord()
        {
            final boolean isValid;

            isValid = this.timestamp != null &&
                      !this.roundDetails.isEmpty() &&
                      this.outcome != null;

            return isValid;
        }
    }

    /**
     * Saves a single game session record to the history file.
     * Appends the record to the end of the file.
     * Creates the file and directories if they don't exist.
     *
     * @param record The GameSessionRecord to save.
     */
    void saveGameHistory(final GameSessionRecord record)
    {
        validateRecord(record);
        
        final Path filePath;
        filePath = Paths.get(HISTORY_FILE_PATH);

        try
        {
            // Ensure parent directories exist
            Files.createDirectories(filePath.getParent());

            // Append to the file, creating it if it doesn't exist
            try(final BufferedWriter writer = Files.newBufferedWriter(filePath,
                                                                      StandardCharsets.UTF_8,
                                                                      StandardOpenOption.CREATE,
                                                                      StandardOpenOption.APPEND))
            {
                final String dateTimeString;
                final String roundDetailsString;
                final String truthScanString;
                final String outcomeString;

                dateTimeString = record.getTimestamp()
                                        .format(TIMESTAMP_FORMATTER);
                roundDetailsString = record.getRoundDetails()
                                          .stream()
                                          .map(detail -> "  " + detail)
                                          .collect(Collectors.joining("\n"));               
                truthScanString = record.getTruthScanInfo();
                outcomeString = record.getOutcome();
            
                writer.write(GAME_START_MARKER);
                writer.newLine();

                writer.write(TIMESTAMP_PREFIX + dateTimeString);
                writer.newLine();

                writer.write(ROUNDS_HEADER);
                writer.newLine();
                
                writer.write(roundDetailsString);
                writer.newLine();

                if(truthScanString != null && !truthScanString.isEmpty())
                {
                    writer.write(TRUTH_SCAN_PREFIX + truthScanString);
                    writer.newLine();
                }

                writer.write(OUTCOME_PREFIX + outcomeString);
                writer.newLine();
                writer.write(GAME_END_MARKER);
                writer.newLine();
                // Add an extra blank line between entries
                writer.newLine();
            }
        }
        catch(final IOException e)
        {
            System.err.println("Error saving game history to " +
                               HISTORY_FILE_PATH +
                               ": " +
                               e.getMessage());
        }
    }

    /**
     * Loads all game session records from the history file.
     * <p>
     * Parses the history file line by line, identifying game session boundaries
     * and extracting relevant data using helper methods.
     * Handles file not found errors and parsing issues gracefully.
     * </p>
     *
     * @return A List of GameSessionRecord objects, or an empty list if the file
     *         doesn't exist or an error occurs during reading/parsing.
     */
    final List<GameSessionRecord> loadGameHistory()
    {
        final Path                    filePath;
        final List<GameSessionRecord> gameHistory;
        final ParsingState            parseState;
        boolean                       inGameRecord;

        filePath    = Paths.get(HISTORY_FILE_PATH);
        gameHistory = new ArrayList<>();

        if(!Files.exists(filePath))
        {
            System.out.println("History file not found (" +
                               HISTORY_FILE_PATH +
                               "). No history to display.");
            return gameHistory; // Return empty list if file doesn't exist
        }

        parseState   = new ParsingState();
        inGameRecord = false;

        try(final BufferedReader reader = Files.newBufferedReader(filePath,
                                                                  StandardCharsets.UTF_8))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                final String trimmedLine;
                trimmedLine = line.trim();

                if(trimmedLine.equals(GAME_START_MARKER))
                {
                    parseState.reset();
                    inGameRecord = true;
                }
                else if(trimmedLine.equals(GAME_END_MARKER))
                {
                    if(inGameRecord)
                    {
                        buildAndAddRecord(parseState,
                                          gameHistory);
                        inGameRecord = false;
                    }
                }
                else if(inGameRecord)
                {
                    processRecordLine(trimmedLine,
                                      parseState);
                }
            }

            if(inGameRecord)
            {
                System.err.println("Warning: History file ended unexpectedly within a game record.");
            }
        }
        catch(final IOException error)
        {
            System.err.println("Error loading game history from " +
                               HISTORY_FILE_PATH +
                               ": " +
                               error.getMessage());
        }
        return gameHistory;
    }

    /*
     * Processes a single line belonging to the current game record being
     * parsed.
     * Identifies the line type (timestamp, round, truth scan, outcome) and
     * updates the parsing state.
     *
     * @param line The trimmed line from the history file.
     * 
     * @param state The current parsing state object to update.
     */
    private void processRecordLine(final String line,
                                   final ParsingState state)
    {
        if(line.startsWith(TIMESTAMP_PREFIX))
        {
            try
            {
                state.timestamp = LocalDateTime.parse(line.substring(TIMESTAMP_PREFIX.length()),
                                                      TIMESTAMP_FORMATTER);
            }
            catch(final Exception e) 
            {
                System.err.println("Warning: Could not parse timestamp: " +
                                   line +
                                   " (" +
                                   e.getMessage() +
                                   ")");
                state.timestamp = null;
            }
        }
        else if(line.matches("^Round \\d+:.*$"))
        {
            state.roundDetails.add(line);
        }
        else if(line.startsWith(TRUTH_SCAN_PREFIX))
        {
            state.truthScanInfo = line.substring(TRUTH_SCAN_PREFIX.length());
        }
        else if(line.startsWith(OUTCOME_PREFIX))
        {
            final String parsedOutcome;
            parsedOutcome = line.substring(OUTCOME_PREFIX.length());
            
            if("Won".equalsIgnoreCase(parsedOutcome) ||
               "Lost".equalsIgnoreCase(parsedOutcome))
            {
                state.outcome = parsedOutcome;
            }
            else
            {
                System.err.println("Warning: Invalid outcome found: " +
                                   parsedOutcome);
                state.outcome = null;
            }
        }
    }

    /*
     * Attempts to build a GameSessionRecord from the current parsing state and
     * add it to the history list.
     * Validates the state before creating the record.
     *
     * @param state The current parsing state containing data for one record.
     * 
     * @param gameHistory The list to add the created record to.
     */
    private void buildAndAddRecord(final ParsingState state,
                                   final List<GameSessionRecord> gameHistory)
    {
        if(state.isValidForRecord())
        {
            final GameSessionRecord record;
            final List<String> roundDetails;

            roundDetails = new ArrayList<>(state.roundDetails);
            record = new GameSessionRecord(state.timestamp,
                                          roundDetails,
                                          state.truthScanInfo,
                                          state.outcome);
            gameHistory.add(record);
        }
        else
        {
            System.err.println("Warning: Incomplete game record found in history file. Skipping.");
        }
    }

    /**
     * Filters a list of game session records based on the desired outcome.
     *
     * @param history       The list of GameSessionRecord to filter.
     * @param outcomeFilter The outcome to filter by ("Won" or "Lost").
     *                      Case-insensitive.
     * @return A new list containing only the records matching the filter.
     */
    List<GameSessionRecord> filterHistoryByOutcome(final List<GameSessionRecord> history,
                                                   final String outcomeFilter)
    {
        final List<GameSessionRecord> filteredHistory;

        filteredHistory = new ArrayList<>();
        
        for(final GameSessionRecord record : history)
        {
            if(record.getOutcome()
                     .equalsIgnoreCase(outcomeFilter))
            {
                filteredHistory.add(record);
            }
        }
        return filteredHistory;
    }

    /**
     * Validates the game session record.
     * 
     * @param record The game session record to validate.
     */
    private static void validateRecord(final GameSessionRecord record)
    {
        if(record == null)
        {
            throw new IllegalArgumentException("Record cannot be null");
        }
        if(record.getTimestamp() == null)
        {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if(record.getRoundDetails() == null || record.getRoundDetails().isEmpty())
        {
            throw new IllegalArgumentException("Round details cannot be null or empty");
        }
        if(record.getTruthScanInfo() == null)
        {
            throw new IllegalArgumentException("Truth scan info cannot be null");
        }
        if(record.getOutcome() == null || record.getOutcome().isEmpty())
        {
            throw new IllegalArgumentException("Outcome cannot be null or empty");
        }
    }
}