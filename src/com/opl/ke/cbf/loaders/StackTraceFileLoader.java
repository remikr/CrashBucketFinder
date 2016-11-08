package com.opl.ke.cbf.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;
import com.opl.ke.cbf.entities.Variable;

public class StackTraceFileLoader {

	public static StackTrace getStackTraceFromFile(File stackTraceFile) throws FileNotFoundException{
		
		List<TraceElement> elements = new ArrayList<TraceElement>();
		
		//System.out.println("###" + stackTraceFile.getAbsolutePath());
		/**
		 * Recup�ration du contenu du fichier
		 */
		List<String> stringElements = new ArrayList<String>();
		
		//Scanner in = new Scanner(stackTraceFile);
		//in.useDelimiter(System.lineSeparator());
		BufferedReader br = new BufferedReader(new FileReader(stackTraceFile));
		
		String tmpElement = "";
		String line;
		try {
			while(
					(line = br.readLine()) != null
					//in.hasNextLine()
					){
				
				line = line + "\n";
				//line=in.nextLine() + "\n";
				//System.out.print("lecture fichier : "+line);

				if(line.startsWith("#")){
					//nouvelle element
					//System.out.print("nouvelle ligne : "+line);
					line = line.substring(1);
					//sauvegarde l'ancienne
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
		
		//in.close();
		
		/**
		 * Split par �l�ment + gen
		 */
		
		for(int i = 0; i < stringElements.size(); i ++){
			elements.add(generateStackElement(stringElements.get(i)));
		}
		
		
		String traceName = stackTraceFile.getName().substring(0, stackTraceFile.getName().length()-4);
		StackTrace stackTrace = new StackTrace(traceName, elements);
		
		return stackTrace;
	}

	private static TraceElement generateStackElement(String elementString) {
		int id = -1;
		String methodname = "";
		String memoryAddress = "";
		String fileSource = "";
		int lineInFileSource = -1;
		String fileSourceDetails;
		List<Variable> vars = new ArrayList<Variable>();
		
		//System.out.println(elementString);
		/**
		 * Line number
		 */
		int value = Integer.parseInt(elementString.substring(0, elementString.indexOf(" ")));
		id = value;
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		//System.out.println(elementString);
		
		/**
		 * Special line
		 */
		if(elementString.startsWith("<")){
			//TODO ajouter d�tail � trace element
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
		//System.out.println(elementString);
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
			//System.out.println();
			//System.out.print(elementString);
			
			if(elementString.indexOf("\n") == -1){
				fileSourceDetails = elementString;
			}else{
				fileSourceDetails = elementString.substring(0, elementString.indexOf("\n"));
			}
			
			//System.out.println("fileSourceDetails : " + fileSourceDetails );
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
				//Sometimes, there's no line number
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
						//Var name
						varName = varDetails[0].trim();
						
						//Var type
						if(varDetails[1].contains("(") && varDetails[1].contains("*)")){
							varType = varDetails[1].substring(varDetails[1].indexOf("(")+1, varDetails[1].indexOf("*)")+1);
							varDetails[1] = varDetails[1].substring(varDetails[1].indexOf("*)")+2).trim();
						}
						
						//Var value (or memory address)
						varValue = varDetails[1].trim();
												
						//Only simple vars (No composed var)
						if(!varValue.contains("{") && !varValue.contains(",")){
							if(!composed){
								Variable var = new Variable(varName, varType, varValue);
								System.out.println("Var : " + "\n\t name='" + varName + "'\n\t varType='"+ varType +"'\n\t value='"+varValue+"'");
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
		

		/*System.out.print("id : "+ id+";");
		System.out.print("memory : "+ memoryAdress+";");
		System.out.print("method : "+methodname+";");
		System.out.print("file : "+fileSource+";");
		System.out.print("lineN : "+lineInFileSource+";");*/
		//System.out.print("vars : "+vars+";");
		//System.out.println();
		return new TraceElement(id, memoryAddress, methodname, fileSource, lineInFileSource, vars);
	}
	
	public static int indexOf(Pattern pattern, String s) {
	    Matcher matcher = pattern.matcher(s);
	    return matcher.find() ? matcher.start() : -1;
	}
}
