package bloepiloepi.github.io.spear.parser;

import bloepiloepi.github.io.spear.exceptions.InvalidCharacterException;
import bloepiloepi.github.io.spear.exceptions.UnexpectedTokenException;
import bloepiloepi.github.io.spear.objects.*;
import gq.mineville.spear.objects.*;

import java.util.ArrayList;

public class SPParser {

	private SPLexer lexer;
	private SPToken currentToken;
	
	public SPParser(String text) {
		this.lexer = new SPLexer(text);
	}
	
	private void eat(SPTokenType tokenType) throws InvalidCharacterException, UnexpectedTokenException {
		if (currentToken.getTokenType() == tokenType) {
			currentToken = lexer.nextToken();
		} else {
			throw new UnexpectedTokenException(currentToken);
		}
	}
	
	private SPValue list() throws InvalidCharacterException, UnexpectedTokenException {
		eat(SPTokenType.BRACKET_SQUARE_OPEN);
		
		if (currentToken.getTokenType() == SPTokenType.IDENTIFIER) {
			ArrayList<SPAssignment> contents = new ArrayList<>();
			contents.add(assignment());
			
			while (currentToken.getTokenType() == SPTokenType.COMMA) {
				eat(SPTokenType.COMMA);
				contents.add(assignment());
			}
			
			eat(SPTokenType.BRACKET_SQUARE_CLOSE);
			return new SPValue(contents);
		} else if (currentToken.getTokenType() == SPTokenType.INTEGER || currentToken.getTokenType() == SPTokenType.DOUBLE || currentToken.getTokenType() == SPTokenType.STRING || currentToken.getTokenType() == SPTokenType.TRUE || currentToken.getTokenType() == SPTokenType.FALSE || currentToken.getTokenType() == SPTokenType.BRACKET_SQUARE_OPEN) {
			ArrayList<SPValue> contents = new ArrayList<>();
			contents.add(value());
			
			while (currentToken.getTokenType() == SPTokenType.COMMA) {
				eat(SPTokenType.COMMA);
				contents.add(value());
			}
			
			eat(SPTokenType.BRACKET_SQUARE_CLOSE);
			return new SPValue(contents);
		} else {
			ArrayList<SPValue> contents = new ArrayList<>();
			eat(SPTokenType.BRACKET_SQUARE_CLOSE);
			return new SPValue(contents);
		}
	}
	
	private SPValue value() throws InvalidCharacterException, UnexpectedTokenException {
		if (currentToken.getTokenType() == SPTokenType.INTEGER) {
			int value = Integer.parseInt(currentToken.getValue());
			eat(SPTokenType.INTEGER);
			return new SPValue(value);
		} else if (currentToken.getTokenType() == SPTokenType.DOUBLE) {
			double value = Double.parseDouble(currentToken.getValue());
			eat(SPTokenType.DOUBLE);
			return new SPValue(value);
		} else if (currentToken.getTokenType() == SPTokenType.STRING) {
			Object value = currentToken.getValue();
			eat(SPTokenType.STRING);
			return new SPValue(value);
		} else if (currentToken.getTokenType() == SPTokenType.TRUE) {
			eat(SPTokenType.TRUE);
			return new SPValue(true);
		} else if (currentToken.getTokenType() == SPTokenType.FALSE) {
			eat(SPTokenType.FALSE);
			return new SPValue(false);
		} else if (currentToken.getTokenType() == SPTokenType.BRACKET_SQUARE_OPEN) {
			return list();
		} else {
			throw new UnexpectedTokenException(currentToken);
		}
	}
	
	private SPAssignment assignment() throws InvalidCharacterException, UnexpectedTokenException {
		String name = currentToken.getValue();
		eat(SPTokenType.IDENTIFIER);
		eat(SPTokenType.EQUALS);
		return new SPAssignment(name, value());
	}
	
	private ArrayList<SPNodeValue> nodeList() throws InvalidCharacterException, UnexpectedTokenException {
		eat(SPTokenType.BRACKET_ROUND_OPEN);
		
		ArrayList<SPNodeValue> nodeList = new ArrayList<>();
		nodeList.add(object());
		
		while (currentToken.getTokenType() == SPTokenType.COMMA) {
			eat(SPTokenType.COMMA);
			nodeList.add(object());
		}
		
		eat(SPTokenType.BRACKET_ROUND_CLOSE);
		return nodeList;
	}
	
	private SPNode node() throws InvalidCharacterException, UnexpectedTokenException {
		String name = currentToken.getValue();
		eat(SPTokenType.IDENTIFIER);
		eat(SPTokenType.COLON);
		
		ArrayList<SPNodeValue> subNodes = new ArrayList<>();
		if (currentToken.getTokenType() == SPTokenType.IDENTIFIER) {
			subNodes.add(object());
		} else {
			subNodes.addAll(nodeList());
		}
		return new SPNode(name, subNodes);
	}
	
	private SPNodeValue object() throws InvalidCharacterException, UnexpectedTokenException {
		SPToken peekToken = lexer.peekToken();
		if (peekToken.getTokenType() == SPTokenType.COLON) {
			return node();
		} else {
			return assignment();
		}
	}
	
	public SPData parse() throws InvalidCharacterException, UnexpectedTokenException {
		currentToken = lexer.nextToken();
		
		ArrayList<SPNodeValue> nodes = new ArrayList<>();
		while (currentToken.getTokenType() == SPTokenType.IDENTIFIER || currentToken.getTokenType() == SPTokenType.BRACKET_ROUND_OPEN) {
			if (currentToken.getTokenType() == SPTokenType.IDENTIFIER) {
				nodes.add(object());
			} else {
				nodes.addAll(nodeList());
			}
		}
		return new SPData(nodes);
	}
}
