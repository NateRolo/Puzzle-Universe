package ca.bcit.comp2522.gameproject.wordgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
    private final Map<String, Country> countries;

    private static final String COUNTRY_CAPITAL_SEPARATOR = ":";
    private static final int    FACTS_PER_COUNTRY         = 3;
    private static final int    LINES_TO_SKIP_AFTER_FACTS = 3;
    private static final int    COUNTRY_NAME_INDEX        = 0;
    private static final int    CAPITAL_NAME_INDEX        = 1;
    private static final int    FIRST_FACT_OFFSET         = 1;

    private static final int FIRST_FACT_INDEX  = 0;
    private static final int SECOND_FACT_INDEX = 1;
    private static final int THIRD_FACT_INDEX  = 2;

    private static final String[] RESOURCE_FILES = {"src/res/a.txt",
                                                    "src/res/b.txt",
                                                    "src/res/c.txt",
                                                    "src/res/d.txt",
                                                    "src/res/e.txt",
                                                    "src/res/f.txt",
                                                    "src/res/g.txt",
                                                    "src/res/h.txt",
                                                    "src/res/i.txt",
                                                    "src/res/j.txt",
                                                    "src/res/k.txt",
                                                    "src/res/l.txt",
                                                    "src/res/m.txt",
                                                    "src/res/n.txt",
                                                    "src/res/o.txt",
                                                    "src/res/p.txt",
                                                    "src/res/q.txt",
                                                    "src/res/r.txt",
                                                    "src/res/s.txt",
                                                    "src/res/t.txt",
                                                    "src/res/u.txt",
                                                    "src/res/v.txt",
                                                    "src/res/y.txt",
                                                    "src/res/z.txt"};

    /**
     * Constructs a new World object and loads countries from resource files.
     *
     * @throws IOException if there's an error reading the resource files
     */
    World() throws IOException
    {
        this.countries = new HashMap<>();
        loadCountriesFromAllFiles();
    }

    /**
     * Loads countries from all resource files defined in RESOURCE_FILES.
     * Iterates through each file path and calls loadCountriesFromFile for each one.
     */
    private void loadCountriesFromAllFiles()
    {
        for(final String file : RESOURCE_FILES)
        {
            loadCountriesFromFile(file);
        }
    }

    /**
     * Loads countries from a single resource file.
     * Validates the file path, reads lines from the resource, and processes them.
     * 
     * @param filePath the path to the resource file to load
     */
    private void loadCountriesFromFile(final String filePath)
    {
        try
        {
            validatePath(filePath);
            final List<String> lines;

            lines = FileManager.readLinesFromResource(filePath);
            processFileLines(lines);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Processes lines from a file to create Country objects.
     * Parses country names, capital names, and facts from the file content.
     * 
     * @param lines the list of strings read from a resource file
     */
    private void processFileLines(final List<String> lines)
    {
        String   countryName;
        String   capitalName;
        String[] facts;

        for(int lineIndex = 0; lineIndex < lines.size(); lineIndex++)
        {
            final String  line;
            final Country country;

            line = lines.get(lineIndex);
            if(line.isEmpty())
            {
                continue;
            }

            if(line.contains(COUNTRY_CAPITAL_SEPARATOR))
            {
                final String[] parts;

                parts       = line.split(COUNTRY_CAPITAL_SEPARATOR);
                countryName = parts[COUNTRY_NAME_INDEX].trim();
                capitalName = parts[CAPITAL_NAME_INDEX].trim();
                facts       = new String[FACTS_PER_COUNTRY];

                for(int factIndex = 0; factIndex < FACTS_PER_COUNTRY; factIndex++)
                {
                    if(lineIndex + factIndex + FIRST_FACT_OFFSET < lines.size())
                    {
                        facts[factIndex] = lines.get(lineIndex + factIndex + FIRST_FACT_OFFSET)
                                                .trim();
                    }
                }

                country = new Country(countryName,
                                      capitalName,
                                      facts[FIRST_FACT_INDEX],
                                      facts[SECOND_FACT_INDEX],
                                      facts[THIRD_FACT_INDEX]);
                addCountry(country);
                lineIndex += LINES_TO_SKIP_AFTER_FACTS;
            }
        }
    }

    /**
     * Adds a country to the world.
     * Validates the country object before adding it to the countries map.
     *
     * @param country the country to add
     */
    final void addCountry(final Country country)
    {
        validateCountry(country);

        this.countries.put(country.getName(),
                           country);
    }

    /**
     * Gets a country by its name.
     * Validates the country name before attempting to retrieve it.
     *
     * @param name the name of the country to retrieve
     * @return the Country object if found, null otherwise
     */
    final Country getCountry(final String name)
    {
        validateCountryName(name);

        return countries.get(name);
    }

    /**
     * Returns a random Country from the list of available countries.
     * Creates a list from the map values and selects a random index.
     *
     * @return A randomly selected Country object
     */
    final Country getRandomCountry()
    {
        final List<Country> countryList;
        final int           randomIndex;

        countryList = new ArrayList<>(countries.values());
        randomIndex = (int)(Math.random() * countryList.size());

        return countryList.get(randomIndex);
    }

    /**
     * Checks if a country exists in the world.
     * Validates the country name before checking for its existence.
     *
     * @param name the name of the country to check
     * @return true if the country exists, false otherwise
     */
    final boolean hasCountry(final String name)
    {
        validateCountryName(name);

        return countries.containsKey(name);
    }

    /**
     * Gets the number of countries in the world.
     * Returns the size of the countries map.
     *
     * @return the number of countries
     */
    final int getCountryCount()
    {
        return countries.size();
    }

    /*
     * Validates that a file path is not null, blank, and exists.
     * 
     * @param filePath the file path to validate
     * @throws IOException if the path does not exist
     */
    private static void validatePath(final String filePath) throws IOException
    {
        if(filePath == null || filePath.isBlank())
        {
            throw new IllegalArgumentException("Path can't be null or blank.");
        }

        if(Files.notExists(Paths.get(filePath)))
        {
            throw new FileNotFoundException("Path does not exist.");
        }
    }

    /*
     * Validates that a Country object is not null.
     * 
     * @param country the Country object to validate
     */
    private static void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Country cannot be null.");
        }
    }

    /*
     * Validates that a country name is not null, blank, and exists in the world.
     * 
     * @param countryName the name of the country to validate
     */
    private void validateCountryName(final String countryName)
    {
        if(countryName == null || countryName.isBlank())
        {
            throw new IllegalArgumentException("Country name can't be null or blank");
        }

        if(! hasCountry(countryName))
        {
            throw new NullPointerException("Country doesn't exist: " + countryName);
        }
    }
}
