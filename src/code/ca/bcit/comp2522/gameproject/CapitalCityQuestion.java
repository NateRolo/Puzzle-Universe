package ca.bcit.comp2522.gameproject;

public class CapitalCityQuestion extends Question
{
    public CapitalCityQuestion(Country country)
    {
        super(country);
    }

    @Override
    public String getPrompt()
    {
        return country.getCapitalCityName() + "is the capital of what country?";
    }

    @Override
    public String getExpectedAnswer()
    {
        return country.getName();
    }
}
