package com.ucsd.laurentLab.tfStatistics;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class Loader {

	public static void main(String args[]){
		System.out.println("Starting...");
		File network = new File("All_TF_network.txt");
		File subnetworks = new File("TF_6Clusters_two columns.txt");
		BindingList bindingList = new BindingList();
		HashSet<String> subnetNames = new HashSet<String>();
		try {  
		 		 
			String line;
			
			//read in the subnetworks
			BufferedReader subNetworkReader = new BufferedReader(new FileReader(subnetworks));
			int numSubnetworkElements = 0;
			while((line = subNetworkReader.readLine()) != null){
				String[] values = line.split("\\s+");
				if(values[0].equalsIgnoreCase("tf_name")){
					continue;
				}
				String name = values[0];
				String subnet = values[1];
				double weight = 1;
				if(values.length>2){
					//weights are included in this subnet file
					weight = Double.parseDouble(values[2]);
				}
				
				bindingList.addSubnetNode(name, subnet, weight);
				numSubnetworkElements++;
				subnetNames.add(subnet);
			}
			System.out.println("!!!there are " + numSubnetworkElements + " subnetwork elements");
			String[] tempNames = new String[subnetNames.size()];
			//once all subnet members have been read and all subnets discovered, add subnet statistics
			bindingList.addSubnets(subnetNames);
			
			//read in the full network pairings
			BufferedReader networkReader = new BufferedReader(new FileReader(network));			
			while((line = networkReader.readLine()) != null){
				String[] values = line.split("\\s+");
				if(values[0].equalsIgnoreCase("source")){
					continue;
				}
				String source = values[0];
				String target = values[1];
				bindingList.addPair(source, target);
			}
			//once all the pairings are read, populate the statistics for subnets
			bindingList.populateSubnets();

			//do statistical shuffling
			int numShuffles = 1000;
			for(int i = 0; i < numShuffles; ++i){
				if(i%(numShuffles/10)==0 || i == numShuffles-1){
					System.out.println((i*100/numShuffles) + "% done...");
				}
				bindingList.doShuffle();
			}
			
			//use the results of the shuffle to calculate statistics 
			bindingList.calculateStats();
			
			networkReader.close();
			subNetworkReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
