package com.opl.ke.cbf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

public class Finder {
	
	List<Bucket> buckets;
	List<InputScore> inputsScore;

	Map<String, Integer> stats;

	public Finder(List<Bucket> buckets) {
		this.buckets = buckets;
		stats = new HashMap<String,Integer>();
		this.inputsScore=new ArrayList<InputScore>();
	}

	public void generateResults(File exportFile) throws IOException {
		FileWriter out = new FileWriter(exportFile);
		InputScore inputScore;
		
		//for(StackTrace st : stacksBuckets.keySet()){	
		//	String line = st.getName() +" -> " + stacksBuckets.get(st).getName();
		for(int i=0;i<inputsScore.size();i++){
			inputScore=inputsScore.get(i);
			String line = inputScore.input.getName() +" -> " + inputScore.bestBucket.getName();
			if( !stats.containsKey(inputScore.bestBucket.getName()) ){
				stats.put(inputScore.bestBucket.getName(), 1);
			}else{
				int val=stats.get(inputScore.bestBucket.getName());
				stats.put(inputScore.bestBucket.getName(), val+1);
			}
			out.write(line + "\n");
			out.flush();
		}
		
		out.close();
		
		System.out.println(stats);
	}

	public void run(List<StackTrace> inputs) {
		StackTrace input;
		Bucket bucket;
		StackTrace bucketStack;
		int bucketScore=0;
		InputScore inputScore;
		
		//parcourt inputs
		for(int i=0;i<inputs.size();i++){
			input=inputs.get(i);
			inputScore=new InputScore(input);
			
			
			//pour chaque bucket
			for(int j=0;j<buckets.size();j++){
				bucket=buckets.get(j);
				StackTraceScoring.files=new ArrayList<String>();
				StackTraceScoring.methodes=new ArrayList<String>();
				
				//pour chaque bucket stack
				for(int k=0;k<bucket.getStacks().size();k++){
					bucketStack=bucket.getStacks().get(k);	

					bucketScore=bucketScore+StackTraceScoring.getScore(input,bucketStack);			
				}
				
				inputScore.addScore(bucket, bucketScore);
				bucketScore=0;
			}
			
			inputsScore.add(inputScore);
		}
	}
}
