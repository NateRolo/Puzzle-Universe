package ca.bcit.comp2522.gameproject.mastermind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Mastermind game components.
 *
 * @author Nathan O
 */
public class FeedbackTest
{
    private List<Integer> secret;
    private List<Integer> guess;
    private SecretCode    secretCode;
    private Code         guessCode;
    private TestCase     test;
    private Feedback     feedback;

    @BeforeEach
    public void setup()
    {
        // Initialize with default values that will be overwritten in specific tests
        secret     = Arrays.asList(1, 2, 3, 4);
        guess      = Arrays.asList(1, 2, 3, 4);
        secretCode = new SecretCode(secret);
        guessCode  = new PlayerGuessCode(guess);
    }

    @Test
    public void testPerfectMatchFeedback()
    {
        // Using default setup values
        test     = new TestCase(secret,
                               guess,
                               4,
                               0);
        feedback = new Feedback(test.expectedExact,
                               test.expectedPartial,
                               false);
        
        assertEquals(4,
                     feedback.getCorrectPositionCount(),
                     "Should have 4 exact matches for identical code");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Should have 0 partial matches for identical code");
    }

    @Test
    public void testPartialAndExactMatchesFeedback()
    {
        guess      = Arrays.asList(1, 2, 4, 3);
        guessCode  = new PlayerGuessCode(guess);
        test       = new TestCase(secret,
                                 guess,
                                 2,
                                 2);
        feedback   = new Feedback(test.expectedExact,
                                 test.expectedPartial,
                                 false);

        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Should have 2 exact matches for first two digits");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Should have 2 partial matches for swapped last digits");
    }

    @Test
    public void testAllPartialMatchesWithDuplicatesFeedback()
    {
        secret     = Arrays.asList(1, 1, 2, 2);
        guess      = Arrays.asList(2, 2, 1, 1);
        secretCode = new SecretCode(secret);
        guessCode  = new PlayerGuessCode(guess);
        test       = new TestCase(secret,
                                 guess,
                                 0,
                                 4);
        feedback   = new Feedback(test.expectedExact,
                                 test.expectedPartial,
                                 false);

        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Should have 0 exact matches when all positions are wrong");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Should have 4 partial matches with swapped pairs");
    }

    @Test
    public void testNoMatchesFeedback()
    {
        guess      = Arrays.asList(5, 6, 7, 8);
        test       = new TestCase(secret,
                                 guess,
                                 0,
                                 0);
        feedback   = new Feedback(test.expectedExact,
                                 test.expectedPartial,
                                 false);

        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Should have 0 exact matches when all digits are different");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Should have 0 partial matches when all digits are different");
    }

    // Helper class for organizing test cases
    private static class TestCase
    {
        private final List<Integer> secret;
        private final List<Integer> guess;
        private final int           expectedExact;
        private final int           expectedPartial;

        public TestCase(final List<Integer> secret,
                        final List<Integer> guess,
                        final int expectedExact,
                        final int expectedPartial)
        {
            this.secret          = secret;
            this.guess           = guess;
            this.expectedExact   = expectedExact;
            this.expectedPartial = expectedPartial;
        }
    }
}

