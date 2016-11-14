package com.opl.ke.cbf.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;
import com.opl.ke.cbf.entities.Variable;

/**
 * StackTrace loader
 * @author Geoffrey & Remi
 *
 */
public class StackTraceFileLoader {

	/**
	 * Load a complete StackTrace from file
	 * @param stackTraceFile
	 * @return StackTrace
	 * @throws FileNotFoundException
	 */
	public static StackTrace getStackTraceFromFile(File stackTraceFile) throws FileNotFoundException{
		/**
		 * Elements of stack trace
		 */
		List<TraceElement> elements = new ArrayList<TraceElement>();
		
		/**
		 * Recovering file contents
		 */
		List<String> stringElements = new ArrayList<String>();
	
		BufferedReader br = new BufferedReader(new FileReader(stackTraceFile));
		
		String tmpElement = "";
		String line;
		try {
			while((line = br.readLine()) != null){
			
				line = line + "\n";

				if(line.startsWith("#")){
					//New element
					line = line.substring(1);
					//Save older
					if(!tmpElement.isEmpty()){
						stringElements.add(tmpElement);
						tmpElement = "";
					}
				}
				
				tmpElement += line;
			}
		} catch (IOException e) {e.printStackTrace();}
		
		if(tmpElement != null){
			stringElements.add(tmpElement);
			tmpElement = "";
		}
		
		/**
		 * Split element + gen
		 */
		
		for(int i = 0; i < stringElements.size(); i ++){
			elements.add(generateStackElement(stringElements.get(i)));
		}
		
		
		String traceName = stackTraceFile.getName().substring(0, stackTraceFile.getName().length()-4);
		StackTrace stackTrace = new StackTrace(traceName, elements);
		
		return stackTrace;
	}

	/**
	 * Load complete TraceElement from String element
	 * @param elementString
	 * @return Trace Element
	 */
	private static TraceElement generateStackElement(String elementString) {
		int id = -1;
		String methodname = "";
		String memoryAddress = "";
		String fileSource = "";
		int lineInFileSource = -1;
		String fileSourceDetails;
		List<Variable> vars = new ArrayList<Variable>();
		
		/**
		 * Line number
		 */
		int value = Integer.parseInt(elementString.substring(0, elementString.indexOf(" ")));
		id = value;
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		/**
		 * Special line
		 */
		if(elementString.startsWith("<")){
			return new TraceElement(id, memoryAddress, methodname, fileSource, lineInFileSource);
		}
		
		
		/**
		 * Memory Address
		 */
		if(elementString.startsWith("0x")){
			memoryAddress = elementString.substring(0, elementString.indexOf(" "));	
			elementString = elementString.substring(elementString.indexOf(" ")).trim();	
			if(elementString.startsWith("in ")){
				elementString = elementString.substring(elementString.indexOf(" ")).trim();
			}
		}
		
		
		/**
		 * Method name
		 */
		methodname = elementString.substring(0, elementString.indexOf(" "));
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		/**
		 * Method arguments
		 */
		elementString = elementString.substring(elementString.indexOf(")")+1).trim();
		
		
		/**
		 * File + line in file (at/from + :)
		 */
		if(elementString.startsWith("from ") || elementString.startsWith("at ")){
			elementString = elementString.substring(elementString.indexOf((elementString.startsWith("from "))?"from ":"at ")).trim();
			elementString = elementString.substring(elementString.indexOf(" ")).trim();
			
			if(elementString.indexOf("\n") == -1){
				fileSourceDetails = elementString;
			}else{
				fileSourceDetails = elementString.substring(0, elementString.indexOf("\n"));
			}
			
			if(fileSourceDetails.contains(":")){
				String[] fileDetailsSplit;
				fileDetailsSplit = fileSourceDetails.split(":");
				fileSource = fileDetailsSplit[0];
				int iEnd = indexOf( Pattern.compile("\n| ") , fileDetailsSplit[1] );
				if(iEnd==-1){
					iEnd=fileDetailsSplit[1].length();
				}
				lineInFileSource = Integer.parseInt( fileDetailsSplit[1].substring(0, iEnd) );
				
			} else {
				if( fileSourceDetails.substring(fileSourceDetails.lastIndexOf(".")+1).matches("[0-9]+")  ){
					fileSource = fileSourceDetails.substring(0, fileSourceDetails.lastIndexOf("."));
					lineInFileSource = Integer.parseInt(fileSourceDetails.substring(fileSourceDetails.lastIndexOf(".")+1));
				}else{
					fileSource = fileSourceDetails;
				}
			}
			
			if(elementString.indexOf("\n") != -1){
				elementString = elementString.substring(elementString.indexOf("\n")).trim();
			}
		
		}
		
		/**
		 * Vars ( = )
		 */
		if(!elementString.isEmpty()){
			boolean composed = false;
			for(String line : elementString.split("\n")){
				if(line.contains(" = ")){
					String varName ="";
					String varType = "";
					String varValue = "";
					
					String[] varDetails = line.split(" = ");
					
					if(varDetails.length > 1){
						/**
						 * Var name
						 */
						varName = varDetails[0].trim();
						
						/**
						 * Var type
						 */
						if(varDetails[1].contains("(") && varDetails[1].contains("*)")){
							varType = varDetails[1].substring(varDetails[1].indexOf("(")+1, varDetails[1].indexOf("*)")+1);
							varDetails[1] = varDetails[1].substring(varDetails[1].indexOf("*)")+2).trim();
						}
						
						/**
						 * Var value (or memory address)
						 */
						varValue = varDetails[1].trim();
												
						/**
						 * Only simple vars (No composed var)
						 */
						if(!varValue.contains("{") && !varValue.contains(",")){
							if(!composed){
								Variable var = new Variable(varName, varType, varValue);
								vars.add(var);
							} else if(varValue.contains("}") && !varValue.contains(",")){
								composed = false;
							}
						} else {
							composed = true;
						}
					}
				}
			}
		}
	
		return new TraceElement(id, memoryAddress, methodname, fileSource, lineInFileSource, vars);
	}
	
	public static int indexOf(Pattern pattern, String s) {
	    Matcher matcher = pattern.matcher(s);
	    return matcher.find() ? matcher.start() : -1;
	}
}
