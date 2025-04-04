package ca.bcit.comp2522.gameproject;

/**
 * Interface for games that are played in rounds. Extends the {@link Replayable} interface.
 * 
 * @author Nathan O
 * @version 1.0 2025
 */
public interface RoundBased extends Replayable
{
    /**
     * Plays one round of the game.
     */
    public void playOneRound();

}
