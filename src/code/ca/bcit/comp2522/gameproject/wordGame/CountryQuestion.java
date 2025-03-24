package ca.bcit.comp2522.gameproject.wordGame;

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
class CountryQuestion extends Question
{
    /**
     * Constructs a new CountryQuestion with the specified country.
     *
     * @param country the Country object containing the data for this question
     */
    CountryQuestion(Country country)
    {
        super(country);
    }

    /**
     * Creates the question prompt for this country question.
     *
     * @return a string containing the question prompt
     */
    @Override
    protected String createPrompt()
    {
        return "What is the capital of " + country.getName() + "?";
    }

    /**
     * Gets the expected answer for this question.
     *
     * @return the name of the capital city as the expected answer
     */
    @Override
    public String getExpectedAnswer()
    {
        return country.getCapitalCityName();
    }
}
