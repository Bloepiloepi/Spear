package bloepiloepi.github.io.spear.exceptions;

public class InvalidCharacterException extends Exception {
	
	public InvalidCharacterException(Character character) {
		super("Invalid Character: " + character + "! Is the file manually edited?");
	}
}
