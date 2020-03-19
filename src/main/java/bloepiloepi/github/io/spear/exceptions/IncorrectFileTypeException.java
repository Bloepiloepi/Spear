package bloepiloepi.github.io.spear.exceptions;

public class IncorrectFileTypeException extends Exception {
	
	public IncorrectFileTypeException(String fileName) {
		super("Spear can only handle .sp file extensions, so " + fileName + " is not valid.");
	}
}
