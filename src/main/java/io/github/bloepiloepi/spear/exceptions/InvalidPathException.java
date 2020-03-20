package io.github.bloepiloepi.spear.exceptions;

public class InvalidPathException extends Exception {
	
	public InvalidPathException() {
		super("Spear identifiers can only have letters, numbers or underscores.");
	}
}