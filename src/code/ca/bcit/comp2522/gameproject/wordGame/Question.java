package ca.bcit.comp2522.gameproject.wordGame;

public abstract class Question
{
    protected final Country country;

    public Question(final Country country)
    {
        validateCountry(country);
        this.country = country;
    }

    private void validateCountry(final Country country)
    {
        if(country == null)
        {
            throw new IllegalArgumentException("Country cannot be null");
        }
    }

    public abstract String getPrompt();
    public abstract String getExpectedAnswer();
}
