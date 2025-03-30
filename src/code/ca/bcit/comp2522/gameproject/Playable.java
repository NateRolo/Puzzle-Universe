package ca.bcit.comp2522.gameproject;

/**
 * Represents an entity or action that can be "played" or executed.
 * This is a functional interface whose functional method is {@link #play()}.
 * 
 * @author Nathan O
 * @version 1.0 2025
 */
@FunctionalInterface
public interface Playable
{
    /**
     * Executes the primary action associated with this playable entity.
     */
    void play();
}
