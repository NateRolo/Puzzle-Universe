package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Represents a question about identifying a country based on an interesting
 * fact.
 * <p>
 * This class extends the Question class to create questions where the user
 * is given a random fact about a country and must identify which country
 * the fact is describing.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class FactQuestion extends
                   Question
{
    private final int factIndex;

    /**
     * Constructs a new FactQuestion with the specified country.
     * Randomly selects one of the country's facts to use in the question.
     *
     * @param country the Country object containing the data for this question
     */
    FactQuestion(Country country)
    {
        super(country);

        final int randomIndex;
        randomIndex = (int)(Math.random() * country.getFacts().length);

        this.factIndex = randomIndex;
    }

    /**
     * Creates the question prompt for this fact question.
     *
     * @return a string containing the question prompt with the selected fact
     */
    @Override
    String createPrompt()
    {
        final StringBuilder promptBuilder;
        final String        countryFact;
        final String        promptString;

        promptBuilder = new StringBuilder();
        countryFact   = country.getFacts()[factIndex];

        promptBuilder.append(countryFact)
                     .append("\n")
                     .append("What country is this fact describing?");

        promptString = promptBuilder.toString();

        return promptString;
    }

    /**
     * Gets the expected answer for this question.
     *
     * @return the name of the country as the expected answer
     */
    @Override
    String getExpectedAnswer()
    {
        final String countryName;
        countryName = country.getCountryName();

        return countryName;
    }
}
