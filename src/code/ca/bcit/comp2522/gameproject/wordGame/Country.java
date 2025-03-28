package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Represents a country with its name, capital city, and interesting facts.
 * <p>
 * This class stores basic information about a country including its name,
 * capital city name, and three interesting facts. All fields are immutable
 * after construction.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */

final class Country
{
    private final String name;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * Constructs a Country object.
     *
     * @param name            the name of the country
     * @param capitalCityName the name of the capital city
     * @param fact1           first fact about the country
     * @param fact2           second fact about the country
     * @param fact3           third fact about the country
     */
    Country(final String name,
                  final String capitalCityName,
                  final String fact1,
                  final String fact2,
                  final String fact3)
    {
        validateName(name);
        validateCapitalCityName(capitalCityName);
        validateFact(fact1);
        validateFact(fact2);
        validateFact(fact3);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = new String[]{fact1, fact2, fact3};
    }

    /**
     * Gets the name of the country.
     *
     * @return name as String
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Gets the capital city name.
     *
     * @return capital city name as String
     */
    public final String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Gets a copy of the facts array.
     *
     * @return copy of facts array
     */
    public final String[] getFacts()
    {
        return facts.clone();
    }

    /* 
     * Validates the country name.
     */
    private static void validateName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Invalid country name: " + name);
        }
    }

    /* 
     * Validates the capital city name.
     */
    private static void validateCapitalCityName(final String capitalCityName)
    {
        if (capitalCityName == null || capitalCityName.isBlank())
        {
            throw new IllegalArgumentException("Invalid capital city name:" + 
                                                capitalCityName);
        }
    }

    /* 
     * Validates a single fact.
     */
    private static void validateFact(final String fact)
    {
        if (fact == null || fact.isBlank())
        {
            throw new IllegalArgumentException("Invalid fact: " +
                                               fact);
        }
    }
}