package ca.bcit.comp2522.gameproject;

import java.io.IOException;
import java.util.Scanner;

public class WordGame
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

    static void play()
    {
        System.out.println("Starting Word Game...");

    }

    static void capitalCityQuestion()
    {
        final Country thisCountry;
        final String countryName;
        final String countryCapital;
        final String userAnswer;
        final boolean answerIsCorrect;

        thisCountry = world.getRandomCountry();
        countryName = thisCountry.getName();
        countryCapital = thisCountry.getCapitalCityName();

        System.out.println(countryCapital + " is the capial of what country?");
        userAnswer = scan.nextLine();

        answerIsCorrect = userAnswer.equals(countryName);

        if(answerIsCorrect)
        {
            System.out.println("That's correct!" + 
                            " +2 points.");
        }
        else
        {
            System.out.println("Incorrect! One more try...");
        }
        
        
    }
}
