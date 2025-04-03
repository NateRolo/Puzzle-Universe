package ca.bcit.comp2522.gameproject.mastermind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Feedback logic in Mastermind.
 *
 * @author Nathan O
 */
public class FeedbackTest
{
    private SecretCode secretCode;
    private Code       guessCode;
    private Feedback   feedback;

    @BeforeEach
    public void setup()
    {
        // Setup basic codes for tests, can be overridden in specific tests
        secretCode = new SecretCode(Arrays.asList(1, 2, 3, 4));
        guessCode  = PlayerGuessCode.fromInput("1234");
    }

    @Test
    public void testPerfectMatchFeedback()
    {
        // Uses @BeforeEach setup: secret=[1,2,3,4], guess=[1,2,3,4]
        feedback = new Feedback(secretCode, guessCode);

        assertEquals(4,
                     feedback.getCorrectPositionCount(),
                     "Exact:4, Partial:0 -> Should have 4 exact matches for identical code");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Exact:4, Partial:0 -> Should have 0 partial matches for identical code");
    }

    @Test
    public void testNoMatchesFeedback()
    {
        // Secret: [1,2,3,4]
        guessCode = PlayerGuessCode.fromInput("5656");
        feedback  = new Feedback(secretCode, guessCode);

        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Exact:0, Partial:0 -> Should have 0 exact matches when all digits are different");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Exact:0, Partial:0 -> Should have 0 partial matches when all digits are different");
    }

    @Test
    public void testPartialAndExactMatchesFeedback()
    {
        // Secret: [1,2,3,4]
        guessCode = PlayerGuessCode.fromInput("1243");
        feedback  = new Feedback(secretCode, guessCode);

        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Exact:2, Partial:2 -> Should have 2 exact matches for first two digits");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Exact:2, Partial:2 -> Should have 2 partial matches for swapped last digits");
    }

    @Test
    public void testAllPartialMatchesFeedback()
    {
        // Secret: [1,2,3,4]
        guessCode = PlayerGuessCode.fromInput("4321");
        feedback  = new Feedback(secretCode, guessCode);

        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Exact:0, Partial:4 -> Should have 0 exact matches");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Exact:0, Partial:4 -> Should have 4 partial matches");
    }

    @Test
    public void testDuplicatesInSecretAndGuessFeedback_Case1()
    {
        secretCode = new SecretCode(Arrays.asList(1, 1, 2, 3));
        guessCode  = PlayerGuessCode.fromInput("1214");
        feedback   = new Feedback(secretCode, guessCode);

        assertEquals(1,
                     feedback.getCorrectPositionCount(),
                     "Secret:[1,1,2,3], Guess:[1,2,1,4] -> Exact:1 (first 1)");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Secret:[1,1,2,3], Guess:[1,2,1,4] -> Partial:2 (second 1, 2)");
    }

    @Test
    public void testDuplicatesInSecretAndGuessFeedback_Case2()
    {
        secretCode = new SecretCode(Arrays.asList(1, 2, 1, 2));
        guessCode  = PlayerGuessCode.fromInput("1122");
        feedback   = new Feedback(secretCode, guessCode);

        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Secret:[1,2,1,2], Guess:[1,1,2,2] -> Exact:2 (first 1, last 2)");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Secret:[1,2,1,2], Guess:[1,1,2,2] -> Partial:2 (second 1, first 2)");
    }

    @Test
    public void testDuplicatesInSecretOnlyFeedback()
    {
        secretCode = new SecretCode(Arrays.asList(1, 1, 2, 2));
        guessCode  = PlayerGuessCode.fromInput("1345");
        feedback   = new Feedback(secretCode, guessCode);

        assertEquals(1,
                     feedback.getCorrectPositionCount(),
                     "Secret:[1,1,2,2], Guess:[1,3,4,5] -> Exact:1 (first 1)");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Secret:[1,1,2,2], Guess:[1,3,4,5] -> Partial:0");
    }

    @Test
    public void testDuplicatesInGuessOnlyFeedback()
    {
        secretCode = new SecretCode(Arrays.asList(1, 2, 3, 4));
        guessCode  = PlayerGuessCode.fromInput("1122");
        feedback   = new Feedback(secretCode, guessCode);

        assertEquals(1,
                     feedback.getCorrectPositionCount(),
                     "Secret:[1,2,3,4], Guess:[1,1,2,2] -> Exact:1 (first 1)");
        assertEquals(1,
                     feedback.getMisplacedCount(),
                     "Secret:[1,2,3,4], Guess:[1,1,2,2] -> Partial:1 (one 2)");
    }

     @Test
    public void testAllPartialMatchesWithDuplicatesFeedback()
    {
        secretCode = new SecretCode(Arrays.asList(1, 1, 2, 2));
        guessCode  = PlayerGuessCode.fromInput("2211");
        feedback   = new Feedback(secretCode, guessCode);

        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Secret:[1,1,2,2], Guess:[2,2,1,1] -> Exact:0");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Secret:[1,1,2,2], Guess:[2,2,1,1] -> Partial:4");
    }
}

