package ca.bcit.comp2522.gameproject;

import java.util.Map;
import java.util.HashMap;


/**
 * Represents a world containing multiple countries.
 * <p>
 * This class manages a collection of countries using a HashMap where
 * the country name is the key and the Country object is the value.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public class World
{
    private final Map<String, Country> countries;

    /**
     * Constructs a new World object with an empty collection of countries.
     */
    public World()
    {
        this.countries = new HashMap<>();
    }

    /**
     * Adds a country to the world.
     *
     * @param country the country to add
     */
    public final void addCountry(final Country country)
    {
        if (country == null)
        {
            throw new IllegalArgumentException();
        }
        
        this.countries.put(country.getName(), country);
    }

    /**
     * Gets a country by its name.
     *
     * @param name the name of the country to retrieve
     * @return the Country object if found, null otherwise
     */
    public final Country getCountry(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException();
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
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException();
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