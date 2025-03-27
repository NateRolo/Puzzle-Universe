package ca.bcit.comp2522.gameproject.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the secret code that players must guess in Mastermind.
 * <p>
 * This class extends the base Code class and provides functionality to generate
 * random secret codes of specified lengths.
 * </p>
 *
 * @author Nathan O
 * @version 1.0 2025
 */
final class SecretCode extends Code
{
    private static final Random RANDOM = new Random();

    /**
     * Constructs a new SecretCode with the specified digits.
     *
     * @param digits the sequence of digits that make up the code
     */
    SecretCode(final List<Integer> digits)
    {
        super(digits);
    }

    /**
     * Generates a random secret code of the specified length.
     *
     * @param length the desired length of the secret code
     * @return a new SecretCode with random digits
     */
    static SecretCode generateRandomCode(final int length)
    {
        if (length != CODE_LENGTH)
        {
            throw new IllegalArgumentException("Code length must be 4");
        }

        final List<Integer> randomDigits;
        
        randomDigits = new ArrayList<>();
        for (int i = 0; i < length; i++)
        {
            randomDigits.add(RANDOM.nextInt(MAX_DIGIT) + MIN_DIGIT);
        }

        return new SecretCode(randomDigits);
    }
}
