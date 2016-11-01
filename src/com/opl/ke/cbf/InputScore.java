package com.opl.ke.cbf;

import java.util.HashMap;
import java.util.Map;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

public class InputScore {
	StackTrace input;
	Map<Bucket, Integer> bucketsScores;
	Bucket bestBucket;
	int bestScore;
	
	public InputScore(StackTrace input) {
		this.input=input;
		bucketsScores= new HashMap<Bucket, Integer>();
	}
	
	
	
	public void addScore(Bucket bucket,int score){
		bucketsScores.put(bucket, score);
		if(bestBucket==null || bestScore < score){
			bestBucket=bucket;
			bestScore=score;
		}	
	}
	
	/**
	 * Get the bucket with the best score for the input
	 * @return bucket with the best score
	 */
	public Bucket getBestBucketScore(){
		return bestBucket;
	}

}
