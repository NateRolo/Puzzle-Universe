package ca.bcit.comp2522.gameproject.mastermind;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

/**
 * Test class for Mastermind game components.
 *
 * @author Nathan O
 */
public class MastermindTest
{
    @Test
    public void testSecretCodeLength()
    {
        final int tooShortLength = 3;
        final int tooLongLength  = 7;
        final int validLength    = 4;

        // Test length too short
        IllegalArgumentException shortException = assertThrows(IllegalArgumentException.class,
                                                               () -> SecretCode.generateRandomCode(tooShortLength));

        // Test valid length should not throw
        assertDoesNotThrow(() -> SecretCode.generateRandomCode(validLength));

        // Verify the generated code has correct length
        final SecretCode validCode = SecretCode.generateRandomCode(validLength);
        assertTrue(validCode.getDigits()
                            .toString()
                            .length() >= validLength);
    }

    @Test
    public void testSecretCodeValidDigits()
    {
        final List<Integer> smallCodeDigits = Arrays.asList(0,
                                                            0,
                                                            0,
                                                            0);
        final List<Integer> largeCodeDigits = Arrays.asList(6,
                                                            7,
                                                            9,
                                                            10);

        IllegalArgumentException digitsTooSmallException = assertThrows(IllegalArgumentException.class,
                                                                        () -> new SecretCode(smallCodeDigits));

        IllegalArgumentException digitsTooLargeException = assertThrows(IllegalArgumentException.class,
                                                                        () -> new SecretCode(largeCodeDigits));
    }
}

