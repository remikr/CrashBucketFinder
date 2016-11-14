package com.opl.ke.cbf.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.opl.ke.cbf.entities.StackTrace;

/**
 * Loader of Bucket
 * @author Geoffrey & Rémi
 *
 */
public class BucketFileLoader {
	
	/**
	 * Load stackTraces from a (bucket) file
	 * @param bucketFile
	 * @return
	 */
	public static List<StackTrace> listStackTraces(File bucketFile){
		List<StackTrace> stackTraces = new ArrayList<StackTrace>();
		
		File[] traceFilesTab = bucketFile.listFiles();
		
		List<File> traceFilesList = new ArrayList<File>();
		
		for(int i = 0; i < traceFilesTab.length; i ++){
			File tmpBetween = traceFilesTab[i];
			
			if(tmpBetween.exists() && tmpBetween.list().length > 0){
				File trace = tmpBetween.listFiles()[0];
				
				if(trace.exists()){
					traceFilesList.add(trace);
				}
			}
		}
		
		for(File stackTraceFile : traceFilesList){
			
			try {
				stackTraces.add(StackTraceFileLoader.getStackTraceFromFile(stackTraceFile));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		return stackTraces;
	}

}
