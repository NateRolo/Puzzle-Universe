package ca.bcit.comp2522.gameproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading text files from resources.
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public class FileReader
{
    /**
     * Reads lines from a resource file.
     *
     * @param filePath path to the resource file
     * @return List of strings, each representing a line from the file
     * @throws IOException if file cannot be read
     */
    public final static List<String> readLinesFromResource(final String filePath) throws IOException
    {
        final List<String> lines;
        final InputStream inputStream;
        final InputStreamReader inputStreamReader;
        final BufferedReader bufferedReader;
        
        lines = new ArrayList<>();
        inputStream = FileReader.class.getResourceAsStream(filePath);
        
        if (inputStream == null)
        {
            throw new IOException("Resource not found: " + filePath);
        }
        
        inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        bufferedReader = new BufferedReader(inputStreamReader);
        
        try (inputStream; bufferedReader) 
        {
            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                lines.add(line);
            }
        }
        return lines;
    }
}
