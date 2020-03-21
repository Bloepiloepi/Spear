package io.github.bloepiloepi.spear.exceptions;

public class IncorrectFileTypeException extends RuntimeException {
	
	public IncorrectFileTypeException(String fileName) {
		super("Spear can only handle .sp file extensions, so " + fileName + " is not valid.");
	}
}
