package javafpe;

/**
 * Checked exception for all errors that can occur during encryption or decryption. Typically this is thrown for passing in unsuitable input values for secret
 * keys, initialisation vectors, or data to encrypt/decrypt.
 */
public class FPEException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with a detail message.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public FPEException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance with a detail message and cause.
	 * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause
	 *            is nonexistent or unknown.)
	 */
	public FPEException(String message, Throwable cause) {
		super(message, cause);
	}
}
