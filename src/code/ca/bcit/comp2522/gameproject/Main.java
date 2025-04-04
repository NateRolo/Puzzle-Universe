package ca.bcit.comp2522.gameproject;

import ca.bcit.comp2522.gameproject.mastermind.MastermindGame;
import ca.bcit.comp2522.gameproject.numbergame.NumberGame;
import ca.bcit.comp2522.gameproject.wordgame.WordGame;
import java.util.Scanner;

/**
 * Main class for the game project.
 * <p>
 * This class provides a simple text-based menu that allows the user to choose
 * from different games.
 * The menu runs in an infinite loop until the user chooses to quit by pressing
 * 'Q' or 'q'.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
public final class Main
{
    private static final Scanner  scan;
    private static final Playable wordGame;
    private static final Playable numberGame;
    private static final Playable mastermindGame;

    private static final String CHOICE_WORD_GAME   = "W";
    private static final String CHOICE_NUMBER_GAME = "N";
    private static final String CHOICE_MASTERMIND  = "M";
    private static final String CHOICE_QUIT        = "Q";

    private static final String MENU_WORD_GAME = " to play the Word Game\n";
    private static final String MENU_NUMBER_GAME = " to play the Number Game\n";
    private static final String MENU_MASTERMIND = " to play Mastermind - Deception\n";

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
        final String menu;

        menuBuilder = new StringBuilder();

        menuBuilder.append("Press ")
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
                    .append(" to quit");

        menu = menuBuilder.toString();

        System.out.println(menu);
    }

    /*
     * Prints a welcome message to the console.
     */
    private static void printWelcomeMessage()
    {
        System.out.println("""
            
                           ----------------------------------------
                            WELCOME TO NATHAN'S GAME CORNER!
                           ----------------------------------------
                           Choose one of the games below to start playing.
                           """);
    }
}
