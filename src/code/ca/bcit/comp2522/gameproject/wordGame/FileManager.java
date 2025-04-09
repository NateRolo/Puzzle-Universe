package ca.bcit.comp2522.gameproject.wordgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing text files from resources.
 * <p>
 * This class provides static methods to read lines from resource files
 * and write formatted scores to files. It handles file validation and
 * resource management.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class FileManager
{
    /**
     * Reads lines from a resource file.
     * <p>
     * First attempts to read from the file system. If the file doesn't exist
     * in the file system, attempts to read it from the classpath resources.
     * This dual approach ensures flexibility in accessing files both during
     * development and after deployment.
     * </p>
     *
     * @param pathString path to the resource file (can be filesystem path or classpath resource)
     * @return List of strings, each representing a line from the file with whitespace trimmed
     * @throws IOException if file cannot be read or if the path is invalid
     */
    static List<String> readLinesFromResource(final String pathString) throws IOException
    {
        validatePathString(pathString);

        final List<String>      lines;
        final InputStream       inputStream;
        final InputStreamReader inputStreamReader;
        final BufferedReader    bufferedReader;
        final Path              path;

        // Convert the string path to a Path object for file operations
        path = Paths.get(pathString);

        // First attempt: Try to read from the file system
        if(Files.exists(path))
        {
            final List<String> allLines;
            allLines = Files.readAllLines(path);
            // Exit from method if file exists in file system
            return allLines;
        }

        // Second attempt: File doesn't exist in file system, try classpath resources
        lines       = new ArrayList<>();
        inputStream = FileManager.class.getResourceAsStream(pathString);
        validateInputStream(inputStream);

        inputStreamReader = new InputStreamReader(inputStream,
                                                  StandardCharsets.UTF_8);
        bufferedReader    = new BufferedReader(inputStreamReader);

        try(inputStream; bufferedReader)
        {
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                lines.add(line.trim());
            }
        }
        catch(final IOException error)
        {
            error.printStackTrace();
            throw error;
        }

        return lines;
    }

    /**
     * Writes formatted score data to a file.
     * <p>
     * Creates the file if it doesn't exist or appends to it if it does.
     * </p>
     *
     * @param formattedScore list of strings to write to the file
     * @param pathString       path to the file where data should be written
     * @throws FileNotFoundException if the file path is invalid
     */
    static void writeToResource(final List<String> formattedScore,
                                final String pathString) throws FileNotFoundException
    {
        validateFormattedScore(formattedScore);
        validatePathString(pathString);

        try
        {
            final Path scorePath;
            scorePath = Paths.get(pathString);

            Files.write(scorePath,
                        formattedScore,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
        }
        catch(final IOException error)
        {
            error.printStackTrace();
        }
    }

    /*
     * Validates that the formatted score list is not null or empty.
     *
     * @param formattedScore the list of score strings to validate
     */
    private static void validateFormattedScore(final List<String> formattedScore)
    {
        if(formattedScore == null || formattedScore.isEmpty())
        {
            throw new IllegalArgumentException("Cannot write null or empty scores to file.");
        }
    }

    /*
     * Validates that the file path is not null, blank, or non-existent.
     *
     * @param file the file path to validate
     */
    private static void validatePathString(final String pathString) throws FileNotFoundException
    {
        if(pathString == null ||
           pathString.isBlank() ||
           Files.notExists(Paths.get(pathString)))
        {
            throw new FileNotFoundException("Invalid file path");
        }
    }

    /*
     * Validates that the input stream is not null.
     *
     * @param inputStream the input stream to validate
     */
    private static void validateInputStream(final InputStream inputStream)
    {
        if(inputStream == null)
        {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
    }
}
