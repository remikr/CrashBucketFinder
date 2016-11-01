package com.opl.ke.cbf.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;

public class StackTraceFileLoader {

	public static StackTrace getStackTraceFromFile(File stackTraceFile) throws FileNotFoundException{
		
		List<TraceElement> elements = new ArrayList<TraceElement>();
		
		System.out.println("###" + stackTraceFile.getAbsolutePath());
		/**
		 * Recupération du contenu du fichier
		 */
		List<String> stringElements = new ArrayList<String>();
		
		Scanner in = new Scanner(stackTraceFile);		
		
		String tmpElement = "";
		while(in.hasNextLine()){
			
			String line = in.nextLine() + "\n";
			
			if(line.startsWith("#")){
				line = line.substring(1);
				if(!tmpElement.isEmpty()){
					stringElements.add(tmpElement);
					tmpElement = "";
				}
			}
			tmpElement += line;
		}
		
		if(tmpElement != null){
			stringElements.add(tmpElement);
			tmpElement = "";
		}
		
		in.close();
		
		/**
		 * Split par élément + gen
		 */
		
		for(int i = 1; i < stringElements.size(); i ++){
			elements.add(generateStackElement(stringElements.get(i)));
		}
		
		
		String traceName = stackTraceFile.getName().substring(0, stackTraceFile.getName().length()-4);
		StackTrace stackTrace = new StackTrace(traceName, elements);
		
		return stackTrace;
	}

	private static TraceElement generateStackElement(String elementString) {
		int id = -1;
		String methodname = "";
		String fileSource = "";
		String details = "";
		int lineInFileSource = -1;
		
		/**
		 * Obtention numéro de ligne
		 */
		int value = Integer.parseInt(elementString.substring(0, elementString.indexOf(" ")));
		
		lineInFileSource = value;
		
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		if(elementString.startsWith("<")){
			//TODO ajouter détail à trace element
			return new TraceElement(id, methodname, fileSource, lineInFileSource);
		}
		
		/**
		 * Adresse mémoire
		 */
		
		if(elementString.startsWith("Ox")){
			String memoryAdress = elementString.substring(0, elementString.indexOf(" "));
			elementString = elementString.substring(elementString.indexOf(" ")).trim();
			
			if(elementString.startsWith("in ")) elementString = elementString.substring(elementString.indexOf(" ")).trim();
		}
		
		
		/**
		 * Nom méthode
		 */
		methodname = elementString.substring(0, elementString.indexOf(" "));
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		/**
		 * Arguments méthode
		 */
		
		//System.out.println("ELEMENT STRING1> " + elementString);
		String arguments = elementString.substring(1, elementString.indexOf(")"));
		elementString = elementString.substring(elementString.indexOf(")")+1).trim();
		
		//System.out.println("ELEMENT STRING2> " + elementString);
		
		if(elementString.startsWith(" from ") || elementString.startsWith(" at ")){
			elementString = elementString.substring(elementString.indexOf((elementString.startsWith(" from "))?"from ":"at ")).trim();
			elementString = elementString.substring(elementString.indexOf(" ")).trim();
			
			String fileSourceDetails = elementString.substring(0, elementString.indexOf("\n"));
			
			/**
			 * Fichier + ligne dans le fichier (at/from + :)
			 */
			
			System.out.println("----> " + fileSourceDetails );
			if(fileSourceDetails.contains(":")){
				String[] fileDetailsSplit;
				fileDetailsSplit = fileSourceDetails.split(":");
				fileSource = fileDetailsSplit[0];
				lineInFileSource = Integer.parseInt(fileDetailsSplit[1]);
			} else {
				fileSource = fileSourceDetails.substring(0, fileSourceDetails.lastIndexOf("."));
				lineInFileSource = Integer.parseInt(fileSourceDetails.substring(fileSourceDetails.lastIndexOf(".")+1));
			}
			
			elementString = elementString.substring(elementString.indexOf("\n")).trim();
			/**
			 * Vars ( = )
			 */
		}
		
		
		return new TraceElement(id, methodname, fileSource, lineInFileSource);
	}
}
