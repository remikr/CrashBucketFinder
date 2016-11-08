package com.opl.ke.cbf.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

public class Loader {
	
	public Loader() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Bucket> loadBuckets(String folder){
		File bucketsFolder = new File(folder, "training");
		File[] bucketsFile = bucketsFolder.listFiles();
		
		List<Bucket> buckets = new ArrayList<Bucket>();
		for(int i = 0; i < bucketsFile.length; i ++){
			File bucketFile = bucketsFile[i];
			
			if(bucketFile.exists()){
				//System.out.println("bucket file : "+bucketFile.getName());
				Bucket bucket = new Bucket(bucketFile.getName());
				//System.out.println("bucket "+bucket.getName());
				bucket.setStacks(BucketFileLoader.listStackTraces(bucketFile));
				
				buckets.add(bucket);
			}
		}
		System.out.println("Buckets Size : " + buckets.size());
		return buckets;
	}
	
	public List<StackTrace> loadInputs(String folder){
		File tracesFolder = new File(folder, "testing");
		List<StackTrace> inputs = new ArrayList<StackTrace>();
		
		if(tracesFolder.exists()){
			File[] inputFiles = tracesFolder.listFiles();
			
			for(int i = 0; i < inputFiles.length; i++){
				//System.out.println(inputFiles[i].getName());
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
