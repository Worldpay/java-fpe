package javafpe;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

/**
 * Utility methods for convenience when writing tests.
 */
public class TestUtility {

	/**
	 * Hides constructor for utility class.
	 */
	private TestUtility() {
		
	}
	
	/**
	 * Creates a byte array from the integers specified, used to conveniently create byte[] instances.
	 *
	 * @param ints values to put in to the byte array
	 * @return a byte array
	 */
	public static byte[] cba(int... ints) {
		byte[] bytes;
		if ((ints == null) || (ints.length == 0)) {
			bytes = new byte[0];
		} else {
			bytes = new byte[ints.length];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) ints[i];
			}
		}
		return bytes;
	}

	/**
	 * Converts a byte array to a human readable string of hexadecimal digits.
	 * @param a the byte array to convert.  Must not be null, may be zero length.
	 * @return a string containing the hex digits taken from the byte array (leading zero always included where required).
	 */
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	/**
	 * Tests a single round trip of encrypting a value then decrypting it again and ensuring that the original value is returned.
	 * @param modulus the modulus.
	 * @param plainText the value to round-trip.
	 * @param key the key to encrypt with.
	 * @param iv the tweak.
	 * @return the encrypted value, just in case it's useful.
	 * @throws FPEException if anything goes wrong.
	 */
	public static long roundTripTest(BigInteger modulus, long plainText, byte[] key, byte[] iv) throws FPEException {
		assertEquals("Cannot encrypt number(" + plainText + ") that is bigger than modulusBI (" + modulus + "), bug in test!", 1,
						modulus.compareTo(BigInteger.valueOf(plainText)));
		BigInteger plainTextBigInteger = BigInteger.valueOf(plainText);
		BigInteger cipherText = new FE1().encrypt(modulus, plainTextBigInteger, key, iv);
		BigInteger decryptedText = new FE1().decrypt(modulus, cipherText, key, iv);

		// LOGGER.info("Round trip: {} -> {} -> {}", plainTextBigInteger, cipherText, decryptedText);

		// assertNotEquals(plainTextBigInteger, cipherText); // Not a valid test as X encrypted could happen to be X!

		// Ensure that the encrypted number doesn't fall outside the range of 0..modulus
		assertEquals(-1, cipherText.compareTo(modulus));
		assertEquals(true, cipherText.signum() > -1);

		assertEquals(plainTextBigInteger, decryptedText);

		return cipherText.longValue();
	}


}
