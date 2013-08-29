package com.ucsd.laurentLab.tfStatistics;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

public class StatObject {
	private String tf;
	private String id;
	
	private int count = 0;	
	private double mean = 0;
	private double sigma = 0;
	private double pValue = 0;
	private double zValue = 0;
	private double sumCounts = 0;
	private ArrayList<Integer> counts = new ArrayList<Integer>();
	private int numConnections = 0;	
	
	StatObject(String tf, String id){
		this.tf = tf;
		this.id = id;
	}
	
	public void incrementCount(){
		count++;
	}
	
	public void addCountToList(){
		sumCounts +=count;
		counts.add(count);
		count = 0;
	}
	
	public void incrementConnectionCount(){
		numConnections++;
	}
	
	public void setStats() {
		double nCounts = counts.size();		
		mean = sumCounts/nCounts;
		
		double difSum = 0;
		for(Integer val: counts){
			double dif = val - mean;
			difSum += (dif*dif);
		}
		
		sigma = Math.sqrt(difSum/nCounts);
		if(sigma!=0){		
			NormalDistribution d = new NormalDistribution(mean, sigma);
			pValue = d.cumulativeProbability(numConnections);
			zValue = (numConnections-mean)/sigma;
		}
	}

	public void print(){
		if(numConnections !=0){
			System.out.println(tf + ", " + id + ", " + mean + ", " + sigma + ", " + numConnections + ", "  + zValue + ", " + pValue );
		}
	}

}
