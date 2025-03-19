package ca.bcit.comp2522.gameproject.wordGame;

public class QuestionFactory
{
    public static Question createQuestion(final Country country,
                                          final int questionType)
    {
        return switch(questionType)
        {
            case WordGame.CAPITAL_CITY_QUESTION -> new CapitalCityQuestion(country);
            case WordGame.COUNTRY_QUESTION -> new CountryQuestion(country);
            case WordGame.FACT_QUESTION -> new FactQuestion(country);
            default -> throw new IllegalArgumentException("Invalid question type");
        };
    }
}