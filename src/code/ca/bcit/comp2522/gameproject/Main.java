package ca.bcit.comp2522.gameproject;

import java.util.Scanner;

import ca.bcit.comp2522.gameproject.mastermind.MastermindGame;
import ca.bcit.comp2522.gameproject.numbergame.NumberGameGUI;
import ca.bcit.comp2522.gameproject.wordgame.WordGameLauncher;

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
    private static final Playable mastermindGame;

    private static final String CHOICE_WORD_GAME   = "w";
    private static final String CHOICE_NUMBER_GAME   = "n";
    private static final String CHOICE_MASTERMIND = "m";
    private static final String CHOICE_QUIT          = "q";

    private static final String MESSAGE_INVALID_CHOICE = "Not a valid option, please select a valid game.";
    private static final String MESSAGE_EXIT           = "Exiting the game. Goodbye!";

    static
    {
        scan       = new Scanner(System.in);
        wordGame   = new WordGameLauncher();
        numberGame   = new NumberGameGUI();
        mastermindGame = new MastermindGame();
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
                case CHOICE_WORD_GAME -> wordGame.play();
                case CHOICE_NUMBER_GAME -> numberGame.play();
                case CHOICE_MASTERMIND -> mastermindGame.play();
                case CHOICE_QUIT -> System.out.println(MESSAGE_EXIT);
                default -> System.out.println(MESSAGE_INVALID_CHOICE);
            }
        } while(! choice.equals(CHOICE_QUIT));
    }

    /*
     * Prints the game menu to the console.
     */
    private static void printGameMenu()
    {
        System.out.println("""
                           Press W to play the Word game.
                           Press N to play the Number game.
                           Press M to play Mastermind - Deception.
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
