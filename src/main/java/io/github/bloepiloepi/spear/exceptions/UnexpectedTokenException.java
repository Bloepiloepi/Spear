package io.github.bloepiloepi.spear.exceptions;

import io.github.bloepiloepi.spear.parser.SPToken;

public class UnexpectedTokenException extends RuntimeException {
	
	public UnexpectedTokenException(SPToken token) {
		super("Unexpected token: " + token.getValue() + "! Is the file manually edited?");
	}
}
