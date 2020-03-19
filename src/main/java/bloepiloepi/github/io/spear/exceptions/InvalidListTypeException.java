package bloepiloepi.github.io.spear.exceptions;

public class InvalidListTypeException extends RuntimeException {
	
	public InvalidListTypeException() {
		super("Something went VERY wrong with Spear: List is of invalid type. THIS IS NOT YOUR FAULT! Please warn the author about this, including the stack trace and additional information.");
	}
}
