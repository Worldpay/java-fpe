package javafpe;

/**
 * Utility methods for convenience when writing tests.
 */
public class TestUtility {

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


}
