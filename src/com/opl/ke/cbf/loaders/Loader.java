package com.opl.ke.cbf.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

/**
 * Loader all things needed
 * @author Geoffrey
 *
 */
public class Loader {
	
	public Loader() {
	}
	
	/**
	 * Load buckets from folder named training
	 * @param folder
	 * @return List<Bucket>
	 */
	public List<Bucket> loadBuckets(String folder){
		File bucketsFolder = new File(folder, "training");
		File[] bucketsFile = bucketsFolder.listFiles();
		
		List<Bucket> buckets = new ArrayList<Bucket>();
		for(int i = 0; i < bucketsFile.length; i ++){
			File bucketFile = bucketsFile[i];
			
			if(bucketFile.exists()){
				Bucket bucket = new Bucket(bucketFile.getName());
				bucket.setStacks(BucketFileLoader.listStackTraces(bucketFile));
				
				buckets.add(bucket);
			}
		}
		System.out.println("Buckets size : " + buckets.size());
		return buckets;
	}
	
	/**
	 * Load inputs from folder named testing
	 * @param folder
	 * @return List<StackTrace>
	 */
	public List<StackTrace> loadInputs(String folder){
		File tracesFolder = new File(folder, "testing");
		List<StackTrace> inputs = new ArrayList<StackTrace>();
		
		if(tracesFolder.exists()){
			File[] inputFiles = tracesFolder.listFiles();
			
			for(int i = 0; i < inputFiles.length; i++){
				try {
					inputs.add(StackTraceFileLoader.getStackTraceFromFile(inputFiles[i]));
				} catch (FileNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		System.out.println("Inputs size : " + inputs.size());
		return inputs;
	}

}
