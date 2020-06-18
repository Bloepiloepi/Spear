package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when the path contains invalid characters
 */
public class InvalidPathException extends RuntimeException {
	
	public InvalidPathException(char wrongCharacter) {
		super("Spear identifiers can not contain the following character: " + wrongCharacter);
	}
}
