package com.ucsd.laurentLab.tfStatistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class BindingList {

	int numShuffles = 0;
	ArrayList<String> sources = new ArrayList<String>();
	ArrayList<String> targets = new ArrayList<String>();
	ArrayList<String> randomTargets = new ArrayList<String>();
	HashMap<String, TFStatObject> tfMap = new HashMap<String, TFStatObject>();
	
	public void addSubnetNode(String node, String subnet, double weight){
		tfMap.put(node, new TFStatObject(node, subnet, weight));
	}
	
	public void addPair(String src, String tgt) {	
		//System.out.println("Adding source: " + src +", target: " + tgt);
		sources.add(src);
		targets.add(tgt);
		randomTargets.add(tgt);
		
		//ensure the source collects statistics for its target and vice versa.  ONLY ADD SRC-TGT PAIRS THAT ARE BOTH IN-NETWORK
		if(tfMap.containsKey(src) && tfMap.containsKey(tgt)){	
			tfMap.get(src).addSourceOrTarget(TFStatObject.TARGETS, tgt);	
			tfMap.get(tgt).addSourceOrTarget(TFStatObject.SOURCES, src);	
		}
	}
	
	public void addSubnets(HashSet<String> subnetNames){
		//add tfstatobjects for the subnets themselves
		for(String s: subnetNames){
			tfMap.put(s, new TFStatObject(s, s, 1));
		}
		
		//add stat objects to all tfStatobjects
		for(String s: tfMap.keySet()){
			tfMap.get(s).addSubnetObjects(subnetNames);
		}	
	}
	
	public void populateSubnets(){
		//for each stat object, iterate its sources and targets list to see if it has connections to another subnet
		//Update accordingly
		for(String keys: tfMap.keySet()){
			
			TFStatObject obj = tfMap.get(keys);
			ArrayList<String> sources = obj.getSources();
			ArrayList<String> targets = obj.getTargets();
			String keyNet = obj.getSubNetworkMembership().networkId;
			//note that the sources and targets for subnet tfstat objects will be empty (and therefore safe)
			
			for(String s : sources){
				String net = tfMap.get(s).getSubNetworkMembership().networkId;
				obj.getStatObject(net+TFStatObject.SOURCES).incrementConnectionCount();
				//update subnet objects
				tfMap.get(keyNet).getStatObject(net+TFStatObject.SOURCES).incrementConnectionCount();
			}
			for(String s : targets){
				String net = tfMap.get(s).getSubNetworkMembership().networkId;
				obj.getStatObject(net+TFStatObject.TARGETS).incrementConnectionCount();
				//update subnet objects
				tfMap.get(keyNet).getStatObject(net+TFStatObject.TARGETS).incrementConnectionCount();
			}			
			
		}
	}
	
	public void doShuffle(){
		Collections.shuffle(randomTargets, new Random(Calendar.getInstance().getTimeInMillis()));
		//for each source, check to see if it and its random target are in the network. 

		for(int i = 0; i < sources.size(); ++i){
			//ignore sources that are not in subnetworks
			if(tfMap.containsKey(sources.get(i)) && tfMap.containsKey(randomTargets.get(i))){
				//get TF				 
				TFStatObject srcTf = tfMap.get(sources.get(i));
				TFStatObject tgtTf = tfMap.get(randomTargets.get(i));
				TFStatObject srcNet = tfMap.get(srcTf.getSubNetworkMembership().networkId);
				TFStatObject tgtNet = tfMap.get(tgtTf.getSubNetworkMembership().networkId);
				
				//get the sources stat object for the source tf to increment its number of targets		
				srcTf.getStatObject(TFStatObject.TARGETS).incrementCount();
				//also increment its subnetwork target or source relationships
				srcTf.getStatObject(tgtTf.getSubNetworkMembership().getNetworkId()+TFStatObject.TARGETS).incrementCount();
								
				//get the targets stat object for the target tf to increment its number of sources		
				tgtTf.getStatObject(TFStatObject.SOURCES).incrementCount();	
				//also increment its subnetwork target or source relationships
				tgtTf.getStatObject(srcTf.getSubNetworkMembership().getNetworkId()+TFStatObject.SOURCES).incrementCount();	
				
				//update the counts for the subnets
				srcNet.getStatObject(tgtTf.getSubNetworkMembership().getNetworkId() + TFStatObject.TARGETS).incrementCount();
				tgtNet.getStatObject(srcTf.getSubNetworkMembership().getNetworkId() + TFStatObject.SOURCES).incrementCount();				
			}			
		}
		
		//update all the stat objects for this shuffle
		for(String s : tfMap.keySet()){
			tfMap.get(s).updateAll();
		}
	}
	
	public void calculateStats(){
		System.out.println("Source, Statistic, mean, standard deviation, number of connections, z, p");
		for(String s : tfMap.keySet()){
			//TODO: somehow order things between setting stats and then printing them out
			tfMap.get(s).setStats();
		}
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

}
