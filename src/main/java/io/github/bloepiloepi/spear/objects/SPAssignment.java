package io.github.bloepiloepi.spear.objects;

/**
 * Represents an assignment in Spear.
 */
public class SPAssignment extends SPNodeValue {
	
	private String name;
	private SPValue value;
	
	public SPAssignment(String name, SPValue value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public SPValue getValue() {
		return value;
	}
	
	public void setValue(SPValue value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "SPAssignment{name=" + name + ",value=" + value + "}";
	}
	
	@Override
	String build() {
		return name + "=" + value.build();
	}
}
