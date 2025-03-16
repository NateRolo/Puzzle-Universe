package ca.bcit.comp2522.gameproject;

import java.util.Scanner;

/**
 * Main class for the game project.
 * <p>
 * This class provides a simple text-based menu that allows the user to choose from different games.
 * The menu runs in an infinite loop until the user chooses to quit by pressing 'Q' or 'q'.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public class Main
{
    private static final Scanner scan;
    private static final Playable wordGame;
    private static final Playable numberGame;
    private static final Playable myGame;

    static
    {
        scan = new Scanner(System.in);
        wordGame = new WordGame();
        numberGame = new NumberGame();
        myGame = new MyGame();
    }

    /**
     * Main entry point for the application.
     * <p>
     * Runs the main game selection loop.
     * The user is prompted to select a game or quit.
     * If an invalid option is entered, an error message is displayed.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args)
    {
        printWelcomeMessage();

        String choice;

        do
        {
            printGameMenu();
            choice = scan.next()
                         .toLowerCase();

            switch(choice)
            {
                case "w" -> wordGame.play();
                case "n" -> numberGame.play();
                case "m" -> myGame.play();
                case "q" -> System.out.println("Exiting the game. Goodbye!");
                default -> System.out.println("Not a valid option, please select a valid game.");
            }
        } while(! choice.equals("q"));
    }

    /*
     * Prints the game menu to the console.
     */
    private static void printGameMenu()
    {
        System.out.println("""
                           Press W to play the Word game.
                           Press N to play the Number game.
                           Press M to play the <your game's name> game.
                           Press Q to quit.
                           """);
    }

    /*
     * Prints a welcome message to the console.
     */
    private static void printWelcomeMessage()
    {
        System.out.println("""
                           Welcome to Nathan's Game Corner!
                           Choose one of the games below to start playing.
                           """);
    }
}
