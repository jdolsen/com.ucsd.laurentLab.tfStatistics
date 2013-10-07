package com.ucsd.laurentLab.tfStatistics;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TFStatObject {

	String id;
	NetworkMember subNetworkMembership ;
	boolean isInSubnetwork = true; //all statistics objects are for TFs in a subnetwork by default
	HashMap<String, StatObject> statObjects = new HashMap<String, StatObject>();
	ArrayList<String> sources = new ArrayList<String>();
	ArrayList<String> targets = new ArrayList<String>();
	
	public static final String SOURCES = " sources";
	public static final String TARGETS = " targets";
	
	TFStatObject(String id, String subnet, double weight){
		this.id = id;
		subNetworkMembership = new NetworkMember(subnet, weight);
		statObjects.put(SOURCES, new StatObject(id, SOURCES));
		statObjects.put(TARGETS, new StatObject(id, TARGETS));
	}
	
	public void addStatObject(String statId){
		statObjects.put(statId, new StatObject(id, statId));
	}
	
	public void addSubnetObjects(HashSet<String> subnetNames){
		for(String s : subnetNames){
			statObjects.put(s+TARGETS, new StatObject(id, s+TARGETS));
			statObjects.put(s+SOURCES, new StatObject(id, s+SOURCES));
		}
	}
	
	public void addSourceOrTarget(String statObjectId, String id){
		statObjects.get(statObjectId).incrementConnectionCount();
		if(statObjectId.equals(SOURCES)){
			sources.add(id);
		}else if (statObjectId.equals(TARGETS)){
			targets.add(id);
		}
	}
	
	public StatObject getStatObject(String name){
		return statObjects.get(name);		
	}
	
	public boolean isInSubnetwork() {
		return isInSubnetwork;
	}

	public void setInSubnetwork(boolean isInSubnetwork) {
		this.isInSubnetwork = isInSubnetwork;
	}

	public ArrayList<String> getSources() {
		return sources;
	}

	public void setSources(ArrayList<String> sources) {
		this.sources = sources;
	}

	public ArrayList<String> getTargets() {
		return targets;
	}
	
	public void setTargets(ArrayList<String> targets) {
		this.targets = targets;
	}

	public NetworkMember getSubNetworkMembership() {
		return subNetworkMembership;
	}

	public void setSubNetworkMembership(NetworkMember subNetworkMembership) {
		this.subNetworkMembership = subNetworkMembership;
	}

	public void updateAll() {
		for(String s : statObjects.keySet()){
			statObjects.get(s).addCountToList();
		}
	}
	
	public void printAll(PrintWriter outFile){
		for(String s : statObjects.keySet()){
			statObjects.get(s).print(outFile);
		}
	}

	public void setStats(PrintWriter outFile) {
		for(String s : statObjects.keySet()){
			statObjects.get(s).setStats();
		}	
		printAll(outFile);
	}
}
