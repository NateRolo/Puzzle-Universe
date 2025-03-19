package ca.bcit.comp2522.gameproject.wordGame;

class CountryQuestion extends Question
{
    CountryQuestion(Country country)
    {
        super(country);
    }

    @Override
    String getPrompt()
    {
        return "What is the capital of " + country.getName() + "?";
    }

    @Override
    String getExpectedAnswer()
    {
        return country.getCapitalCityName();
    }
}
