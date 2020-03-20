package io.github.bloepiloepi.spear.exceptions;

public class UnsupportedTypeException extends RuntimeException {
	
	public UnsupportedTypeException(Object object) {
		super("Sorry, Spear can't handle the type " + object.getClass().getTypeName() + ".");
	}
}