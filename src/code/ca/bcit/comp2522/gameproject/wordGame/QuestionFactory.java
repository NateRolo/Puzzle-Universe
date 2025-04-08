package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Factory class for creating different types of questions.
 * <p>
 * This class handles the creation of specific question types based on the
 * provided question style and country data. It ensures proper instantiation of
 * the correct question subclass.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class QuestionFactory
{
    /**
     * Creates a new question of the specified type for the given
     * {@code Country}.
     * <p>
     * This method validates the country parameter and creates the appropriate
     * question type based on the questionType parameter.
     * </p>
     *
     * @param country      the {@code Country} to create a question about
     * @param questionType the type of question to create (must be one of the
     *                     constants defined in {@code WordGameLauncher})
     * @return a new {@code Question} instance of the specified type
     */
    static Question createQuestion(final Country country,
                                   final int questionType)
    {
        validateCountry(country);

        final Question question;

        switch(questionType)
        {
            case WordGame.QUESTION_CAPITAL_CITY -> question = new CapitalCityQuestion(country);
            case WordGame.QUESTION_COUNTRY -> question = new CountryQuestion(country);
            case WordGame.QUESTION_FACT -> question = new FactQuestion(country);
            default -> throw new IllegalArgumentException("Invalid question type");
        }

        return question;
    }

    /*
     * Validates that the country parameter is not null. 
     * <p>
     * This method ensures
     * that a valid {@code Country} object is provided before attempting to
     * create a question. 
     * </p>
     * @param country the {@code Country} object to validate
     */
    private static void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Cannot create question from null country.");
        }
    }
}