package com.opl.ke.cbf.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bucket of StackTraces
 * @author Geoffrey & Remi
 */
public class Bucket {
	
	private String name;
	
	private List<StackTrace> stacks;

	public Bucket(String name) {
		super();
		this.name = name;
		stacks = new ArrayList<StackTrace>();
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final List<StackTrace> getStacks() {
		return stacks;
	}

	public final void setStacks(List<StackTrace> stacks) {
		this.stacks = stacks;
	}
	
	public final void addStackTrace(StackTrace stack){
		this.stacks.add(stack);
	}
}
