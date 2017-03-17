package javafpe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link BitMap} class.
 */
public class BitMapTest {

	/**
	 * Simple functional test of a bitmap.
	 */
	@Test
	public void testBitMap() {

		int size = 65;
		BitMap map = new BitMap(size);
		for (int i = 0; i < size; i += 2) {
			assertEquals(false, map.testAndSet(i));
		}

		for (int i = 0; i < size; i += 2) {
			assertEquals(true, map.testAndSet(i));
		}

		for (int i = 1; i < size; i += 2) {
			assertEquals(false, map.testAndSet(i));
		}

		for (int i = 1; i < size; i += 2) {
			assertEquals(true, map.testAndSet(i));
		}
	}

	/**
	 * Make sure out of range values passed to the bitmap are rejected.
	 */
	@Test
	public void testOutOfRangeValues() {
		for (int i = 0; i < 1000; i++) {
			BitMap map = new BitMap(i);
			try {
				map.testAndSet(i + 1);
				fail("Expected IllegalArgumentException but didn't get it");
			} catch (IllegalArgumentException iae) {
				// As expedcted
			}
			try {
				map.testAndSet(i + 64);
				fail("Expected IllegalArgumentException but didn't get it");
			} catch (IllegalArgumentException iae) {
				// As expedcted
			}
		}
	}

	/**
	 * Test that passing -1 to a BitMap is rejected.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMinusOne() {
		BitMap map = new BitMap(5);
		map.testAndSet(-1);
	}

}
