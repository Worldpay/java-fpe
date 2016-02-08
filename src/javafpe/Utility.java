package javafpe;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility methods that swap between BigInteger, Java primitives and byte array representations of numbers.
 */
public class Utility {

	/**
	 * Converts the passed big integer to a byte array, stripping off all the leading zero bytes.
	 *
	 * @param n the number to encode.
	 * @return a byte array, possibly zero length (if n==0), never null.
	 */
	static byte[] encode(BigInteger n) {
		byte[] encodedN;
		if (n.signum() == 0) {
			// If n is zero, return an zero-length byte array
			encodedN = new byte[0];
		} else {
			// No need to reverse as C# original does at this point, as Java is always Big Endian.
			byte[] nAsByteArray = n.toByteArray();
			int firstNonZeroIndex = 0;
			while ((nAsByteArray[firstNonZeroIndex] == 0) && (firstNonZeroIndex < nAsByteArray.length)) {
				firstNonZeroIndex++;
			}
			// Finally copy the non-zero bytes in to a new array
			encodedN = new byte[nAsByteArray.length - firstNonZeroIndex];
			System.arraycopy(nAsByteArray, firstNonZeroIndex, encodedN, 0, nAsByteArray.length - firstNonZeroIndex);
		}

		return encodedN;
	}

	/**
	 * Returns a copy of the passed array with the order of bytes reversed.
	 *
	 * @param source the byte array to reverse.
	 * @return the reversed version of the source array.
	 */
	public static byte[] reverse(byte[] source) {
		int len = source.length;
		byte[] destination = new byte[len];
		for (int i = 0; i < len; i++) {
			destination[len - i - 1] = source[i];
		}
		return destination;
	}

	/**
	 * Returns a byte array representing the 32-bit integer passed in a 4 element byte array in big-endian form.
	 *
	 * @param i the integer to create the byte array for
	 * @return the 4 element byte array. Never null.
	 */
	static byte[] toBEBytes(int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
	}

	/**
	 * Returns a byte array representing the 32-bit integer passed in a 4 element byte array in little-endian form.
	 *
	 * @param i the integer to create the byte array for
	 * @return the 4 element byte array. Never null.
	 */
	public static byte[] toLEBytes(int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
	}

	/**
	 * Prevents construction of a utility class.
	 */
	private Utility() {
	}

}
