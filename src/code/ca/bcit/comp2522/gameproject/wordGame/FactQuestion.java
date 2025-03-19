package ca.bcit.comp2522.gameproject.wordGame;

class FactQuestion extends Question
{
    private final int factIndex;

    FactQuestion(Country country)
    {
        super(country);
        this.factIndex = (int)(Math.random() * country.getFacts().length);
    }

    @Override
    String getPrompt()
    {
        return country.getFacts()[factIndex] + "\nWhat country is this fact describing?";
    }

    @Override
    String getExpectedAnswer()
    {
        return country.getName();
    }
}
