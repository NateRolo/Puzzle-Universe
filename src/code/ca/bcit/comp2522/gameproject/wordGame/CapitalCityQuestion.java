package ca.bcit.comp2522.gameproject.wordGame;

class CapitalCityQuestion extends Question
{
    CapitalCityQuestion(Country country)
    {
        super(country);
    }

    @Override
    String getPrompt()
    {
        return country.getCapitalCityName() + "is the capital of what country?";
    }

    @Override
    String getExpectedAnswer()
    {
        return country.getName();
    }
}
