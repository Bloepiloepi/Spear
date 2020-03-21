package io.github.bloepiloepi.spear.validation;

/**
 * Validation
 */
public class Validation {
	
	/**
	 * Checks whether something is null. Throws a NullPointerException if it is.
	 *
	 * @param object  The object to perform the null check on
	 * @param message The message to give the NullPointerException when the object is null
	 */
	public static void notNull(Object object, String message) {
		if (object == null) throw new NullPointerException(message);
	}
}
