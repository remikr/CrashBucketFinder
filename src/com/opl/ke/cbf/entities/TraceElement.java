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
	private String memoryAddress;
	private String methodName;
	private String fileSource;
	private int lineInFileSource;
	private List<Variable> vars;

	public TraceElement(int id, String memoryAdress, String methodName, String fileSource, int lineInFileSource) {
		this(id, memoryAdress, methodName, fileSource, lineInFileSource, new ArrayList<Variable>());
	}
	
	public TraceElement(int id, String memoryAddress, String methodName, String fileSource, int lineInFileSource, List<Variable> variables) {
		super();
		this.id = id;
		this.memoryAddress=memoryAddress;
		this.methodName = methodName;
		this.fileSource = fileSource;
		this.lineInFileSource = lineInFileSource;
		this.setVars(variables);
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
		this.memoryAddress=memoryAd;
	}
	
	public String getMemoryAddress(){
		return memoryAddress;
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

	public final int getLineInSourceFile() {
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
