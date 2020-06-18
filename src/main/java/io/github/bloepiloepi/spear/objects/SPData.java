package io.github.bloepiloepi.spear.objects;

import io.github.bloepiloepi.spear.exceptions.*;
import io.github.bloepiloepi.spear.parser.SPParser;
import io.github.bloepiloepi.spear.validation.SPPath;
import io.github.bloepiloepi.spear.validation.Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * The main Spear object. This is the object that represents a data file of Spear, in which you can remove, set and get values.
 */
public class SPData extends SPObject {
	
	private ArrayList<SPNode> separatedNodes;
	private ArrayList<SPAssignment> separatedAssignments;
	
	public SPData(ArrayList<SPNodeValue> nodes) {
		separate(nodes);
	}
	
	private void separate(ArrayList<SPNodeValue> nodes) {
		separatedNodes = new ArrayList<>();
		separatedAssignments = new ArrayList<>();
		
		for (SPNodeValue node : nodes) {
			if (node instanceof SPAssignment) {
				separatedAssignments.add((SPAssignment) node);
			} else {
				separatedNodes.add((SPNode) node);
			}
		}
	}
	
	private ArrayList<SPObject> merge() {
		ArrayList<SPObject> merged = new ArrayList<>();
		merged.addAll(separatedNodes);
		merged.addAll(separatedAssignments);
		return merged;
	}
	
	private SPAssignment getAssignment(String name) {
		for (SPAssignment assignment : separatedAssignments) {
			if (assignment.getName().equals(name)) {
				return assignment;
			}
		}
		return null;
	}
	
	private SPNode getNode(String name) {
		for (SPNode node : separatedNodes) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}
	
	private void removeNode(String name) {
		separatedNodes.removeIf(node -> node.getName().equals(name));
	}
	
	private void removeAssignment(String name) {
		separatedAssignments.removeIf(assignment -> assignment.getName().equals(name));
	}
	
	private SPValue get(SPPath path) {
		if (path.isLastNode()) {
			SPAssignment assignment = getAssignment(path.getCurrentNode());
			if (assignment == null) return null;
			return assignment.getValue();
		} else {
			SPNode node = getNode(path.getCurrentNode());
			if (node == null) return null;
			
			path.removeCurrentNode();
			return node.get(path);
		}
	}
	
	private void set(SPPath path, Object value) {
		if (path.isLastNode()) {
			if (getNode(path.getCurrentNode()) != null) {
				removeNode(path.getCurrentNode());
			}
			
			SPAssignment assignment = getAssignment(path.getCurrentNode());
			if (assignment != null) {
				assignment.setValue(new SPValue(value));
			} else {
				separatedAssignments.add(new SPAssignment(path.getCurrentNode(), new SPValue(value)));
			}
		} else {
			if (getAssignment(path.getCurrentNode()) != null) {
				removeAssignment(path.getCurrentNode());
			}
			
			SPNode node = getNode(path.getCurrentNode());
			if (node != null) {
				path.removeCurrentNode();
				node.set(path, value);
			} else {
				SPNode newNode = new SPNode(path.getCurrentNode(), new ArrayList<>());
				separatedNodes.add(newNode);
				path.removeCurrentNode();
				newNode.set(path, value);
			}
		}
	}
	
	/**
	 * Remove a node or assignment. This is the equivalent of setting it to null (which you can't do because Spear doesn't handle null).
	 * @param path The path to remove
	 */
	public void remove(String path) {
		SPPath spPath = new SPPath(path);
		if (spPath.isLastNode()) {
			removeAssignment(spPath.getCurrentNode());
			removeNode(spPath.getCurrentNode());
		} else {
			SPNode node = getNode(spPath.getCurrentNode());
			if (node != null) {
				spPath.removeCurrentNode();
				if (node.remove(spPath)) { //aka is node unused now
					removeNode(node.getName());
				}
			}
		}
	}
	
