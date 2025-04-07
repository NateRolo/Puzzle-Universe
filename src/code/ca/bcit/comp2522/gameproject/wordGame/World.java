package ca.bcit.comp2522.gameproject.wordgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a world containing multiple countries.
 * <p>
 * This class manages a collection of countries using a HashMap where
 * the country name is the key and the Country object is the value.
 * Countries are loaded from resource files.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class World
{
    private final Map<String, Country> countriesMap;

    // Directory constants
    private static final String DIRECTORY_SRC       = "src";
    private static final String DIRECTORY_RES       = "res";
    private static final String DIRECTORY_COUNTRIES = "countries";

    private static final String COUNTRY_CAPITAL_SEPARATOR = ":";
    private static final int    FACTS_PER_COUNTRY         = 3;
    private static final int    LINES_TO_SKIP_AFTER_FACTS = 3;
    private static final int    INDEX_NAME_COUNTRY        = 0;
    private static final int    INDEX_NAME_CAPITAL        = 1;
    private static final int    FIRST_FACT_OFFSET         = 1;

    private static final int INDEX_FACT_FIRST  = 0;
    private static final int INDEX_FACT_SECOND = 1;
    private static final int INDEX_FACT_THIRD  = 2;

    // Update RESOURCE_FILES to contain only filenames
    private static final String[] RESOURCE_FILES = {"a.txt",
                                                    "b.txt",
                                                    "c.txt",
                                                    "d.txt",
                                                    "e.txt",
                                                    "f.txt",
                                                    "g.txt",
                                                    "h.txt",
                                                    "i.txt",
                                                    "j.txt",
                                                    "k.txt",
                                                    "l.txt",
                                                    "m.txt",
                                                    "n.txt",
                                                    "o.txt",
                                                    "p.txt",
                                                    "q.txt",
                                                    "r.txt",
                                                    "s.txt",
                                                    "t.txt",
                                                    "u.txt",
                                                    "v.txt",
                                                    "y.txt",
                                                    "z.txt"};

    /**
     * Constructs a new World object and loads countries from resource files.
     */
    World()
    {
        this.countriesMap = new HashMap<>();
        try
        {
            loadCountriesFromAllFiles();
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Adds a country to the world.
     * Validates the country object before adding it to the countries map.
     *
     * @param country the country to add
     */
    void addCountry(final Country country)
    {
        validateCountryObject(country);

        this.countriesMap.put(country.getCountryName(),
                           country);
    }

    /**
     * Gets a country by its name.
     * Validates the country name before attempting to retrieve it.
     *
     * @param countryName the name of the country to retrieve
     * @return the Country object if found, null otherwise
     */
    Country getCountry(final String countryName)
    {
        validateCountryName(countryName);

        final Country requestedCountry;
        requestedCountry = countriesMap.get(countryName);

        return requestedCountry;
    }

    /**
     * Returns a random Country from the list of available countries.
     * Creates a list from the map values and selects a random index.
     *
     * @return A randomly selected Country object
     */
    Country getRandomCountry()
    {
        final List<Country> countryList;
        final int           randomIndex;
        final Country randomCountry;

        countryList = new ArrayList<>(countriesMap.values());
        randomIndex = (int)(Math.random() * countryList.size());
        randomCountry = countryList.get(randomIndex);

        return randomCountry;
    }

    /**
     * Checks if a country exists in the world.
     * Validates the country name before checking for its existence.
     *
     * @param countryName the name of the country to check
     * @return true if the country exists, false otherwise
     */
    boolean hasCountry(final String countryName)
    {
        validateCountryName(countryName);

        final boolean countryExists;
        countryExists = countriesMap.containsKey(countryName);

        return countryExists;
    }

     /**
     * Gets the number of countries in the world.
     * Returns the size of the countries map.
     *
     * @return the number of countries
     */
    int getCountryCount()
    {
        final int countryCount;
        countryCount = countriesMap.size();

        return countryCount;
    }

    /*
     * Loads countries from all resource files defined in RESOURCE_FILES.
     * Iterates through each file path and calls loadCountriesFromFile for each
     * one.
     * 
     * @throws FileNotFoundException if there's an error reading the resource files
     */
    private void loadCountriesFromAllFiles() throws FileNotFoundException
    {
        for(final String file : RESOURCE_FILES)
        {
            final Path filePath;
            filePath = Paths.get(DIRECTORY_SRC,
                                 DIRECTORY_RES,
                                 DIRECTORY_COUNTRIES,
                                 file);

            validateFilePath(filePath);
            loadCountriesFromFile(filePath);
        }
    }

    /*
     * Loads countries from a single resource file.
     * Validates the file path, reads lines from the resource, and processes
     * them.
     * 
     * @param filePath the path to the resource file to load
     */
    private void loadCountriesFromFile(final Path filePath)
    {
        try
        {
            validateFilePath(filePath);

            final List<String> lines;
            lines = FileManager.readLinesFromResource(filePath.toString());
            processFileLines(lines);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Processes lines from a file to create Country objects.
     * Parses country names, capital names, and facts from the file content.
     * 
     * The method expects a specific file format where:
     * - Each country entry starts with a line containing "Country:Capital"
     * - This is followed by three lines containing facts about the country
     * - Empty lines are skipped
     * - After processing a country and its facts, several lines are skipped
     *   to position at the next country entry
     * 
     * Each valid country entry results in a new Country object being created
     * and added to the world's collection of countries.
     * 
     * @param lines the list of strings read from a resource file
     */
    private void processFileLines(final List<String> lines)
    {
        String   countryName;
        String   capitalName;
        String[] facts;

        // Iterate through each line in the file
        for(int lineIndex = 0; lineIndex < lines.size(); lineIndex++)
        {
            final String  line;
            final Country countryToAdd;

            line = lines.get(lineIndex);
            if(line.isEmpty())
            {
                continue;
            }

            if(line.contains(COUNTRY_CAPITAL_SEPARATOR))
            {
                final String[] parts;

                // Split the line into country name and capital city
                parts       = line.split(COUNTRY_CAPITAL_SEPARATOR);
                countryName = parts[INDEX_NAME_COUNTRY].trim();
                capitalName = parts[INDEX_NAME_CAPITAL].trim();
                facts       = new String[FACTS_PER_COUNTRY];

                // Extract the three facts that follow the country/capital line
                for(int factIndex = 0; factIndex <
                                       FACTS_PER_COUNTRY; factIndex++)
                {
                    // Calculate the line index for each fact and check if it's
                    if(lineIndex + factIndex + FIRST_FACT_OFFSET < lines.size())
                    {
                        // Extract and trim the fact from the appropriate line
                        facts[factIndex] = lines.get(lineIndex +
                                                     factIndex +
                                                     FIRST_FACT_OFFSET)
                                                .trim();
                    }
                }

                // Create a new Country object with the extracted data
                countryToAdd = new Country(countryName,
                                      capitalName,
                                      facts[INDEX_FACT_FIRST],
                                      facts[INDEX_FACT_SECOND],
                                      facts[INDEX_FACT_THIRD]);
                addCountry(countryToAdd);
                lineIndex += LINES_TO_SKIP_AFTER_FACTS;
            }
        }
    }

   
    /*
     * Validates that a file path is not null, blank, and exists.
     * 
     * @param filePath the file path to validate
     * 
     * @throws IOException if the path does not exist
     */
    private static void validateFilePath(final Path filePath) throws FileNotFoundException
    {
        if(filePath == null)
        {
            throw new IllegalArgumentException("Path can't be null.");
        }

        if(Files.notExists(filePath))
        {
            throw new FileNotFoundException("Path does not exist: " + filePath);
        }
    }

    /*
     * Validates that a Country object is not null.
     * 
     * @param country the Country object to validate
     */
    private static void validateCountryObject(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Country cannot be null.");
        }
    }

    /*
     * Validates that a country name is not null, blank, and exists in the
     * world.
     * 
     * @param countryName the name of the country to validate
     */
    private void validateCountryName(final String countryName)
    {
        if(countryName == null || countryName.isBlank())
        {
            throw new IllegalArgumentException("Country name can't be null or blank");
        }

        if(countriesMap.containsKey(countryName))
        {
            throw new NullPointerException("Country doesn't exist: " +
                                           countryName);
        }
    }
}
