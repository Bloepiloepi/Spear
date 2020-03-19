package bloepiloepi.github.io.spear.objects;

import bloepiloepi.github.io.spear.exceptions.UnsupportedKeyException;
import bloepiloepi.github.io.spear.exceptions.UnsupportedTypeException;
import bloepiloepi.github.io.spear.validation.SPPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SPNode extends SPNodeValue {
	
	private String name;
	
	private ArrayList<SPAssignment> separatedAssignments;
	private ArrayList<SPNode> separatedNodes;
	
	public SPNode(String name, ArrayList<SPNodeValue> subNodes) {
		this.name = name;
		separate(subNodes);
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
	
	private boolean checkUnused() {
		return separatedNodes.size() == 0 && separatedAssignments.size() == 0;
	}
	
	SPValue get(SPPath path) {
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
	
	void set(SPPath path, Object value) {
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
	
	public boolean remove(SPPath path) {
		if (path.isLastNode()) {
			removeAssignment(path.getCurrentNode());
			removeNode(path.getCurrentNode());
		} else {
			SPNode node = getNode(path.getCurrentNode());
			if (node != null) {
				path.removeCurrentNode();
				if (node.remove(path)) { //aka is node unused now
					removeNode(node.getName());
				}
			}
		}
		return checkUnused();
	}
	
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
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "SPNode{}";
	}
	
	@Override
	public String build() {
		StringBuilder result = new StringBuilder(name);
		result.append(":");
		
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
}
