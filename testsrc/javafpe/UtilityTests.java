package javafpe;

import static org.junit.Assert.assertArrayEquals;

import java.math.BigInteger;

import org.junit.Test;

public class UtilityTests {

	/**
	 * Creates a byte array from the integers specified, used to conveniently create byte[] instances.
	 *
	 * @param ints values to put in to the byte array
	 * @return a byte array
	 */
	private byte[] cba(int... ints) {
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

	@Test
	public void testEncode() {
		assertArrayEquals(this.cba(0xBE, 0xBA, 0xFE, 0xCA), Utility.encode(BigInteger.valueOf(0xCAFEBABE)));
		assertArrayEquals(this.cba(0xBA, 0xFE, 0xCA), Utility.encode(BigInteger.valueOf(0xCAFEBA00)));
		assertArrayEquals(this.cba(0xFE, 0xCA), Utility.encode(BigInteger.valueOf(0xCAFE0000)));
		assertArrayEquals(this.cba(0xCA), Utility.encode(BigInteger.valueOf(0xCA000000)));
		assertArrayEquals(new byte[0], Utility.encode(BigInteger.valueOf(0)));
	}

	@Test
	public void testReverse() {
		assertArrayEquals(new byte[0], Utility.reverse(new byte[0]));
		assertArrayEquals(this.cba(0xCA, 0xFE, 0xBA, 0xBE), Utility.reverse(this.cba(0xBE, 0xBA, 0xFE, 0xCA)));
		assertArrayEquals(this.cba(0xCA), Utility.reverse(this.cba(0xCA)));
	}

	@Test
	public void testToBELEBytes() {
		assertArrayEquals(this.cba(0xCA, 0xFE, 0xBA, 0xBE), Utility.toBEBytes(0xCAFEBABE));
		assertArrayEquals(this.cba(0xBE, 0xBA, 0xFE, 0xCA), Utility.toLEBytes(0xCAFEBABE));
		assertArrayEquals(this.cba(0xFF, 0xEE, 0xDD, 0xCC), Utility.toBEBytes(0xFFEEDDCC));
		assertArrayEquals(this.cba(0xFF, 0xEE, 0xDD, 0xCC), Utility.toLEBytes(0xCCDDEEFF));
	}

}
