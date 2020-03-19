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
	
	public void remove(String path) throws InvalidPathException {
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
	
	public void setInteger(String path, Integer value) throws InvalidPathException {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	public void setDouble(String path, Double value) throws InvalidPathException {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	public void setString(String path, String value) throws InvalidPathException {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	public void setBoolean(String path, Boolean value) throws InvalidPathException {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, value);
	}
	
	@Deprecated
	public void setList(String path, ArrayList<Object> value) throws InvalidPathException {
		Validation.notNull(value, "Spear can't handle null values! Use the remove() method instead.");
		SPPath spPath = new SPPath(path);
		set(spPath, formatList(value));
	}
	
	public void setList(String path, HashMap<Object, Object> value) throws InvalidPathException {
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
	
	@SuppressWarnings("unchecked")
	private ArrayList<SPAssignment> formatList(HashMap<Object, Object> value) {
		ArrayList<SPAssignment> assignments = new ArrayList<>();
		value.forEach((k, v) -> {
			if (k instanceof String) {
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
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Object get(String path) throws InvalidPathException {
		SPPath spPath = new SPPath(path);
		SPValue spValue = get(spPath);
		
		if (spValue == null) return null;
		Object value = spValue.getValue();
		
		if (value instanceof ArrayList) {
			if (((ArrayList) value).isEmpty()) {
				return new ArrayList<>();
			} else if (((ArrayList) value).get(0) instanceof SPAssignment) {
				HashMap<String, Object> list = new HashMap<>();
				
				for (SPAssignment assignment : (ArrayList<SPAssignment>) value) {
					list.put(assignment.getName(), assignment.getValue().getValue());
				}
				
				return list;
			} else if (((ArrayList) value).get(0) instanceof SPValue) {
				ArrayList<Object> list = new ArrayList<>();
				
				for (SPValue value1 : (ArrayList<SPValue>) value) {
					list.add(value1.getValue());
				}
				
				return list;
			} else {
				throw new InvalidListTypeException();
			}
		} else {
			return value;
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
	
	public static SPData load(File file) throws InvalidCharacterException, UnexpectedTokenException, FileNotFoundException, IncorrectFileTypeException {
		String[] split = file.getName().split("\\.");
		String extension = split[split.length - 1];
		if (extension.equals("sp")) {
			Scanner scanner = new Scanner(file);
			StringBuilder contents = new StringBuilder();
			scanner.forEachRemaining(contents::append);
			return loadFromString(contents.toString());
		} else {
			throw new IncorrectFileTypeException(file.getName());
		}
	}
	
	public static SPData loadFromString(String data) throws InvalidCharacterException, UnexpectedTokenException {
		return new SPParser(data).parse();
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void save(File file) throws IOException, IncorrectFileTypeException {
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
