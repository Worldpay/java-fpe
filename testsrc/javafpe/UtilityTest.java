package javafpe;

import static org.junit.Assert.assertArrayEquals;

import java.math.BigInteger;

import org.junit.Test;

public class UtilityTest {

	@Test
	public void testEncode() {
		// TODO These tests may be pointless as BigInteger is being sensible.  Need negative numbers to prove it maybe?
		assertArrayEquals(TestUtility.cba(0xCA, 0xFE, 0xBA, 0xBE), Utility.encode(BigInteger.valueOf(0xCAFEBABE)));
		assertArrayEquals(TestUtility.cba(0xCA, 0xFE, 0xBA), Utility.encode(BigInteger.valueOf(0x00CAFEBA)));
		assertArrayEquals(TestUtility.cba(0xCA, 0xFE), Utility.encode(BigInteger.valueOf(0x0000CAFE)));
		assertArrayEquals(TestUtility.cba(0xCA), Utility.encode(BigInteger.valueOf(0x000000CA)));
		assertArrayEquals(new byte[0], Utility.encode(BigInteger.valueOf(0)));
	}

	@Test
	public void testReverse() {
		assertArrayEquals(new byte[0], Utility.reverse(new byte[0]));
		assertArrayEquals(TestUtility.cba(0xCA, 0xFE, 0xBA, 0xBE), Utility.reverse(TestUtility.cba(0xBE, 0xBA, 0xFE, 0xCA)));
		assertArrayEquals(TestUtility.cba(0xCA), Utility.reverse(TestUtility.cba(0xCA)));
	}

	@Test
	public void testToBELEBytes() {
		assertArrayEquals(TestUtility.cba(0xCA, 0xFE, 0xBA, 0xBE), Utility.toBEBytes(0xCAFEBABE));
		assertArrayEquals(TestUtility.cba(0xBE, 0xBA, 0xFE, 0xCA), Utility.toLEBytes(0xCAFEBABE));
		assertArrayEquals(TestUtility.cba(0xFF, 0xEE, 0xDD, 0xCC), Utility.toBEBytes(0xFFEEDDCC));
		assertArrayEquals(TestUtility.cba(0xFF, 0xEE, 0xDD, 0xCC), Utility.toLEBytes(0xCCDDEEFF));
	}

}
