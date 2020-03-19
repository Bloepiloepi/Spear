package bloepiloepi.github.io.spear.exceptions;

import bloepiloepi.github.io.spear.parser.SPToken;

public class UnexpectedTokenException extends Exception {
	
	public UnexpectedTokenException(SPToken token) {
		super("Unexpected token: " + token.getValue() + "! Is the file manually edited?");
	}
}
