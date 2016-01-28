package javafpe;

/**
 * Miscellaneous methods that help with logging.
 */
public class LoggingHelper {

	/**
	 * Set of hexadecimal digits, used in {@link #bytesToHex(byte[])}.
	 */
	 private static final char[] HEXADECIMAL_DIGITS = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts a byte array in to a stream of hexadecimal digits, useful for logging.
	 *
	 * @param bytes the byte array to convert.
	 * @return a string, of length bytes.length*2, containing the hexadecimal digit equivalent for each byte in the passed array. Never returns null. Returns
	 *         empty string on null or zero-length array.
	 */
	public static String bytesToHex(byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return "";
		}
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEXADECIMAL_DIGITS[v >>> 4];
			hexChars[(j * 2) + 1] = HEXADECIMAL_DIGITS[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Prevents construction of a utility class.
	 */
	private LoggingHelper() {
	}

}
