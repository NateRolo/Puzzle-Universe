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

/**
 * Manages saving and loading Mastermind game history to a file.
 * Handles file I/O operations for game session records.
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class GameHistoryManager
{
    static final class GameSessionRecord
    {
        private final LocalDateTime timestamp;
        private final List<String>  roundDetails;

        private final String truthScanInfo;

        private final String outcome;

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


        LocalDateTime getTimestamp()
        {
            return timestamp;
        }

        List<String> getRoundDetails()
        {
            return new ArrayList<>(roundDetails);
        }

        String getTruthScanInfo()
        {
            return truthScanInfo;
        }

        String getOutcome()
        {
            return outcome;
        }

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

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Saves a single game session record to the history file.
     * Appends the record to the end of the file.
     * Creates the file and directories if they don't exist.
     *
     * @param record The GameSessionRecord to save.
     */
    public void saveGameHistory(final GameSessionRecord record)
    {
        final Path filePath;
        filePath = Paths.get(HISTORY_FILE_PATH);

        try
        {
            // Ensure parent directories exist
            Files.createDirectories(filePath.getParent());

            // Append to the file, creating it if it doesn't exist
            try(BufferedWriter writer = Files.newBufferedWriter(filePath,
                                                                StandardCharsets.UTF_8,
                                                                StandardOpenOption.CREATE,
                                                                StandardOpenOption.APPEND))
            {
                writer.write(GAME_START_MARKER);
                writer.newLine();
                writer.write(TIMESTAMP_PREFIX +
                             record.getTimestamp()
                                   .format(TIMESTAMP_FORMATTER));
                writer.newLine();
                writer.write(ROUNDS_HEADER);
                writer.newLine();
                for(final String roundDetail : record.getRoundDetails())
                {
                    // Assuming roundDetail is already formatted like "Round X:
                    // Guess: ... | Feedback: ..."
                    writer.write("  " + roundDetail); // Indent for readability
                    writer.newLine();
                }
                if(record.getTruthScanInfo() != null &&
                   !record.getTruthScanInfo()
                          .isEmpty())
                {
                    writer.write(TRUTH_SCAN_PREFIX + record.getTruthScanInfo());
                    writer.newLine();
                }
                writer.write(OUTCOME_PREFIX + record.getOutcome());
                writer.newLine();
                writer.write(GAME_END_MARKER);
                writer.newLine();
                writer.newLine(); // Add an extra blank line between entries
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
     *
     * @return A List of GameSessionRecord objects, or an empty list if the file
     *         doesn't exist or an error occurs.
     */
    final List<GameSessionRecord> loadGameHistory()
    {
        final Path                    filePath;
        final List<GameSessionRecord> gameHistory;

        filePath    = Paths.get(HISTORY_FILE_PATH);
        gameHistory = new ArrayList<>();

        if(!Files.exists(filePath))
        {
            System.out.println("History file not found (" +
                               HISTORY_FILE_PATH +
                               "). No history to display.");
            return gameHistory; // Return empty list if file doesn't exist
        }

        // refactor, overly complicated if-else
        try(BufferedReader reader = Files.newBufferedReader(filePath,
                                                            StandardCharsets.UTF_8))
        {
            String        line;
            LocalDateTime currentTimestamp;
            List<String>  currentRoundDetails;
            String        currentTruthScanInfo;
            String        currentOutcome;
            boolean       inGameRecord;

            currentTimestamp     = null;
            currentRoundDetails  = new ArrayList<>();
            currentTruthScanInfo = null;
            currentOutcome       = null;
            inGameRecord         = false;

            while((line = reader.readLine()) != null)
            {
                line = line.trim(); // Trim leading/trailing whitespace

                if(line.equals(GAME_START_MARKER))
                {
                    inGameRecord = true;
                    // Reset for new record
                    currentTimestamp = null;
                    currentRoundDetails.clear();
                    currentTruthScanInfo = null;
                    currentOutcome       = null;
                }
                else if(line.equals(GAME_END_MARKER) && inGameRecord)
                {
                    if(currentTimestamp != null &&
                       !currentRoundDetails.isEmpty() &&
                       currentOutcome != null)
                    {
                        final GameSessionRecord record;
                        record = new GameSessionRecord(currentTimestamp,
                                                       currentRoundDetails,
                                                       currentTruthScanInfo,
                                                       currentOutcome);
                        gameHistory.add(record);
                    }
                    else
                    {
                        System.err.println("Warning: Incomplete game record found in history file.");
                    }
                    inGameRecord = false; // Reset flag
                }
                else if(inGameRecord)
                {
                    if(line.startsWith(TIMESTAMP_PREFIX))
                    {
                        try
                        {
                            currentTimestamp = LocalDateTime.parse(line.substring(TIMESTAMP_PREFIX.length()),
                                                                   TIMESTAMP_FORMATTER);
                        }
                        catch(final Exception e)
                        {
                            System.err.println("Warning: Could not parse timestamp: " +
                                               line);
                            currentTimestamp = null; // Invalidate record if
                                                     // timestamp is bad

                        }
                    }
                    else if(line.startsWith("Round ")) // Simple check for round
                                                       // lines

                    {
                        currentRoundDetails.add(line);
                    }
                    else if(line.startsWith(TRUTH_SCAN_PREFIX))
                    {
                        currentTruthScanInfo = line.substring(TRUTH_SCAN_PREFIX.length());
                    }
                    else if(line.startsWith(OUTCOME_PREFIX))
                    {
                        currentOutcome = line.substring(OUTCOME_PREFIX.length());
                        // Simple validation for outcome
                        if(!"Won".equalsIgnoreCase(currentOutcome) &&
                           !"Lost".equalsIgnoreCase(currentOutcome))
                        {
                            System.err.println("Warning: Invalid outcome found: " +
                                               currentOutcome);
                            currentOutcome = null; // Invalidate record if
                                                   // outcome is bad
                        }
                    }
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


    /**
     * Filters a list of game session records based on the desired outcome.
     *
     * @param history       The list of GameSessionRecord to filter.
     * @param outcomeFilter The outcome to filter by ("Won" or "Lost").
     *                      Case-insensitive.
     * @return A new list containing only the records matching the filter.
     */
    final List<GameSessionRecord> filterHistoryByOutcome(final List<GameSessionRecord> history,
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
}