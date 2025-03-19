package ca.bcit.comp2522.gameproject;

import ca.bcit.comp2522.gameproject.barkingBombs.BarkingBombsLauncher;
import ca.bcit.comp2522.gameproject.numberGame.NumberGameLauncher;
import ca.bcit.comp2522.gameproject.wordGame.WordGameLauncher;

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
    private static final Scanner  scan;
    private static final Playable wordGame;
    private static final Playable numberGame;
    private static final Playable barkingBombs;

    private static final String WORD_GAME_CHOICE   = "w";
    private static final String NUMBER_GAME_CHOICE   = "n";
    private static final String BARKING_BOMBS_CHOICE = "b";
    private static final String QUIT_CHOICE          = "q";

    private static final String INVALID_CHOICE_MESSAGE = "Not a valid option, please select a valid game.";
    private static final String EXIT_MESSAGE           = "Exiting the game. Goodbye!";

    static
    {
        scan       = new Scanner(System.in);
        wordGame   = new WordGameLauncher();
        numberGame   = new NumberGameLauncher();
        barkingBombs = new BarkingBombsLauncher();
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
                case WORD_GAME_CHOICE -> wordGame.play();
                case NUMBER_GAME_CHOICE -> numberGame.play();
                case BARKING_BOMBS_CHOICE -> barkingBombs.play();
                case QUIT_CHOICE -> System.out.println(EXIT_MESSAGE);
                default -> System.out.println(INVALID_CHOICE_MESSAGE);
            }
        } while(! choice.equals(QUIT_CHOICE));
    }

    /*
     * Prints the game menu to the console.
     */
    private static void printGameMenu()
    {
        System.out.println("""
                           Press W to play the Word game.
                           Press N to play the Number game.
                           Press B to play the Barking Bombs.
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
