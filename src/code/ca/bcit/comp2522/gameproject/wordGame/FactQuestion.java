package ca.bcit.comp2522.gameproject.wordGame;

public class FactQuestion extends Question
{
    private final int factIndex;

    public FactQuestion(Country country)
    {
        super(country);
        this.factIndex = (int)(Math.random() * country.getFacts().length);
    }

    @Override
    public String getPrompt()
    {
        return country.getFacts()[factIndex] + "\nWhat country is this fact describing?";
    }

    @Override
    public String getExpectedAnswer()
    {
        return country.getName();
    }
}
