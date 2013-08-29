package com.ucsd.laurentLab.tfStatistics;

public class NetworkMember {
	String networkId;
	double membershipWeight;
	
	NetworkMember(String networkId, double weight){
		this.networkId = networkId;
		this.membershipWeight = weight;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		networkId = networkId;
	}

	public double getMembershipWeight() {
		return membershipWeight;
	}

	public void setMembershipWeight(double membershipWeight) {
		this.membershipWeight = membershipWeight;
	}


	@Override
	public boolean equals(Object obj) {
		return this.networkId.equals(((NetworkMember)obj).networkId);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	
}
