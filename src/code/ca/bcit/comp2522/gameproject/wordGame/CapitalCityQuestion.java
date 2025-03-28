package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Represents a question about identifying a country based on its capital city.
 * <p>
 * This class extends the Question class to create questions where the user
 * is given a capital city name and must identify the corresponding country.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class CapitalCityQuestion extends Question
{
    /**
     * Constructs a new CapitalCityQuestion with the specified country.
     *
     * @param country the Country object containing the data for this question
     */
    CapitalCityQuestion(final Country country)
    {
        super(country);
    }

    /**
     * Creates the question prompt for this capital city question.
     *
     * @return a string containing the question prompt
     */
    @Override
    String createPrompt()
    {
        return country.getCapitalCityName() + "is the capital of what country?";
    }

    /**
     * Gets the expected answer for this question.
     *
     * @return the name of the country as the expected answer
     */
    @Override
    String getExpectedAnswer()
    {
        return country.getName();
    }
}
