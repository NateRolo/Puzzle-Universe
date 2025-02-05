package ca.bcit.comp2522.gameproject;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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
public class World
{
    private final Map<String, Country> countries;
    private static final String[]      RESOURCE_FILES = {"/a.txt",
                                                         "/b.txt",
                                                         "/c.txt",
                                                         "/d.txt",
                                                         "/e.txt",
                                                         "/f.txt",
                                                         "/g.txt",
                                                         "/h.txt",
                                                         "/i.txt",
                                                         "/j.txt",
                                                         "/k.txt",
                                                         "/l.txt",
                                                         "/m.txt",
                                                         "/n.txt",
                                                         "/o.txt",
                                                         "/p.txt",
                                                         "/q.txt",
                                                         "/r.txt",
                                                         "/s.txt",
                                                         "/t.txt",
                                                         "/u.txt",
                                                         "/v.txt",
                                                         "/y.txt",
                                                         "/z.txt"};

    /**
     * Constructs a new World object and loads countries from resource files.
     *
     * @throws IOException if there's an error reading the resource files
     */
    public World() throws IOException
    {
        this.countries = new HashMap<>();
        loadCountriesFromFiles();
    }

    /* 
     * Loads countries from all resource files.
     */
    private void loadCountriesFromFiles() throws IOException
    {
        for(final String file : RESOURCE_FILES)
        {
            loadCountriesFromFile(file);
        }
    }

    /* 
     * Loads countries from a single resource file.
     */
    private void loadCountriesFromFile(final String filePath) throws IOException
    {
        final List<String> lines;

        lines = FileReader.readLinesFromResource(filePath);
        processFileLines(lines);
    }

    /* 
     * Processes lines from a file to create Country objects.
     */
    private void processFileLines(final List<String> lines)
    {
        String   countryName;
        String   capitalName;
        String[] facts;

        for(int i = 0; i < lines.size(); i++)
        {
            final String line;

            line = lines.get(i);
            if(line.isEmpty())
            {
                continue;
            }

            if(line.contains(":"))
            {
                final String[] parts;

                parts       = line.split(":");
                countryName = parts[0].trim();
                capitalName = parts[1].trim();
                facts       = new String[3];

                for(int j = 0; j < 3; j++)
                {
                    if(i + j + 1 < lines.size())
                    {
                        facts[j] = lines.get(i + j + 1)
                                        .trim();
                    }
                }

                final Country country;

                country = new Country(countryName,
                                      capitalName,
                                      facts[0],
                                      facts[1],
                                      facts[2]);
                addCountry(country);
                i += 3;
            }
        }
    }

    /**
     * Adds a country to the world.
     *
     * @param country the country to add
     */
    public final void addCountry(final Country country)
    {
        if(country == null)
        {
            throw new IllegalArgumentException("Invalid country:" + country);
        }

        this.countries.put(country.getName(),
                           country);
    }

    /**
     * Gets a country by its name.
     *
     * @param name the name of the country to retrieve
     * @return the Country object if found, null otherwise
     */
    public final Country getCountry(final String name)
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Invalid country name: " + name);
        }

        return countries.get(name);
    }

    /**
     * Checks if a country exists in the world.
     *
     * @param name the name of the country to check
     * @return true if the country exists, false otherwise
     */
    public final boolean hasCountry(final String name)
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Invalid country name: " + name);
        }

        return countries.containsKey(name);
    }

    /**
     * Gets the number of countries in the world.
     *
     * @return the number of countries
     */
    public final int getCountryCount()
    {
        return countries.size();
    }
}