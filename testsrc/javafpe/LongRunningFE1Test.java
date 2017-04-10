package javafpe;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a long-running set of round-trip tests with pseudo-random configuration.  This should typically not be run as part of a standard unit test run as it can
 * take several hours to run.
 */
public class LongRunningFE1Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(LongRunningFE1Test.class);

	/**
	 * Worker thread payload for dividing up a {@link FE1Test#testMultipleRoundTrips()}.
	 */
	private class FpeRoundTripRunnable implements Runnable {
		final int progressModulus;
		final byte[] key;
		final byte[] iv;
		final BigInteger modulusBI;
		final int start;
		final int end;
		final BitMap foundNumbers;
		final String toStringValue;

		public Exception ex;

		/**
		 * Initialise this thread worker with the necessary data for it to do its calculations. 
		 * @param key See {@link FE1#encrypt(BigInteger, BigInteger, byte[], byte[])}.
		 * @param iv See {@link FE1#encrypt(BigInteger, BigInteger, byte[], byte[])}.
		 * @param modulus See {@link FE1#encrypt(BigInteger, BigInteger, byte[], byte[])}.
		 * @param start The first number (inclusive) to be round-tripped.
		 * @param end  the last number (exclusive) to be round-tripped
		 * @param foundNumbers a bitmap storing which values have already been generated.
		 */
		FpeRoundTripRunnable(byte[] key, byte[] iv, BigInteger modulus, int start, int end, BitMap foundNumbers) {
			this.key = key;
			this.iv = iv;
			this.modulusBI = modulus;
			this.start = start;
			this.end = end;
			progressModulus = (end - start) / 10;
			this.foundNumbers = foundNumbers;

			toStringValue = String.format("FpeRoundTripRunnable(key = %s, iv = %s, modulus = %s, start = %d, end = %d)", TestUtility.byteArrayToHex(key),
							TestUtility.byteArrayToHex(iv), modulusBI.toString(), start, end);
		}

		@Override
		public void run() {
			try {
				for (int i = start; i < end; i++) {
					long encryptedValue = TestUtility.roundTripTest(modulusBI, i, key, iv);
					if (foundNumbers.testAndSet(encryptedValue)) {
						fail(String.format("Encrypted %d and got %d which already was generated (out of %d values)", i, encryptedValue, modulusBI));
					}
					if (i % progressModulus == 0) {
						LOGGER.info("Thread {} hit {}", this, i);
					}

				}
			} catch (FPEException fpeex) {
				LOGGER.error("FPEException thrown", fpeex);
				ex = fpeex;
			}
		}

		@Override
		public String toString() {
			return toStringValue;
		}

	}

	/**
	 * Utility method to test round tripping a complete set of potential inputs (bounded by <em>modulusBI</em>). Ensures that no output values are repeated
	 * (although this should be a given if the round-trip worked).
	 * 
	 * @param key the key to use, must not be null, must be compatible with FE1 (size), usually 6 bytes.
	 * @param iv the IV/tweak to use, must not be null, must be compatible with FE1 (size), usually 6 bytes.
	 * @param modulus the number of potential inputs (0..modulus).
	 */
	public void testRoundTripsParallel(byte[] key, byte[] iv, int modulus) {
		final int cpuCount = Runtime.getRuntime().availableProcessors();
		final int callsPerCpu = modulus / cpuCount;
		if (modulus % cpuCount != 0) {
			LOGGER.warn("Rounding down modulusBI to " + callsPerCpu * cpuCount);
		}

		BigInteger modulusBI = BigInteger.valueOf(modulus);
		LOGGER.info("Calls Per CPU: {}; CPUs: {}; Total {}", callsPerCpu, cpuCount, modulus);

		BitMap foundNumbers = new BitMap(modulus);
		FpeRoundTripRunnable[] wThreads = new FpeRoundTripRunnable[cpuCount];
		Thread[] threads = new Thread[cpuCount];
		for (int t = 0; t < threads.length; t++) {
			final int start = t * callsPerCpu;
			final int end = ((t + 1) * callsPerCpu) - 1;
			FpeRoundTripRunnable wThread = new FpeRoundTripRunnable(key, iv, modulusBI, start, end, foundNumbers);
			Thread thread = new Thread(wThread);
			wThreads[t] = wThread;
			threads[t] = thread;
			threads[t].start();
		}

		for (int t = 0; t < threads.length; t++) {
			try {
				threads[t].join();
				if (wThreads[t].ex != null) {
					fail("Worker thread " + wThreads[t] + " threw an exception: " + wThreads[t].ex);
				}
			} catch (InterruptedException ie) {
				LOGGER.error("Thread was interrupted", ie);
				fail("Thread " + wThreads[t] + " was interrupted, failing test");
			}
		}

	}

	/**
	 * This can run for a long time depending on your processor.  You can adjust the number of tests by changing the
	 * for-loop bounds and the modulus.  It's 10 and 1024 in the checked in version, but feel free to reduce or extend as you see fit.
	 * 
	 * @throws InterruptedException Thrown if something goes wrong and a thread is interrupted (indicates a runtime failure not related to the code).
	 */
	@Test
	public void testPseudoRandomRoundTrips() throws InterruptedException {
		Random r = new Random(0);
		List<Integer> primes = Arrays.stream(NumberTheory.PRIMES).boxed().collect(Collectors.toList());
		// Change the range of the for loop to set how many round trip tests are done.
		for (int i = 0; i < 10; i++) {
			int modulus = r.nextInt(1024);
			if (primes.contains(modulus)) {
				// Skip prime moduli are they'll fail because you can't use a prime modulus with this algorithm.
				continue;
				}
			
			byte[] key = new byte[6];
			r.nextBytes(key);
			byte[] iv = new byte[6];
			r.nextBytes(iv);
			testRoundTripsParallel(key, iv, modulus);
		}
	}

}
