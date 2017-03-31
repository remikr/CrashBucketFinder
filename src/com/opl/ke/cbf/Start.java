package com.opl.ke.cbf;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.loaders.Loader;
import com.opl.ke.cbf.scoring.SimpleStackTraceScoring;
import com.opl.ke.cbf.scoring.IStackTraceScoring;
import com.opl.ke.cbf.scoring.IdStackTraceScoring;
import com.opl.ke.cbf.scoring.MemoryStackTraceScoring;

/**
 * 
 * @author Geoffrey & Remi
 *
 */
public class Start {

	public static void main(String[] args) {
		long begin1 = System.nanoTime();
		String folderPath = args[0];
		String scoring = args[1];
		System.out.println("Folder path : "+folderPath);
		File folder = new File(folderPath);
		
		IStackTraceScoring stackTraceScoring;
		if(scoring == "Simple"){
			stackTraceScoring = new SimpleStackTraceScoring();
		}else if(scoring.equals("Id")){
			stackTraceScoring = new IdStackTraceScoring();
		}else{
			stackTraceScoring = new MemoryStackTraceScoring();
		}

		
		Loader loader = new Loader();
		
		/**
		 * Load Buckets
		 */
		List<Bucket> buckets = loader.loadBuckets(folderPath);
		/**
		 * Load StackTraces
		 */
		List<StackTrace> inputs = loader.loadInputs(folderPath); 
		
		long end1 = System.nanoTime();
    	long begin2 = System.nanoTime();
		/**
		 * Find results
		 */
		Finder finder = new Finder(buckets,stackTraceScoring);
		finder.run(inputs);
		
		/**
		 * Export results
		 */
		File export = new File(folder, "output.txt");
		try {
			finder.generateResults(export);
		} catch (IOException e) {
			System.out.println(e.getMessage() + " | Unable to generate results file.");
		}
		
		long end2 = System.nanoTime();
		System.out.println((end1-begin1));
    	System.out.println((end2-begin2));
    	System.out.println((end1-begin1)/ 1000000000);
    	System.out.println((end2-begin2)/ 1000000000);
		
		/**
		 * Obtain global score
		 */
		try {
			if(args.length > 2){
				String ur="http://www.monperrus.net/martin/iagl-2016-crash-competition-result.py";
				String post="?target=result&dataset="+args[2]+"&proposal="+String.join("\n" ,Files.readAllLines(export.toPath()));
			
				System.out.print(executePost(ur, post));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Execute post request
	 * @param targetURL
	 * @param urlParameters
	 * @return String result
	 */
	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			/**
			 * Create connection
			 */
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", 
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			/**
			 * Send request
			 */
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			/**
			 * Get Response  
			 */
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}