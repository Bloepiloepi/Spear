package io.github.bloepiloepi.spear.exceptions;

/**
 * Thrown when a list contains a type Spear can't handle
 */
public class UnsupportedTypeException extends RuntimeException {
	
	public UnsupportedTypeException(Object object) {
		super("Sorry, Spear can't handle the type " + object.getClass().getTypeName() + ".");
	}
}
