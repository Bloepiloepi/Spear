package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when the path contains invalid characters
 */
public class InvalidPathException extends RuntimeException {
	
	public InvalidPathException() {
		super("Spear identifiers can only have letters, numbers, underscores, stripes and +'s.");
	}
}
