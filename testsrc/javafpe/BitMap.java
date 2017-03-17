package javafpe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for checking that encrypting a unique input value yields a unique output value. Each potential outcome is represented as a single bit. By using
 * {@link BitMap#testAndSet(long)} you can test and set any particular value.
 */
public class BitMap {

	private static final Logger LOGGER = LoggerFactory.getLogger("javafpe.BitMap");

	private final long[] bitMap;
	private static final int PRIMITIVE_SIZE = Long.SIZE;
	private final int capacity;
	private Object arrayLock = new Object();

	/**
	 * Create a bitmap with a specific capacity.
	 * 
	 * @param capacity 0 < capacity < Integer.MAX_VALUE. This may not be changed once instantiated.
	 */
	public BitMap(int capacity) {
		this.capacity = capacity;
		int numOfPrimitives = capacity / PRIMITIVE_SIZE;
		if (capacity % PRIMITIVE_SIZE > 0) {
			numOfPrimitives++;
		}
		bitMap = new long[numOfPrimitives];
		LOGGER.info("Created a bitmap of {} longs to hold {} values", numOfPrimitives, capacity);
	}

	/**
	 * Tests and sets a specific value in the bitmap.
	 * 
	 * @param value the value to set.
	 * @return true if the value has been set before, false otherwise.
	 */
	public boolean testAndSet(long value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value(" + value + ") must be 0 <= value <= " + capacity);
		}
		if (value > capacity) {
			throw new IllegalArgumentException("Value must not exceed capacity: " + capacity);
		}
		int index = (int) (value / PRIMITIVE_SIZE);
		int offset = (int) (value % PRIMITIVE_SIZE);
		long offsetMask = 1L << offset;

		boolean result;
		synchronized (arrayLock) {
			if ((bitMap[index] & offsetMask) != 0) {
				result = true;
			} else {
				result = false;
				bitMap[index] |= offsetMask;
			}
		}
		return result;

	}

}
