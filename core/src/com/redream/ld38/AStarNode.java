package com.redream.ld38;

import java.util.ArrayList;

public class AStarNode implements Comparable<Object>{
	AStarNode pathParent;
  	float costFromStart;
  	float estimatedCostToGoal;
  	float x;
  	float y;
  
  	public ArrayList<AStarNode> neighbors = new ArrayList<AStarNode>();
	public boolean targeted;


	public float getCost() {
	    return costFromStart + estimatedCostToGoal;
	}
	
	public float getCost(AStarNode node) {
		return (float) Math.sqrt(Math.pow(x-node.x, 2) + Math.pow(y-node.y, 2));
	}
	
	public float getEstimatedCost(AStarNode node) {
		return (float) Math.sqrt(Math.pow(x-node.x, 2) + Math.pow(y-node.y, 2));
	}
	
	
	public ArrayList<AStarNode> getNeighbors(){
		return neighbors;
	}
	  
	public void addNeighbor(AStarNode neighbor){
		neighbors.add(neighbor);
	}
	
	public void removeNeighbor(AStarNode neighbor){
		neighbors.remove(neighbor);
	}

	@Override
	public int compareTo(Object o) {
		float thisValue = this.getCost();
	    float otherValue = ((AStarNode) o).getCost();
	
	    float v = thisValue - otherValue;
	    return (v>0)?1:(v<0)?-1:0; // sign function
	}

}
