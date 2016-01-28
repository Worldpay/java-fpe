package javafpe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FE1Tests {

	private static final Logger LOGGER = LoggerFactory.getLogger("javafpe.FE1Tests");

	private static final byte[] TEST_IV = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04 };

	private static final byte[] TEST_KEY = { 0x20, 0x01, 0x30, 0x50, 0x60, 0x70 };

	private long roundTripTest(BigInteger modulus, long plainText, byte[] key, byte[] iv) throws FPEException {
		assertEquals("Cannot encrypt number(" + plainText + ") that is bigger than modulus (" + modulus + "), bug in test!", 1,
						modulus.compareTo(BigInteger.valueOf(plainText)));
		BigInteger plainTextBigInteger = BigInteger.valueOf(plainText);
		BigInteger cipherText = FE1.encrypt(modulus, plainTextBigInteger, key, iv);
		BigInteger decryptedText = FE1.decrypt(modulus, cipherText, key, iv);

		LOGGER.info("Round trip: {} -> {} -> {}", plainTextBigInteger, cipherText, decryptedText);

		// assertNotEquals(plainTextBigInteger, cipherText); // Not a valid test as X encrypted could happen to be X!

		// Ensure that the encrypted number doesn't fall outside the range of 0..modulus
		assertEquals(-1, cipherText.compareTo(modulus));
		assertEquals(true, cipherText.signum() > -1);

		assertEquals(plainTextBigInteger, decryptedText);

		return cipherText.longValue();
	}

	/**
	 * Tests creation of every element of a small range (1000) with the same key and IV to ensure no clashes.
	 * @throws FPEException if any problems occur (fails test).
	 */
	@Test
	public void testMultipleRoundTrips() throws FPEException {
		int range = 1000;
		BigInteger modulus = BigInteger.valueOf(range);

		Set<Long> foundNumbers = new HashSet<Long>(range);
		for (int i = 0; i < range; i++) {
			long encryptedValue = roundTripTest(modulus, i, TEST_KEY, TEST_IV);
			if (foundNumbers.contains(encryptedValue)) {
				fail(String.format("Encrypted %d and got %d which already was generated (out of %d values)", i, encryptedValue, foundNumbers.size()));
			}
			foundNumbers.add(encryptedValue);
		}
	}

}
