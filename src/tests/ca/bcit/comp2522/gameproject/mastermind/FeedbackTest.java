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
    private Feedback      feedback;

    private String listToString(final List<Integer> list)
    {
        return list.stream()
                   .map(String::valueOf)
                   .collect(Collectors.joining());
    }

    @Test
    public void testPerfectMatchFeedback_Case1_Identical()
    {
        final String guessString1;
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        guess      = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        
        guessString1 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString1);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(4,
                     feedback.getCorrectPositionCount(),
                     "Case 1: Should have 4 exact matches for identical code");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 1: Should have 0 partial matches for identical code");
    }

    @Test
    public void testPerfectMatchFeedback_Case2_Different()
    {
        final String guessString2;
        secret     = Arrays.asList(5,
                                   6,
                                   5,
                                   6);
        guess      = Arrays.asList(5,
                                   6,
                                   5,
                                   6);
        secretCode = new SecretCode(secret);
        
        guessString2 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString2);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(4,
                     feedback.getCorrectPositionCount(),
                     "Case 2: Should have 4 exact matches for [5,6,7,8]");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 2: Should have 0 partial matches for [5,6,7,8]");
    }

    @Test
    public void testPerfectMatchFeedback_Case3_Duplicates()
    {
        final String guessString3;
        secret     = Arrays.asList(1,
                                   1,
                                   1,
                                   1);
        guess      = Arrays.asList(1,
                                   1,
                                   1,
                                   1);
        secretCode = new SecretCode(secret);
        
        guessString3 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString3);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(4,
                     feedback.getCorrectPositionCount(),
                     "Case 3: Should have 4 exact matches for [1,1,1,1]");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 3: Should have 0 partial matches for [1,1,1,1]");
    }

    @Test
    public void testPartialAndExactMatchesFeedback_Case1_SwapLastTwo()
    {
        final String guessString1;
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(1,
                                   2,
                                   4,
                                   3);
        
        guessString1 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString1);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Case 1: Should have 2 exact matches for [1,2,4,3]");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Case 1: Should have 2 partial matches for [1,2,4,3]");

    }

    @Test
    public void testPartialAndExactMatchesFeedback_Case2_SwapMiddleTwo()
    {
        final String guessString2;
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(1,
                                   3,
                                   2,
                                   4);
        
        guessString2 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString2);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Case 2: Should have 2 exact matches for [1,3,2,4]");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Case 2: Should have 2 partial matches for [1,3,2,4]");

    }

    @Test
    public void testPartialAndExactMatchesFeedback_Case3_SwapOuterTwo()
    {
        final String guessString3;
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(4,
                                   2,
                                   3,
                                   1);
        
        guessString3 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString3);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(2,
                     feedback.getCorrectPositionCount(),
                     "Case 3: Should have 2 exact matches for [4,2,3,1]");
        assertEquals(2,
                     feedback.getMisplacedCount(),
                     "Case 3: Should have 2 partial matches for [4,2,3,1]");

    }

    @Test
    public void testAllPartialMatchesWithDuplicates_Case1_PairSwap()
    {
        final String guessString1;
        secret     = Arrays.asList(1,
                                   1,
                                   2,
                                   2);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(2,
                                   2,
                                   1,
                                   1);
        
        guessString1 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString1);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 1: Should have 0 exact matches for [2,2,1,1] vs [1,1,2,2]");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Case 1: Should have 4 partial matches for [2,2,1,1] vs [1,1,2,2]");

    }

    @Test
    public void testAllPartialMatchesWithDuplicates_Case2_InterleavedSwap()
    {
        final String guessString2;
        secret     = Arrays.asList(1,
                                   2,
                                   1,
                                   2);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(2,
                                   1,
                                   2,
                                   1);
        
        guessString2 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString2);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 2: Should have 0 exact matches for [2,1,2,1] vs [1,2,1,2]");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Case 2: Should have 4 partial matches for [2,1,2,1] vs [1,2,1,2]");

    }

    @Test
    void testAllPartialMatchesWithDuplicates_Case3_DifferentValuesSwap()
    {
        final String guessString3;
        secret     = Arrays.asList(3,
                                   4,
                                   4,
                                   3);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(4,
                                   3,
                                   3,
                                   4);
        
        guessString3 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString3);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 3: Should have 0 exact matches for [4,3,3,4] vs [3,4,4,3]");
        assertEquals(4,
                     feedback.getMisplacedCount(),
                     "Case 3: Should have 4 partial matches for [4,3,3,4] vs [3,4,4,3]");

    }

    @Test
    public void testNoMatchesFeedback_Case1_CompletelyDifferent()
    {
        final String guessString1;
        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(5,
                                   6,
                                   5,
                                   6);
        guessString1 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString1);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 1: Should have 0 exact matches for [5,6,5,6] vs [1,2,3,4]");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 1: Should have 0 partial matches for [5,6,5,6] vs [1,2,3,4]");

    }

    @Test
    public void testNoMatchesFeedback_Case2_DifferentValues()
    {
        final String guessString2;

        secret     = Arrays.asList(1,
                                   2,
                                   3,
                                   4);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(8,
                                   7,
                                   6,
                                   5);
        
        guessString2 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString2);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 2: Should have 0 exact matches for [8,7,6,5] vs [1,2,3,4]");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 2: Should have 0 partial matches for [8,7,6,5] vs [1,2,3,4]");
    }

    @Test
    public void testNoMatchesFeedback_Case3_DuplicatesNoMatch()
    {
        final String guessString3;
        secret     = Arrays.asList(1,
                                   1,
                                   2,
                                   2);
        secretCode = new SecretCode(secret);
        guess      = Arrays.asList(3,
                                   3,
                                   4,
                                   4);
        
        guessString3 = listToString(guess);

        guessCode = PlayerGuessCode.fromInput(guessString3);
        feedback  = new Feedback(secretCode,
                                 guessCode);
        assertEquals(0,
                     feedback.getCorrectPositionCount(),
                     "Case 3: Should have 0 exact matches for [3,3,4,4] vs [1,1,2,2]");
        assertEquals(0,
                     feedback.getMisplacedCount(),
                     "Case 3: Should have 0 partial matches for [3,3,4,4] vs [1,1,2,2]");
    }
}

