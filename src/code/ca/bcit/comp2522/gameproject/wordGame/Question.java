package ca.bcit.comp2522.gameproject.wordGame;

abstract class Question
{
    protected final Country country;

    Question(final Country country)
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

    abstract String getPrompt();
    abstract String getExpectedAnswer();
}
