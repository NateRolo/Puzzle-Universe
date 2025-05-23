package ca.bcit.comp2522.gameproject;

import java.util.Scanner;

import ca.bcit.comp2522.gameproject.interfaces.Replayable;
import ca.bcit.comp2522.gameproject.mastermind.MastermindGame;
import ca.bcit.comp2522.gameproject.numbergame.NumberGame;
import ca.bcit.comp2522.gameproject.wordgame.WordGame;

/**
 * Main class for the game project.
 * <p>
 * This class provides a simple text-based menu that allows the user to choose
 * from different games.
 * The menu runs in an infinite loop until the user chooses to quit by pressing
 * {@value #CHOICE_QUIT}.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class Main
{
    private static final Scanner    scan;
    private static final Replayable wordGame;
    private static final Replayable numberGame;
    private static final Replayable mastermindGame;

    private static final String CHOICE_WORD_GAME   = "W";
    private static final String CHOICE_NUMBER_GAME = "N";
    private static final String CHOICE_MASTERMIND  = "M";
    private static final String CHOICE_QUIT        = "Q";

    private static final String MENU_WORD_GAME   = " to play the Word Game\n";
    private static final String MENU_NUMBER_GAME = " to play the Number Game\n";
    private static final String MENU_MASTERMIND  = " to play Mastermind - Deception\n";

    private static final String SEPARATOR = "----------------------------------------";
    private static final String GREETING  = "WELCOME TO PUZZLE UNIVERSE";

    private static final String MESSAGE_INVALID_CHOICE = "Not a valid option, please select a valid game.";
    private static final String MESSAGE_EXIT           = "Exiting the game. Goodbye!";

    static
    {
        scan           = new Scanner(System.in);
        wordGame       = new WordGame();
        numberGame     = new NumberGame();
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
            showMenu();
            choice = scan.next()
                         .toUpperCase();

            switch(choice)
            {
                case CHOICE_WORD_GAME -> wordGame.play();
                case CHOICE_NUMBER_GAME -> numberGame.play();
                case CHOICE_MASTERMIND -> mastermindGame.play();
                case CHOICE_QUIT -> System.out.println(MESSAGE_EXIT);
                default -> System.out.println(MESSAGE_INVALID_CHOICE);
            }
        } while(!choice.equals(CHOICE_QUIT));
    }

    /*
     * Prints the game menu to the console.
     */
    private static void showMenu()
    {
        final StringBuilder menuBuilder;
        final String        menu;

        menuBuilder = new StringBuilder();

        menuBuilder.append("\n")
                   .append("Press ")
                   .append(CHOICE_WORD_GAME)
                   .append(MENU_WORD_GAME)
                   .append("Press ")
                   .append(CHOICE_NUMBER_GAME)
                   .append(MENU_NUMBER_GAME)
                   .append("Press ")
                   .append(CHOICE_MASTERMIND)
                   .append(MENU_MASTERMIND)
                   .append("Press ")
                   .append(CHOICE_QUIT)
                   .append(" to quit")
                   .append("\n\n")
                   .append("Enter your choice: ");

        menu = menuBuilder.toString();

        System.out.print(menu);
    }

    /*
     * Prints a welcome message to the console.
     * <p>
     * This method constructs a formatted welcome message using the predefined
     * {@value #SEPARATOR} and {@value #GREETING} constants, then displays it to the user at the
     * start of the application.
     * </p>
     */
    private static void printWelcomeMessage()
    {
        final StringBuilder welcomeBuilder;
        final String        welcomeMessage;

        welcomeBuilder = new StringBuilder();
        welcomeBuilder.append(SEPARATOR)
                      .append("\n")
                      .append(GREETING)
                      .append("\n")
                      .append(SEPARATOR);

        welcomeMessage = welcomeBuilder.toString();

        System.out.println(welcomeMessage);
    }
}
