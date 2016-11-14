package com.opl.ke.cbf.entities;
/**
 * Represents a Variable of TraceElement
 * @author Geoffrey & Remi
 *
 */
public class Variable {
	
	private String name;
	
	private String type;
	
	private String value;

	public Variable(String name, String type, String value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

}
