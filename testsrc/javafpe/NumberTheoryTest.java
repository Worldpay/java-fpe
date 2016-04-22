package javafpe;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

public class NumberTheoryTest {

	/**
	 * "Create Factored Primes": creates an array of {@link BigInteger} instances from the array of integers passed.
	 *
	 * @param nums an array of integers
	 * @return an array with each passed integer converted to a BigInteger.
	 */
	private BigInteger[] cfp(int... nums) {
		BigInteger[] bis = new BigInteger[nums.length];
		for (int i = 0; i < nums.length; i++) {
			bis[i] = BigInteger.valueOf(nums[i]);
		}
		return bis;
	}

	@Test
	public void testCountLowZeroBits() {
		// If n<=0 always return 0
		assertEquals(0, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b0)));
		assertEquals(0, NumberTheory.countLowZeroBits(BigInteger.valueOf(-1)));
		assertEquals(0, NumberTheory.countLowZeroBits(BigInteger.valueOf(-1000)));

		assertEquals(8, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b100000000)));
		assertEquals(7, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b110000000)));
		assertEquals(0, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b11111111)));
		assertEquals(1, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b11111110)));
		assertEquals(2, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b100)));
		assertEquals(16, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b10000000000000000)));
		assertEquals(17, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b100000000000000000)));
		assertEquals(15, NumberTheory.countLowZeroBits(BigInteger.valueOf(0b1000000000000000)));
	}

	@Test
	public void testCountTrailingZeros() {
		assertEquals(8, NumberTheory.countTrailingZeros((byte) 0b0));
		assertEquals(0, NumberTheory.countTrailingZeros((byte) 0b1));
		assertEquals(1, NumberTheory.countTrailingZeros((byte) 0b11111110));
		assertEquals(6, NumberTheory.countTrailingZeros((byte) 0b11000000));
		assertEquals(4, NumberTheory.countTrailingZeros((byte) 0b00010000));
		assertEquals(1, NumberTheory.countTrailingZeros((byte) 0b10101010));
	}

	@Test
	public void testFactor() throws FPEException {
		assertArrayEquals(this.cfp(3, 3), NumberTheory.factor(BigInteger.valueOf(9)));
		assertArrayEquals(this.cfp(6, 2), NumberTheory.factor(BigInteger.valueOf(12)));
		assertArrayEquals(this.cfp(6, 2), NumberTheory.factor(BigInteger.valueOf(12)));
		assertArrayEquals(this.cfp(16, 8), NumberTheory.factor(BigInteger.valueOf(128)));
		assertArrayEquals(this.cfp(43, 2), NumberTheory.factor(BigInteger.valueOf(86)));
		assertArrayEquals(this.cfp(19207, 2), NumberTheory.factor(BigInteger.valueOf(38414)));
		assertArrayEquals(this.cfp(1000, 1000), NumberTheory.factor(BigInteger.valueOf(1000000)));
	}

	@Test
	public void testPrimesBiCreation() {
		assertEquals(NumberTheory.PRIMES.length, NumberTheory.PRIMES_BI.length);
		for (int i = 0; i < NumberTheory.PRIMES.length; i++) {
			assertEquals(Integer.toString(NumberTheory.PRIMES[i]), NumberTheory.PRIMES_BI[i].toString());
		}
	}

}
