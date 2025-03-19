package ca.bcit.comp2522.gameproject.wordGame;

public class CountryQuestion extends Question
{
    public CountryQuestion(Country country)
    {
        super(country);
    }

    @Override
    public String getPrompt()
    {
        return "What is the capital of " + country.getName() + "?";
    }

    @Override
    public String getExpectedAnswer()
    {
        return country.getCapitalCityName();
    }
}
