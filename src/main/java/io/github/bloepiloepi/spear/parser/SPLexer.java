package io.github.bloepiloepi.spear.parser;

import io.github.bloepiloepi.spear.exceptions.InvalidCharacterException;

/**
 * The lexer of Spear.
 */
public class SPLexer {
	
	private int pos = 0;
	private Character currentChar;
	private final String text;
	
	SPLexer(String text) {
		this.text = text;
		if (text.length() > 0) {
			this.currentChar = text.charAt(this.pos);
		} else {
			this.currentChar = null;
		}
	}
	
	private void next() {
		pos += 1;
		if (pos > text.length() - 1) {
			currentChar = null;
		} else {
			currentChar = text.charAt(pos);
		}
	}
	
	private void skipWhiteSpace() {
		while (Character.isWhitespace(this.currentChar)) {
			this.next();
		}
	}
	
	private SPToken number() {
		StringBuilder result = new StringBuilder();
		
		while (currentChar != null && (Character.isDigit(currentChar) || currentChar == '-')) {
			result.append(currentChar);
			next();
		}
		
		if (currentChar != null && (currentChar == '.' || Character.isDigit(currentChar) || Character.isAlphabetic(currentChar) || currentChar == '-' || currentChar == '+')) {
			while (currentChar != null && (currentChar == '.' || Character.isDigit(currentChar) || isDoubleChar(currentChar) || currentChar == '-' || currentChar == '+')) {
				result.append(currentChar);
				next();
			}
			
			return new SPToken(SPTokenType.DOUBLE, result.toString());
		} else {
			return new SPToken(SPTokenType.INTEGER, result.toString());
		}
	}
	
	private boolean isDoubleChar(char character) {
		return (character == 'I' || character == 'n' || character == 'f' || character == 'i' || character == 't' || character == 'y' || character == 'x' || character == 'X' || character == 'e' || character == 'E' || character == 'F' || character == 'd' || character == 'D');
	}
	
	private SPToken identifier() {
		StringBuilder result = new StringBuilder();
		while (currentChar != null && (Character.isAlphabetic(currentChar) || Character.isDigit(currentChar) || currentChar == '_' || currentChar == '-' || currentChar == '+'  || currentChar == '&' || currentChar == '%' || currentChar == '^' || currentChar == '@' || currentChar == '#')) {
			result.append(currentChar);
			next();
		}
		
		if (result.toString().equalsIgnoreCase("true")) {
			return new SPToken(SPTokenType.TRUE, "true");
		} else if (result.toString().equalsIgnoreCase("false")) {
			return new SPToken(SPTokenType.FALSE, "false");
		} else {
			return new SPToken(SPTokenType.IDENTIFIER, result.toString());
		}
	}
	
	private SPToken string() {
		StringBuilder result = new StringBuilder();
		next();
		while (currentChar != null && currentChar != '"') {
			if (currentChar == '\\') {
				if (text.charAt(pos + 1) == 'n') {
					result.append("\n");
					next();
					next();
				} else if (text.charAt(pos + 1) == '"') {
					result.append("\"");
					next();
					next();
				} else {
					result.append(currentChar);
					next();
					result.append(currentChar);
					next();
				}
			} else {
				result.append(currentChar);
				next();
			}
		}
		next();
		return new SPToken(SPTokenType.STRING, result.toString());
	}
	
	public SPToken nextToken() {
		while (this.currentChar != null) {
			
			if (Character.isWhitespace(currentChar)) {
				skipWhiteSpace();
				continue;
			}
			
			if (Character.isDigit(currentChar) || currentChar == '-') {
				return number();
			}
			
			if (Character.isAlphabetic(currentChar)) {
				return identifier();
			}
			
			if (currentChar == '"') {
				return string();
			}
			
			SPTokenType tokenType = SPTokenType.getTypeOfToken(currentChar.toString());
			
			if (tokenType != null) {
				SPToken token = new SPToken(tokenType, currentChar.toString());
				next();
				return token;
			} else {
				throw new InvalidCharacterException(currentChar);
			}
		}
		
		return new SPToken(SPTokenType.EOF, "");
	}
	
	public SPToken peekToken() {
		int posBefore = pos;
		
		while (this.currentChar != null) {
			
			if (Character.isWhitespace(currentChar)) {
				skipWhiteSpace();
				continue;
			}
			
			if (Character.isDigit(currentChar) || currentChar == '-') {
				SPToken token = number();
				peekRestore(posBefore);
				return token;
			}
			
			if (Character.isAlphabetic(currentChar)) {
				SPToken token = identifier();
				peekRestore(posBefore);
				return token;
			}
			
			if (currentChar == '"') {
				SPToken token = string();
				peekRestore(posBefore);
				return token;
			}
			
			SPTokenType tokenType = SPTokenType.getTypeOfToken(currentChar.toString());
			
			if (tokenType != null) {
				SPToken token = new SPToken(tokenType, currentChar.toString());
				next();
				peekRestore(posBefore);
				return token;
			} else {
				peekRestore(posBefore);
				throw new InvalidCharacterException(currentChar);
			}
		}
		
		peekRestore(posBefore);
		return new SPToken(SPTokenType.EOF, "");
	}
	
	private void peekRestore(int pos) {
		this.pos = pos;
		currentChar = text.charAt(pos);
	}
}
