package ca.bcit.comp2522.gameproject;

import java.io.IOException;
import java.util.Scanner;

public class WordGame implements Playable
{
    private static final Scanner scan;
    private static final Score currentScore;
    private static final World world;
    
    static
    {
        scan = new Scanner(System.in);
        currentScore = new Score();
        
        try
        {
            world = new World();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Failed to load world data.");
        }
    }

    @Override
    public void play()
    {
        int questionsAsked;
        String playAgain;

        System.out.println("Starting Word Game...");

        do
        {
            questionsAsked = 1;
            
            while (questionsAsked < 11)
            {
                System.out.printf("Question %d/10\n", questionsAsked);
                askQuestion();
                questionsAsked++;
            }

            currentScore.incrementNumGamesPlayed();

            currentScore.printScore();
            System.out.println("Would you like to play again?");
            playAgain = scan.nextLine();
        } while (playAgain.equals("yes"));

    }
    private static void askQuestion()
    {
        final Country thisCountry;
        final int questionStyle;

        thisCountry = world.getRandomCountry();
        questionStyle = (int) (Math.random() * 3);
        
        switch(questionStyle)
        {
            case 0 -> capitalCityQuestion(thisCountry); 
            case 1 -> countryQuestion(thisCountry);
            case 2 -> factQuestion(thisCountry);
        }
    }

    private static void capitalCityQuestion(final Country country)
    {
        final String countryName;
        final String countryCapital;
        
        countryName = country.getName();
        countryCapital = country.getCapitalCityName();

        System.out.println(countryCapital + " is the capital of what country?");
        checkAnswer(countryName);
    }

    private static void countryQuestion(final Country country)
    {
        final String countryName;
        final String countryCapital;

        countryName = country.getName();
        countryCapital = country.getCapitalCityName();

        System.out.println("What is the capital of " + countryName + "?");
        checkAnswer(countryCapital);
    }

    private static void factQuestion(final Country country)
    {
        final String countryName;
        final String[] countryFacts;
        final int randomFact;

        countryName = country.getName();
        countryFacts = country.getFacts();
        randomFact = (int)(Math.random() * countryFacts.length);

        System.out.println(countryFacts[randomFact] + 
                           "\n" + 
                           "What country is this fact describing?");
        checkAnswer(countryName);
    }

    /* 
     * Checks if the user's answer matches the expected answer.
     * Returns the number of points earned.
     */
    private static void checkAnswer(final String expectedAnswer)
    {
        String userAnswer;
        boolean answerIsCorrect;
        
        userAnswer = scan.nextLine();
        answerIsCorrect = userAnswer.equalsIgnoreCase(expectedAnswer);

        if (answerIsCorrect)
        {
            System.out.println("CORRECT");
            currentScore.incrementNumCorrectFirstAttempt();
        }
        else
        {
            System.out.println("INCORRECT" + "\nOne more guess:");
            userAnswer      = scan.nextLine();
            answerIsCorrect = userAnswer.equals(expectedAnswer);

            if (answerIsCorrect)
            {
                System.out.println("CORRECT");
                currentScore.incrementNumCorrectSecondAttempt();
            }

            System.out.println("INCORRECT" +
                               "\n" +
                               "The correct answer was " +
                               expectedAnswer);
            currentScore.incrementNumIncorrectTwoAttempts();
        }
    }


}
