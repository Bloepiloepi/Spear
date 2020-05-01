package io.github.bloepiloepi.spear.validation;

import io.github.bloepiloepi.spear.exceptions.FirstCharacterOfIdentifierException;
import io.github.bloepiloepi.spear.exceptions.InvalidPathException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a Spear path. Validates whether it is correct, and splits it into nodes.
 */
public class SPPath {
	
	private String path;
	private ArrayList<String> nodes;
	
	public SPPath(String path) throws InvalidPathException {
		this.path = path;
		checkPath();
		String[] nodes = path.split("\\.");
		this.nodes = new ArrayList<>();
		this.nodes.addAll(Arrays.asList(nodes));
	}
	
	private void checkPath() throws InvalidPathException {
		if (!Character.isDigit(path.toCharArray()[0]) && !(path.toCharArray()[0] == '+') && !(path.toCharArray()[0] == '-') && !Character.isWhitespace(path.toCharArray()[0]) && !(path.toCharArray()[0] == '"')) {
			for (char character : path.toCharArray()) {
				if (!(Character.isAlphabetic(character) || Character.isDigit(character) || character == '_' || character == '.')) {
					throw new InvalidPathException();
				}
			}
		} else {
			throw new FirstCharacterOfIdentifierException();
		}
	}
	
	public String getCurrentNode() {
		return nodes.get(0);
	}
	
	public String getPath() {
		return path;
	}
	
	public void removeCurrentNode() {
		nodes.remove(0);
	}
	
	public boolean isLastNode() {
		return nodes.size() == 1;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("SPPath{<currentNode>");
		for (String node : nodes) {
			string.append(".").append(node);
		}
		return string.append("}").toString();
	}
}
