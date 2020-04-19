package io.github.bloepiloepi.spear.objects;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a value in Spear.
 */
public class SPValue extends SPObject {
	
	private Object value;
	
	public SPValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "SPValue{" + value + "}";
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	String build() {
		if (value instanceof ArrayList) {
			StringBuilder result = new StringBuilder();
			result.append("[");
			
			if (!((ArrayList) value).isEmpty()) {
				if (((ArrayList) value).get(0) instanceof SPAssignment) {
					Iterator<SPAssignment> iterator = ((ArrayList<SPAssignment>) value).iterator();
					result.append(iterator.next().build());
					while (iterator.hasNext()) {
						result.append(",");
						result.append(iterator.next().build());
					}
				} else if (((ArrayList) value).get(0) instanceof SPValue) {
					Iterator<SPValue> iterator = ((ArrayList<SPValue>) value).iterator();
					result.append(iterator.next().build());
					while (iterator.hasNext()) {
						result.append(",");
						result.append(iterator.next().build());
					}
				}
			}
			
			result.append("]");
			return result.toString();
		} else if (value instanceof String) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\"");
			
			for (char character : ((String) value).toCharArray()) {
				if (character == '"') {
					stringBuilder.append("\\\"");
				} else if (character == '\n') {
					stringBuilder.append("\\n");
				} else {
					stringBuilder.append(character);
				}
			}
			
			stringBuilder.append("\"");
			return stringBuilder.toString();
		} else {
			return value.toString();
		}
	}
}
