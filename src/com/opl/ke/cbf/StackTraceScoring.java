package com.opl.ke.cbf;

import java.util.ArrayList;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;

public class StackTraceScoring {
	static ArrayList<String> files = null;
	static ArrayList<String> methodes = null;
	
	public StackTraceScoring() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getScore(StackTrace st1, StackTrace st2){
		int score=0;
		TraceElement trace1;
		TraceElement trace2;
		
		
		//comparaison des lignes
		for(int i=0;i<st1.getElements().size();i++){
			trace1= st1.getElements().get(i);
			
			for(int j=0;j<st2.getElements().size();j++){
				trace2=st2.getElements().get(j);
				
				
				if( 
						!trace1.getMemoryAdresse().equals("") &&
						trace1.getMemoryAdresse().equals(trace2.getMemoryAdresse())  ){
					if( trace1.getId()== trace2.getId() ){
						score=score+5;
					}
					score=score+1;
				}
			
			
				if( !trace1.getMethodName().equals("??") &&  
						trace1.getId()!=-1 &&
						trace1.getMethodName().equals(trace2.getMethodName()) &&
						trace1.getId()== trace2.getId() ){
					
					if(!methodes.contains(trace1.getMethodName())){
						methodes.add(trace1.getMethodName());
						score=score+20;
					}	
			
				}
				
				
				if( !trace1.getFileSource().equals("") &&
						trace1.getId()!=-1 &&
						trace1.getFileSource().equals(trace2.getFileSource()) &&
						trace1.getLineInFileSource() == trace2.getLineInFileSource() &&
						trace1.getId()== trace2.getId() ){
					
					if(!files.contains(trace1.getFileSource())){
						files.add(trace1.getFileSource());
						score=score+20;
					}	
			
					
				}
				
				
					
			}	
		}
		return score;
	}
	
	
	

}
