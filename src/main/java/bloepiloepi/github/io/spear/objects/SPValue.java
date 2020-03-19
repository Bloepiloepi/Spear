package bloepiloepi.github.io.spear.objects;

import java.util.ArrayList;
import java.util.Iterator;

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
			return "\"" + value.toString() + "\"";
		} else {
			return value.toString();
		}
	}
}
