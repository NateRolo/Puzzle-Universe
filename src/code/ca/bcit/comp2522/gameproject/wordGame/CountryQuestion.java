package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Represents a question about identifying a country's capital city.
 * <p>
 * This class extends the Question class to create questions where the user
 * is given a country name and must identify its capital city.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class CountryQuestion extends
                      Question
{
    /**
     * Constructs a new CountryQuestion with the specified country.
     *
     * @param country the Country object containing the data for this question
     */
    CountryQuestion(final Country country)
    {
        super(country);
    }

    /**
     * Creates the question prompt for this country question.
     *
     * @return a string containing the question prompt
     */
    @Override
    String createPrompt()
    {
        final StringBuilder promptBuilder;
        final String        countryName;
        final String        promptString;

        promptBuilder = new StringBuilder();
        countryName   = country.getCountryName();

        promptBuilder.append("What is the capital of ")
                     .append(countryName)
                     .append("?");

        promptString = promptBuilder.toString();

        return promptString;
    }

    /**
     * Gets the expected answer for this question.
     *
     * @return the name of the capital city as the expected answer
     */
    @Override
    String getExpectedAnswer()
    {
        final String capitalCityName;
        capitalCityName = country.getCapitalCityName();

        return capitalCityName;
    }
}
