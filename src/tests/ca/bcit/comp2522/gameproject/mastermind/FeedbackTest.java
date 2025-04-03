package ca.bcit.comp2522.gameproject.mastermind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    private Code          guessCode;
    private TestCase      test;
    private Feedback      feedback;

    private String listToString(final List<Integer> list)
    {
        return list.stream()
                   .map(String::valueOf)
                   .collect(Collectors.joining());
    }

    @BeforeEach
    public void setup()
    {
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        guess      = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        final String guessString = listToString(guess);
        try
        {
            guessCode = PlayerGuessCode.fromInput(guessString);
        }
        catch(final InvalidGuessException e)
        {
            fail("Setup failed for default guess: " + e.getMessage());
        }
    }

    @Test
    public void testPerfectMatchFeedback()
    {
        test = new TestCase(secret,
                            guess,
                            4,
                            0);

        feedback = new Feedback(secretCode,
                                guessCode);

        assertEquals(test.expectedExact,
                     feedback.getCorrectPositionCount(),
                     "Should have 4 exact matches for identical code");
        assertEquals(test.expectedPartial,
                     feedback.getMisplacedCount(),
                     "Should have 0 partial matches for identical code");
    }

    @Test
    public void testPartialAndExactMatchesFeedback()
    {
        guess = Arrays.asList(1,
                              2,
                              4,
                              3);
        final String guessString = listToString(guess);
        try
        {
            guessCode = PlayerGuessCode.fromInput(guessString);
        }
        catch(final InvalidGuessException e)
        {
            fail("Test failed during guess code creation: " + e.getMessage());
        }

        test = new TestCase(secret,
                            guess,
                            2,
                            2);

        feedback = new Feedback(secretCode,
                                guessCode);

        assertEquals(test.expectedExact,
                     feedback.getCorrectPositionCount(),
                     "Should have 2 exact matches for first two digits");
        assertEquals(test.expectedPartial,
                     feedback.getMisplacedCount(),
                     "Should have 2 partial matches for swapped last digits");
    }

    @Test
    public void testAllPartialMatchesWithDuplicatesFeedback()
    {
        secret     = Arrays.asList(1,
                                   1,
                                   2,
                                   2);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(2,
                                   2,
                                   1,
                                   1);
        final String guessString = listToString(guess);
        try
        {
            guessCode = PlayerGuessCode.fromInput(guessString);
        }
        catch(final InvalidGuessException e)
        {
            fail("Test failed during guess code creation: " + e.getMessage());
        }

        test = new TestCase(secret,
                            guess,
                            0,
                            4);

        feedback = new Feedback(secretCode,
                                guessCode);

        assertEquals(test.expectedExact,
                     feedback.getCorrectPositionCount(),
                     "Should have 0 exact matches when all positions are wrong");
        assertEquals(test.expectedPartial,
                     feedback.getMisplacedCount(),
                     "Should have 4 partial matches with swapped pairs");
    }

    @Test
    public void testNoMatchesFeedback()
    {
        guess = Arrays.asList(5,
                              6,
                              5,
                              6);
        final String guessString = listToString(guess);
        try
        {
            guessCode = PlayerGuessCode.fromInput(guessString);
        }
        catch(final InvalidGuessException e)
        {
            fail("Test failed during guess code creation: " + e.getMessage());
        }

        test = new TestCase(secret,
                            guess,
                            0,
                            0);

        feedback = new Feedback(secretCode,
                                guessCode);

        assertEquals(test.expectedExact,
                     feedback.getCorrectPositionCount(),
                     "Should have 0 exact matches when all digits are different");
        assertEquals(test.expectedPartial,
                     feedback.getMisplacedCount(),
                     "Should have 0 partial matches when all digits are different");
    }

    private static class TestCase
    {
        private final int expectedExact;
        private final int expectedPartial;

        public TestCase(final List<Integer> secret,
                        final List<Integer> guess,
                        final int expectedExact,
                        final int expectedPartial)
        {
            this.expectedExact   = expectedExact;
            this.expectedPartial = expectedPartial;
        }
    }
}

