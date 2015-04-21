package com.tgb.extras;

import java.util.concurrent.atomic.AtomicLong;

public class GeneratingID {
	
	public String generatingId(String Id){ 
		
//		AtomicLong idCounter = new AtomicLong();
//		return String.valueOf(idCounter.getAndIncrement());
		
		int surveyIdInt = Integer.parseInt(Id);
		surveyIdInt = surveyIdInt+1;
		Id = surveyIdInt+"";
		
		return Id;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	}

}
