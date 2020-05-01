package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when the first character of an identifier is a number.
 */
public class FirstCharacterOfIdentifierException extends RuntimeException {
	
	public FirstCharacterOfIdentifierException() {
		super("Spear identifiers can not have a number, a plus, a minus, a whitespace or a string sign as the first character in an identifier!");
	}
}
