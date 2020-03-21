package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when the lexer cannot understand a character
 */
public class InvalidCharacterException extends RuntimeException {
	
	public InvalidCharacterException(Character character) {
		super("Invalid Character: " + character + "! Is the file manually edited?");
	}
}
