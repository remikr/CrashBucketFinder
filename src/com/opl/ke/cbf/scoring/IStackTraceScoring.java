package com.opl.ke.cbf.scoring;

import com.opl.ke.cbf.entities.StackTrace;

public interface IStackTraceScoring {

	public void init();
	
	public int getDistance(StackTrace st1, StackTrace st2);
}
