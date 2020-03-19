package io.github.bloepiloepi.spear.parser;

public class SPToken {
	
	private SPTokenType tokenType;
	private String value;
	
	public SPToken(SPTokenType tokenType, String value) {
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
