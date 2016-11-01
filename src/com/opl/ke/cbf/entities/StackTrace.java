package com.opl.ke.cbf.entities;

import java.util.List;

/**
 * Represent a error file
 * @author Admin
 *
 */
public class StackTrace {

	private String name;
	
	private List<TraceElement> elements;

	public StackTrace(String name, List<TraceElement> elements) {
		super();
		this.name = name;
		this.elements = elements;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final List<TraceElement> getElements() {
		return elements;
	}

	public final void setElements(List<TraceElement> elements) {
		this.elements = elements;
	}

}
