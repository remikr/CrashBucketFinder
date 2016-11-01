package com.opl.ke.cbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.loaders.BucketFileLoader;
import com.opl.ke.cbf.loaders.StackTraceFileLoader;

public class Start {

	public static void main(String[] args) {
		String folderPath = "C:\\Users\\Geoffrey\\Desktop\\nautilus";//args[0];
		File folder = new File(folderPath);
		
		/**
		 * Load Buckets
		 */
		
		File bucketsFolder = new File(folder, "nautilus-training");
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
		System.out.println("Buckets Size : " + buckets.size());
		
		/**
		 * Load StackTraces
		 */
		
		File tracesFolder = new File(folder, "nautilus-testing");
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
		
		/**
		 * Find results
		 */
		
		Finder finder = new Finder(buckets);
		
		finder.run(inputs);
		
		
		/**
		 * Export results
		 */
		
		File export = new File(folder, "output.txt");
		
		try {
			finder.generateResults(export);
		} catch (IOException e) {
			System.out.println(e.getMessage() + " | Impossible de générer le fichier de résultats.");
		}
	}
}
