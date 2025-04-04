package ca.bcit.comp2522.gameproject.wordgame;

/**
 * Represents an abstract question in the word game.
 * <p>
 * This class serves as the base for all question types in the game, providing
 * common validation and structure for country-based questions.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
abstract class Question
{
    /**
     * The country associated with this question.
     */
    final Country country;

    /**
     * Constructs a new Question with the specified country.
     * <p>
     * Validates the country parameter and initializes the prompt
     * by calling the createPrompt method.
     * </p>
     *
     * @param country the Country object containing data for this question
     * @throws NullPointerException if country is null
     */
    Question(final Country country)
    {
        validateCountry(country);
        this.country = country;
    }

    /**
     * Gets the prompt for this question by calling createPrompt.
     *
     * @return the question prompt as a String
     */
    final String getPrompt()
    {
        final String prompt;
        prompt = createPrompt();
        
        return prompt;
    }

    /**
     * Creates the question prompt based on the country data.
     * <p>
     * This method must be implemented by subclasses to define
     * the specific format of the question.
     * </p>
     *
     * @return a String containing the formatted question prompt
     */
    abstract String createPrompt();
    
    /**
     * Gets the expected answer for this question.
     * <p>
     * This method must be implemented by subclasses to define
     * what constitutes a correct answer for the question type.
     * </p>
     *
     * @return a String containing the expected answer
     */
    abstract String getExpectedAnswer();

    /**
     * Validates that the country parameter is not null.
     *
     * @param country the Country object to validate
     * @throws NullPointerException if country is null
     */
    private static void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Country cannot be null");
        }
    }
}
