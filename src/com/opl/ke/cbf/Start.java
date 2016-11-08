package com.opl.ke.cbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.loaders.BucketFileLoader;
import com.opl.ke.cbf.loaders.Loader;
import com.opl.ke.cbf.loaders.StackTraceFileLoader;

public class Start {

	public static void main(String[] args) {
		//String folderPath = "C:\\Users\\Geoffrey\\Desktop\\nautilus";
		//String folderPath = "C:\\Users\\Admin\\Desktop\\Cours\\Master2\\opl\\crash_bucket";
		String folderPath = args[0];
		System.out.println("folder path : "+folderPath);
		File folder = new File(folderPath);
		Loader loader = new Loader();
		
		/**
		 * Load Buckets
		 */
		List<Bucket> buckets = loader.loadBuckets(folderPath);
		/**
		 * Load StackTraces
		 */
		List<StackTrace> inputs = loader.loadInputs(folderPath); 
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
			System.out.println(e.getMessage() + " | Impossible de g�n�rer le fichier de r�sultats.");
		}
		
		
	}
}