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
import com.opl.ke.cbf.scoring.IStackTraceScoring;
import com.opl.ke.cbf.scoring.MemoryStackTraceScoring;

/**
 * 
 * @author Geoffrey & Remi
 *
 */
public class Finder {
	
	/**
	 * All Buckets
	 */
	private List<Bucket> buckets;
	
	/**
	 * Scores of StackTraces by bucket
	 */
	private List<InputScore> inputsScore;

	/**
	 * Bucket by tested stacktraces
	 */
	private Map<String, Integer> stats;
	
	private IStackTraceScoring stackTraceScoring;

	public Finder(List<Bucket> buckets, IStackTraceScoring stackTraceScoring) {
		this.buckets = buckets;
		stats = new HashMap<String,Integer>();
		this.inputsScore=new ArrayList<InputScore>();
		this.stackTraceScoring = stackTraceScoring;
	}

	/**
	 * Generate exportFile with results of stats
	 * @param exportFile
	 * @throws IOException
	 */
	public void generateResults(File exportFile) throws IOException {
		FileWriter out = new FileWriter(exportFile);
		InputScore inputScore;
		
		for(int i=0;i<inputsScore.size();i++){
			inputScore=inputsScore.get(i);
			String line = inputScore.getInput().getName() +" -> " + inputScore.getBestBucket().getName();
			if( !stats.containsKey(inputScore.getBestBucket().getName()) ){
				stats.put(inputScore.getBestBucket().getName(), 1);
			}else{
				int val=stats.get(inputScore.getBestBucket().getName());
				stats.put(inputScore.getBestBucket().getName(), val+1);
			}
			out.write(line + "\n");
			out.flush();
		}
		
		out.close();
	}

	public void run(List<StackTrace> inputs) {
		StackTrace input;
		Bucket bucket;
		StackTrace bucketStack;
		int bucketScore=0;
		InputScore inputScore;
		
		/**
		 * Inputs course
		 */
		for(int i=0;i<inputs.size();i++){
			input=inputs.get(i);
			inputScore=new InputScore(input);
			
			
			/**
			 * For each bucket
			 */
			for(int j=0;j<buckets.size();j++){
				bucket=buckets.get(j);
				
				stackTraceScoring.init();
				
				/**
				 * For each bucket's stack
				 */
				for(int k=0;k<bucket.getStacks().size();k++){
					bucketStack=bucket.getStacks().get(k);	

					bucketScore=bucketScore+stackTraceScoring.getDistance(input,bucketStack);			
				}
				
				inputScore.addScore(bucket, bucketScore);
				bucketScore=0;
			}
			
			inputsScore.add(inputScore);
		}
	}
}
