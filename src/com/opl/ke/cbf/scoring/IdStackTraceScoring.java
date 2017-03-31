package com.opl.ke.cbf.scoring;

import com.opl.ke.cbf.entities.StackTrace;
import com.opl.ke.cbf.entities.TraceElement;
import com.opl.ke.cbf.entities.Variable;

public class IdStackTraceScoring implements IStackTraceScoring {
	
	public IdStackTraceScoring() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDistance(StackTrace st1, StackTrace st2) {
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
						score=score+4;
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
					
	
					score=score+10;
					
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
					
					score=score+10;
	
			
				}
				
			}	
		}
		return score;
	}

}
