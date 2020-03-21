package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when a HashMap to set contains not only Strings as key
 */
public class UnsupportedKeyException extends RuntimeException {
	
	public UnsupportedKeyException() {
		super("Sorry, Spear can only handle HashMaps with a string as key. This is due to the different list type in Spear: It's key is not an object, but an identifier.");
	}
}
