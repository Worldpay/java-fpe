package javafpe;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.junit.Test;

/**
 * Credit Card tests are "incomplete" tests, in that there's no automated assertion here. They're included to easily check compatibility with the Botan toolkit
 * (<a href="http://botan.randombit.net/">http://botan.randombit.net/</a>). This class contains code that behaves like the "cc_encrypt" sample included with
 * Botan. See <a href="http://botan.randombit.net/manual/javafpe.html">http://botan.randombit.net/manual/javafpe.html</a>, so you can check that this
 * implementation is interoperable with Botan.
 */
public class BotanCCTests {

	/**
	 * Used to convert a string of hexadecimal digits in to an array of bytes.
	 *
	 * @param hexadecimalString a String of hexadecimal digits.
	 * @return a byte array where each value is the pair of hexadecimal characters in the passed string.
	 */
	public static byte[] hexStringToByteArray(String hexadecimalString) {
		int len = hexadecimalString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexadecimalString.charAt(i), 16) << 4) + Character.digit(hexadecimalString.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Calculate the Luhn check digit for the passed string representation of a number.
	 *
	 * @param number string of digits in string form (e.g. "123456790"), must not be null.
	 * @return the Luhn check digit for the passed number.
	 */
	private int calculateLuhnCheckDigit(String number) {

		// Get the sum of all the digits, however we need to replace the value
		// of the first digit, and every other digit, with the same digit
		// multiplied by 2. If this multiplication yields a number greater
		// than 9, then add the two digits together to get a single digit
		// number.
		//
		// The digits we need to replace will be those in an even position for
		// card numbers whose length is an even number, or those is an odd
		// position for card numbers whose length is an odd number. This is
		// because the Luhn algorithm reverses the card number, and doubles
		// every other number starting from the second number from the last
		// position.
		int sum = 0;
		for (int i = 0; i < number.length(); i++) {

			// Get the digit at the current position.
			int digit = Integer.parseInt(number.substring(i, (i + 1)));

			if ((i % 2) == 0) {
				digit = digit * 2;
				if (digit > 9) {
					digit = (digit / 10) + (digit % 10);
				}
			}
			sum += digit;
		}

		// The check digit is the number required to make the sum a multiple of
		// 10.
		int mod = sum % 10;
		return ((mod == 0) ? 0 : 10 - mod);
	}

	@Test
	public void encryptCCNumber() throws Exception {
		final String password = "passw0rd";
		final String tweakString = "0102030405AA";
		final String ccBaseNumber = "543443295325432";
		final String ccNumber = ccBaseNumber + this.calculateLuhnCheckDigit(ccBaseNumber);

		byte[] tweak = hexStringToByteArray(tweakString);
		int rounds = 100000;
		int derivedKeyLength = 32;
		byte[] encodedPassword = this.getEncryptedPassword(password, tweak, rounds, derivedKeyLength);

		BigInteger modulus = BigInteger.valueOf(1000000000000000L);

		BigInteger ccAsBigInt = new BigInteger(ccBaseNumber);

		BigInteger encryptedCCNumber = new FE1().encrypt(modulus, ccAsBigInt, encodedPassword, tweak);

		final String encryptedCCNumberAsString = encryptedCCNumber.toString();
		System.out.println(String.format("Run the following command on boton:\nbotan cc_encrypt %s %s --tweak=%s", ccNumber, password, tweakString));
		System.out.println(String.format("\nIf this works, then you will be returned the number: %s",
						encryptedCCNumberAsString + this.calculateLuhnCheckDigit(encryptedCCNumberAsString)));
	}

	private byte[] getEncryptedPassword(String password, byte[] salt, int iterations, int derivedKeyLength) throws Exception {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);

		SecretKeyFactory f;
		final String keyFactoryName = "PBKDF2WithHmacSHA256";
		try {
			f = SecretKeyFactory.getInstance(keyFactoryName);
			return f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			fail("Unable to initialise SecretKeyFactory for " + keyFactoryName + ".  Either use Java 8 or BouncyCastle");
			throw e;
		} catch (InvalidKeySpecException e) {
			fail("Invalid key spec for input \"" + password + "\".  This indicates a fault in the test code itself");
			throw e;
		}

	}
}