	/**
	 * Gives a list of the subnodes of a path you give. Returns null if the path does not exist.
	 *
	 * @param path The path to the node to get the list from
	 * @return     The list of subnodes if the node exists, else null
	 */
	public ArrayList<String> listNodes(String path) {
		SPPath spPath = new SPPath(path);
		
		ArrayList<String> nodes = new ArrayList<>();
		if (spPath.isLastNode()) {
			for (SPNode node : separatedNodes) {
				nodes.add(node.getName());
			}
			for (SPAssignment assignment : separatedAssignments) {
				nodes.add(assignment.getName());
			}
			
			return nodes;
		} else {
			SPNode node = getNode(spPath.getCurrentNode());
			if (node != null) {
				spPath.removeCurrentNode();
				nodes.addAll(node.listNodes(spPath));
				
				return nodes;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Set an Integer in the file.
	 *
	 * @param path  The path to the new Integer
	 * @param value The value of the new Integer
	 */
	public void setInteger(String path, Integer value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	/**
	 * Set a Double in the file.
	 *
	 * @param path  The path to the new Double
	 * @param value The value of the new Double
	 */
	public void setDouble(String path, Double value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	/**
	 * Set a String in the file.
	 *
	 * @param path  The path to the new String
	 * @param value The value of the new String
	 */
	public void setString(String path, String value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	/**
	 * Set a Boolean in the file.
	 *
	 * @param path  The path to the new Boolean
	 * @param value The value of the new Boolean
	 */
	public void setBoolean(String path, Boolean value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	/**
	 * Set a List in the file.
	 *
	 * @param path  The path to the new List
	 * @param value The value of the new List
	 */
	public void setList(String path, ArrayList<Object> value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, formatList(value));
	}
	
	/**
	 * Set a key based List (HashMap) in the file.
	 *
	 * @param path  The path to the new List
	 * @param value The value of the new List
	 */
	public void setKeyBasedList(String path, HashMap<Object, Object> value) {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, formatList(value));
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<SPValue> formatList(ArrayList<Object> value) throws UnsupportedTypeException {
		ArrayList<SPValue> values = new ArrayList<>();
		for (Object object : value) {
			if (object instanceof Integer || object instanceof Double || object instanceof String || object instanceof Boolean) {
				values.add(new SPValue(object));
			} else if (object instanceof ArrayList) {
				values.add(new SPValue(formatList((ArrayList<Object>) object)));
			} else if (object instanceof HashMap) {
				values.add(new SPValue(formatList((HashMap<Object, Object>) object)));
			} else {
				throw new UnsupportedTypeException(object);
			}
		}
		
		return values;
	}
	
	private void checkIdentifier(String id) {
		if (!Character.isDigit(id.toCharArray()[0]) && !(id.toCharArray()[0] == '+') && !(id.toCharArray()[0] == '-') && !Character.isWhitespace(id.toCharArray()[0]) && !(id.toCharArray()[0] == '"')) {
			for (char character : id.toCharArray()) {
				if (!(Character.isAlphabetic(character) || Character.isDigit(character) || character == '_' || character == '-' || character == '+'  || character == '&' || character == '%' || character == '^' || character == '@' || character == '#')) {
					throw new InvalidPathException(character);
				}
			}
		} else {
			throw new FirstCharacterOfIdentifierException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<SPAssignment> formatList(HashMap<Object, Object> value) {
		ArrayList<SPAssignment> assignments = new ArrayList<>();
		value.forEach((k, v) -> {
			if (k instanceof String) {
				checkIdentifier((String) k);
				if (v instanceof Integer || v instanceof Double || v instanceof String || v instanceof Boolean) {
					assignments.add(new SPAssignment((String) k, new SPValue(v)));
				} else if (v instanceof ArrayList) {
					assignments.add(new SPAssignment((String) k, new SPValue(formatList((ArrayList<Object>) v))));
				} else if (v instanceof HashMap) {
					assignments.add(new SPAssignment((String) k, new SPValue(formatList((HashMap<Object, Object>) v))));
				}
			} else {
				throw new UnsupportedKeyException();
			}
		});
		
		return assignments;
	}
	
	private Object get(String path) {
		SPPath spPath = new SPPath(path);
		SPValue spValue = get(spPath);
		
		if (spValue == null) return null;
		
		return parseValue(spValue);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object parseValue(SPValue value1) {
		Object value = value1.getValue();
		if (value instanceof Integer || value instanceof Double || value instanceof String || value instanceof Boolean) {
			return value;
		} else if (value instanceof ArrayList) {
			if (((ArrayList) value).isEmpty()) {
				return value;
			} else if (((ArrayList) value).get(0) instanceof SPAssignment) {
				HashMap<String, Object> list = new HashMap<>();
				
				for (SPAssignment assignment : (ArrayList<SPAssignment>) value) {
					list.put(assignment.getName(), parseValue(assignment.getValue()));
				}
				
				return list;
			} else if (((ArrayList) value).get(0) instanceof SPValue) {
				ArrayList<Object> list = new ArrayList<>();
				
				for (SPValue value2 : (ArrayList<SPValue>) value) {
					list.add(parseValue(value2));
				}
				
				return list;
			} else {
				throw new InvalidListTypeException();
			}
		} else {
			throw new InvalidListTypeException();
		}
	}
	
	/**
	 * Get an Integer from the file.
	 *
	 * @param path The path to the Integer
	 * @return     The value of the Integer if it does exist, else null.
	 */
	public Integer getInteger(String path) {
		Object result = get(path);
		if (result instanceof Integer) {
			return (Integer) result;
		} else {
			return null;
		}
	}
	
	/**
	 * Get a Double from the file.
	 *
	 * @param path The path to the Double
	 * @return     The value of the Double if it does exist, else null.
	 */
	public Double getDouble(String path) {
		Object result = get(path);
		if (result instanceof Double) {
			return (Double) result;
		} else {
			return null;
		}
	}
	
	/**
	 * Get a String from the file.
	 *
	 * @param path The path to the String
	 * @return     The value of the String if it does exist, else null.
	 */
	public String getString(String path) {
		Object result = get(path);
		if (result instanceof String) {
			return (String) result;
		} else {
			return null;
		}
	}
	
	/**
	 * Get a Boolean from the file.
	 *
	 * @param path The path to the Boolean
	 * @return     The value of the Boolean if it does exist, else null.
	 */
	public Boolean getBoolean(String path) {
		Object result = get(path);
		if (result instanceof Boolean) {
			return (Boolean) result;
		} else {
			return null;
		}
	}
	
	/**
	 * Get a List from the file.
	 *
	 * @param path The path to the List
	 * @return     The value of the List as an ArrayList if it does exist, else null.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getList(String path) {
		Object result = get(path);
		if (result instanceof ArrayList) {
			return (ArrayList<Object>) result;
		} else {
			return null;
		}
	}
	
	/**
	 * Get a key based List from the file.
	 *
	 * @param path The path to the List
	 * @return     The value of the List as a HashMap if it does exist, else null.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public HashMap<String, Object> getKeyBasedList(String path) {
		Object result = get(path);
		if (result instanceof HashMap) {
			return (HashMap<String, Object>) result;
		} else if (result instanceof ArrayList && ((ArrayList) result).isEmpty()) {
			return new HashMap<>();
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "SPData{}";
	}
	
	@Override
	String build() {
		StringBuilder result = new StringBuilder();
		
		ArrayList<SPObject> nodes = merge();
		if (nodes.size() < 2) {
			if (nodes.size() == 1) {
				result.append(nodes.get(0).build());
			}
		} else {
			result.append("(");
			
			Iterator<SPObject> iterator = nodes.iterator();
			result.append(iterator.next().build());
			while (iterator.hasNext()) {
				result.append(",");
				result.append(iterator.next().build());
			}
			
			result.append(")");
		}
		
		return result.toString();
	}
	
	/**
	 * Returns the contents of the Spear file how it would be saved in a file.
	 *
	 * @return The contents of the Spear file.
	 */
	public String getRaw() {
		return build();
	}
	
	/**
	 * Reads a file, parses it and loads it into the cache.
	 *
	 * @param file                   The File to read
	 * @return                       An SPData object from the file
	 * @throws FileNotFoundException If the file does not exist
	 */
	public static SPData load(File file) throws FileNotFoundException {
		String[] split = file.getName().split("\\.");
		String extension = split[split.length - 1];
		if (extension.equals("sp")) {
			Scanner scanner = new Scanner(file);
			StringBuilder contents = new StringBuilder();
			while (scanner.hasNextLine()) {
				contents.append(scanner.nextLine());
			}
			return loadFromString(contents.toString());
		} else {
			throw new IncorrectFileTypeException(file.getName());
		}
	}
	
	/**
	 * Loads a new SPData object from a String.
	 *
	 * @param data The data in Spear format
	 * @return     A SPData object from the String
	 */
	public static SPData loadFromString(String data) {
		return new SPParser(data).parse();
	}
	
	/**
	 * Builds a new Spear file from this object and saves it to a File.
	 * This creates the file for you if it does not exist.
	 *
	 * @param file         The File to save to
	 * @throws IOException If an error occurred while saving
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void save(File file) throws IOException {
		String[] split = file.getName().split("\\.");
		String extension = split[split.length - 1];
		if (extension.equals("sp")) {
			if (file.isDirectory()) {
				throw new IOException("Spear could not create file " + file.getName() + ", because it was an existing directory.");
			} else {
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file);
				String build = build();
				fileWriter.write(build);
				fileWriter.close();
			}
		} else {
			throw new IncorrectFileTypeException(file.getName());
		}
	}
}
