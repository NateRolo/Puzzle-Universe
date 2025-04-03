package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Factory class for creating different types of questions.
 * <p>
 * This class handles the creation of specific question types based on the
 * provided question style and country data. It ensures proper instantiation
 * of the correct question subclass.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class QuestionFactory
{
    /**
     * Creates a new question of the specified type for the given country.
     * <p>
     * This method validates the country parameter and creates the appropriate
     * question type based on the questionType parameter.
     * </p>
     *
     * @param country      the country to create a question about
     * @param questionType the type of question to create (must be one of the constants defined in WordGameLauncher)
     * @return a new Question instance of the specified type
     */
    static Question createQuestion(final Country country,
                                   final int questionType)
    {
        validateCountry(country);

        return switch(questionType)
        {
            case WordGame.CAPITAL_CITY_QUESTION -> new CapitalCityQuestion(country);
            case WordGame.COUNTRY_QUESTION -> new CountryQuestion(country);
            case WordGame.FACT_QUESTION -> new FactQuestion(country);
            default -> throw new IllegalArgumentException("Invalid question type");
        };
    }

    /**
     * Validates that the country parameter is not null.
     * <p>
     * This method ensures that a valid Country object is provided before
     * attempting to create a question.
     * </p>
     *
     * @param country the Country object to validate
     */
    private static void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Cannot create question from null country.");
        }
    }
}