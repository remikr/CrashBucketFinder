package com.opl.ke.cbf.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;
import com.opl.ke.cbf.entities.Variable;

public class StackTraceFileLoader {

	public static StackTrace getStackTraceFromFile(File stackTraceFile) throws FileNotFoundException{
		
		List<TraceElement> elements = new ArrayList<TraceElement>();
		
		//System.out.println("###" + stackTraceFile.getAbsolutePath());
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
		String memoryAdress = "";
		String fileSource = "";
		int lineInFileSource = -1;
		String fileSourceDetails;
		List<Variable> vars = new ArrayList<Variable>();
		
		/**
		 * Obtention numéro de ligne
		 */
		int value = Integer.parseInt(elementString.substring(0, elementString.indexOf(" ")));
		id = value;
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		/**
		 * ligne speciale
		 */
		if(elementString.startsWith("<")){
			//TODO ajouter détail à trace element
			return new TraceElement(id, memoryAdress, methodname, fileSource, lineInFileSource);
		}
		
		
		/**
		 * Adresse mémoire
		 */
		if(elementString.startsWith("0x")){
			memoryAdress = elementString.substring(0, elementString.indexOf(" "));	
			elementString = elementString.substring(elementString.indexOf(" ")).trim();	
			if(elementString.startsWith("in ")){
				elementString = elementString.substring(elementString.indexOf(" ")).trim();
			}
		}
		
		
		/**
		 * Nom méthode
		 */
		methodname = elementString.substring(0, elementString.indexOf(" "));
		elementString = elementString.substring(elementString.indexOf(" ")).trim();
		
		elementString.substring(1, elementString.indexOf(")"));
		elementString = elementString.substring(elementString.indexOf(")")+1).trim();
		
		
		/**
		 * Fichier + ligne dans le fichier (at/from + :)
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
				lineInFileSource = Integer.parseInt(fileDetailsSplit[1]);
				
			} else {
				//des fois, il n'y a pas le numero de ligne
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
						//Nom de la variable
						varName = varDetails[0].trim();
						
						//Type de la variable
						if(varDetails[1].contains("(") && varDetails[1].contains("*)")){
							varType = varDetails[1].substring(varDetails[1].indexOf("(")+1, varDetails[1].indexOf("*)"));
							varDetails[1] = varDetails[1].substring(varDetails[1].indexOf("*)")+2).trim();
						}
						
						//Valeur de la variable (ou adresse mémoire)
						varValue = varDetails[1].trim();
												
						//Variables simples uniquement (pas de variable composée)
						if(!varValue.contains("{") && !varValue.contains(",")){
							Variable var = new Variable(varName, varType, varValue);
							//System.out.println("Var : " + "\n\t name='" + varName + "'\n\t varType='"+ varType +"'\n\t value='"+varValue+"'");
							vars.add(var);
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
		return new TraceElement(id, memoryAdress, methodname, fileSource, lineInFileSource, vars);
	}
}
