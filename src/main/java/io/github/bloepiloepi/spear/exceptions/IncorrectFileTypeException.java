package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when the file type is not .sp
 */
public class IncorrectFileTypeException extends RuntimeException {
	
	public IncorrectFileTypeException(String fileName) {
		super("Spear can only handle .sp file extensions, so " + fileName + " is not valid.");
	}
}
