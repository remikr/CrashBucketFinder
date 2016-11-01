package com.opl.ke.cbf.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a line (#) in a stacktrace
 * @author Admin
 *
 */
public class TraceElement {
	
	private int id;
	private String memoryAdress;
	private String methodName;
	private String fileSource;
	private int lineInFileSource;
	private List<Variable> vars;

	public TraceElement(int id, String memoryAdress, String methodName, String fileSource, int lineInFileSource) {
		super();
		this.id = id;
		this.memoryAdress=memoryAdress;
		this.methodName = methodName;
		this.fileSource = fileSource;
		this.lineInFileSource = lineInFileSource;
		this.setVars(new ArrayList<Variable>());
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
	
	public void setMemoryAdress(String memoryAd){
		this.memoryAdress=memoryAd;
	}
	
	public String getMemoryAdresse(){
		return memoryAdress;
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
