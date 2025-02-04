package ca.bcit.comp2522.gameproject;

import java.util.Scanner;

public class Main {
    private static final Scanner scan;

    static
    {
        scan = new Scanner(System.in);
    }

    public static void main(final String[] args)
    {
        printWelcomeMessage();
        String choice;

        do {
            printGameMenu();
            choice = scan.next()
                         .toLowerCase();

            switch (choice)
            {
                case "w" -> WordGame.play();
                case "n" -> NumberGame.play();
                case "m" -> MyGame.play();
                case "q" -> System.out.println("Exiting the game. Goodbye!");
                default -> System.out.println("Not a valid option, please select a valid game.");
            }
        } while (!choice.equals("q"));
    }

    private static void printGameMenu()
    {
        System.out.println("""
                Press W to play the Word game.
                Press N to play the Number game.
                Press M to play the <your game's name> game.
                Press Q to quit.
                """);
    }

    private static void printWelcomeMessage()
    {
        System.out.println("""
                Welcome to Nathan's Game Corner!
                Choose one of the games below to start playing.
                """);
    }
}
