package com.opl.ke.cbf;

import java.util.ArrayList;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;
import com.opl.ke.cbf.entities.Variable;

/**
 * 
 * @author Geoffrey & Remi
 *
 */
public class StackTraceScoring {
	
	/**
	 * Source files found
	 */
	static ArrayList<String> files = null;
	
	/**
	 * Methods found
	 */
	static ArrayList<String> methods = null;
	
	public StackTraceScoring() {
	}
	
	public static int getDistance(StackTrace st1, StackTrace st2){
		int score=0;
		TraceElement trace1;
		TraceElement trace2;
		
		
		/**
		 * Line compare
		 */
		for(int i=0;i<st1.getElements().size();i++){
			trace1= st1.getElements().get(i);
			
			for(int j=0;j<st2.getElements().size();j++){
				trace2=st2.getElements().get(j);
				
				
				/**
				 * Address compare
				 */
				if( 
						!trace1.getMemoryAddress().equals("") &&
						trace1.getMemoryAddress().equals(trace2.getMemoryAddress())  
						){
					
					if( trace1.getId()== trace2.getId() ){
						score=score+5;
					}
					score=score+1;
				}
			
				
				
				/**
				 * Source file Path & Source file Line comparisons
				 */
				if(
						!trace1.getFileSource().equals("") &&
						trace1.getId()!=-1 &&
						trace1.getFileSource().equals(trace2.getFileSource()) &&
						trace1.getLineInSourceFile() == trace2.getLineInSourceFile() &&
						trace1.getId()== trace2.getId() 
						){
					
					if( !files.contains(trace1.getFileSource()) ){
						files.add(trace1.getFileSource());
						score=score+20;
					}
					
					/**
					 * Method name compare
					 */
					if( 
							!trace1.getMethodName().equals("??") &&  
							trace1.getId()!=-1 &&
							trace1.getMethodName().equals(trace2.getMethodName()) &&
							trace1.getId()== trace2.getId() 
							){
						
						if(!methods.contains(trace1.getMethodName())){
							methods.add(trace1.getMethodName());
							score=score+20;
						}	
						
						/**
						 * Vars comparisons
						 */
						if(
								!trace1.getVars().isEmpty() &&
								trace1.getId()!=-1
								){
							for(Variable var : trace1.getVars()){
								for(Variable varRef : trace2.getVars()){
									if(var.getName().equals(varRef.getName())){
										score=score+10;
										if(!var.getType().isEmpty()){
											if(var.getType().equals(varRef.getType())) score=score+8;
										} else {
											if(var.getValue().equals(varRef.getValue())) score=score+16;
										}
									}
								}
							}
						}
				
					}
					
					
				}
					
			}	
		}
		return score;
	}
	
}
