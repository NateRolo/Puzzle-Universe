package ca.bcit.comp2522.gameproject;

/**
 * Represents games that can be played repeatedly, one game at a time.
 * Extends the basic {@link Playable} interface with the capability
 * to initiate a single game session.
 *
 * @author Nathan O
 */
public interface Replayable extends Playable
{
    /**
     * Executes a single session or round of the game.
     */
    void playOneGame();
}
