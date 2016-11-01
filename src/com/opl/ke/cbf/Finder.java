package com.opl.ke.cbf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

public class Finder {
	
	List<Bucket> buckets;
	
	Map<StackTrace, Bucket> stacks;

	public Finder(List<Bucket> buckets) {
		this.buckets = buckets;
		stacks = new HashMap<StackTrace, Bucket>();
	}

	public void generateResults(File exportFile) throws IOException {
		FileWriter out = new FileWriter(exportFile);
		
		for(StackTrace st : stacks.keySet()){
			String line = st.getName() +" -> " + stacks.get(st).getName();
			out.write(line + "\n");
			out.flush();
		}
		
		out.close();
	}

	public void run(List<StackTrace> inputs) {
		
	}
}
