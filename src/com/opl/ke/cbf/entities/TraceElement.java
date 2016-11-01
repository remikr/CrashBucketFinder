package com.opl.ke.cbf.entities;

import java.util.ArrayList;
import java.util.List;

public class TraceElement {
	
	private int id;
	
	private String methodName;
	
	private String fileSource;
	
	private int lineInFileSource;
	
	private List<Variable> vars;

	public TraceElement(int id, String methodName, String fileSource, int lineInFileSource) {
		super();
		this.id = id;
		this.methodName = methodName;
		this.fileSource = fileSource;
		this.lineInFileSource = lineInFileSource;
		this.setVars(new ArrayList<>());
	}

	public final String getMethodName() {
		return methodName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public final void setMethodName(String name) {
		this.methodName = name;
	}

	public final String getFileSource() {
		return fileSource;
	}

	public final void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public final int getLineInFileSource() {
		return lineInFileSource;
	}

	public final void setLineInFileSource(int lineInFileSource) {
		this.lineInFileSource = lineInFileSource;
	}

	public List<Variable> getVars() {
		return vars;
	}

	public void setVars(List<Variable> vars) {
		this.vars = vars;
	}	
}
