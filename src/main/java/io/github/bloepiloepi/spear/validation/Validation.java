package io.github.bloepiloepi.spear.validation;

public class Validation {
	
	public static void notNull(Object object, String message) {
		if (object == null) throw new NullPointerException(message);
	}
}