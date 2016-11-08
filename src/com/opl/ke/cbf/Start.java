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
		
		/**
		 * Obtain global score
		 */
		try {
			String ur="http://www.monperrus.net/martin/iagl-2016-crash-competition-result.py";
			String post="?target=result&dataset="+args[1]+"&proposal="+String.join("\n" ,Files.readAllLines(export.toPath()));
			
			System.out.print(executePost(ur, post));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			//Create connection
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

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
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