package ca.bcit.comp2522.gameproject.wordGame;

class QuestionFactory
{
    static Question createQuestion(final Country country,
                                          final int questionType)
    {
        validateCountry(country);

        return switch(questionType)
        {
            case WordGameLauncher.CAPITAL_CITY_QUESTION -> new CapitalCityQuestion(country);
            case WordGameLauncher.COUNTRY_QUESTION -> new CountryQuestion(country);
            case WordGameLauncher.FACT_QUESTION -> new FactQuestion(country);
            default -> throw new IllegalArgumentException("Invalid question type");
        };
    }

    private static void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new NullPointerException("Cannot create question from null country.");
        }
    }
}