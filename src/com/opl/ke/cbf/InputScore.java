package com.opl.ke.cbf;

import java.util.HashMap;
import java.util.Map;

import com.opl.ke.cbf.entities.Bucket;
import com.opl.ke.cbf.entities.StackTrace;

/**
 * Scores of stacktrace for each bucket
 * @author Geoffrey
 *
 */
public class InputScore {
	/**
	 * Reference StackTrace
	 */
	private StackTrace input;
	
	/**
	 * Scores of input by bucket
	 */
	private Map<Bucket, Integer> bucketsScores;
	private Bucket bestBucket;
	private int bestScore;
	
	public InputScore(StackTrace input) {
		this.setInput(input);
		bucketsScores= new HashMap<Bucket, Integer>();
	}
	
	public StackTrace getInput() {
		return input;
	}

	public void setInput(StackTrace input) {
		this.input = input;
	}

	public Bucket getBestBucket() {
		return bestBucket;
	}

	public void setBestBucket(Bucket bestBucket) {
		this.bestBucket = bestBucket;
	}

	/**
	 * Add stacktrace score by bucket
	 * @param bucket
	 * @param score
	 */
	public void addScore(Bucket bucket,int score){
		bucketsScores.put(bucket, score);
		if(getBestBucket()==null || bestScore < score){
			setBestBucket(bucket);
			bestScore=score;
		}	
	}
	
	/**
	 * Get the bucket with the best score for the input
	 * @return bucket with the best score
	 */
	public Bucket getBestBucketScore(){
		return getBestBucket();
	}

}
