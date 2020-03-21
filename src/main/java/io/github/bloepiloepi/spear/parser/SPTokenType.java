package io.github.bloepiloepi.spear.parser;

import java.util.HashMap;

/**
 * Enum of all the token types in Spear.
 */
public enum SPTokenType {

	IDENTIFIER(""),
	COLON(":"),
	BRACKET_ROUND_OPEN("("),
	BRACKET_ROUND_CLOSE(")"),
	EQUALS("="),
	COMMA(","),
	BRACKET_SQUARE_OPEN("["),
	BRACKET_SQUARE_CLOSE("]"),
	DOT("."),
	INTEGER(""),
	DOUBLE(""),
	TRUE(""),
	FALSE(""),
	STRING(""),
	EOF("");
	
	private static final HashMap<String, SPTokenType> BY_VALUE = new HashMap<String, SPTokenType>();
	
	static {
		for (SPTokenType e : values()) {
			BY_VALUE.put(e.value, e);
		}
	}
	
	public final String value;
	
	SPTokenType(String value) {
		this.value = value;
	}
	
	public static SPTokenType getTypeOfToken(String token) {
		return BY_VALUE.get(token);
	}
}
