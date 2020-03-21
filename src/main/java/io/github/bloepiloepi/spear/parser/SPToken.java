package io.github.bloepiloepi.spear.parser;

/**
 * A token in Spear.
 */
public class SPToken {
	
	private SPTokenType tokenType;
	private String value;
	
	SPToken(SPTokenType tokenType, String value) {
		this.tokenType = tokenType;
		this.value = value;
	}
	
	public SPTokenType getTokenType() {
		return tokenType;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "SPToken{SPTokenType." + tokenType + ", " + value + "}";
	}
}
