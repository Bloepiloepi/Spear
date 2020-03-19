package bloepiloepi.github.io.spear.exceptions;

public class UnsupportedKeyException extends RuntimeException {
	
	public UnsupportedKeyException() {
		super("Sorry, Spear can only handle HashMaps with a string as key. This is due to the different list type in Spear: It's key is not an object, but an identifier.");
	}
}
