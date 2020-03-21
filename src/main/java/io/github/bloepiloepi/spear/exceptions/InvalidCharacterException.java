package io.github.bloepiloepi.spear.exceptions;

public class InvalidCharacterException extends RuntimeException {
	
	public InvalidCharacterException(Character character) {
		super("Invalid Character: " + character + "! Is the file manually edited?");
	}
}
