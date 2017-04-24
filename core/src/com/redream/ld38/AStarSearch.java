package com.redream.ld38;

import java.util.LinkedList;
import java.util.List;

public class AStarSearch {
	/**
	    Construct the path, not including the start node.
	  */
	  protected LinkedList<AStarNode> constructPath(AStarNode node) {
		  LinkedList<AStarNode> path = new LinkedList<AStarNode>();
		  while (node.pathParent != null) {
			  path.addFirst(node);
			  node = node.pathParent;
		  }
		  return path;
	  }
	
	
	  /**
	    Find the path from the start node to the end node. A list
	    of AStarNodes is returned, or null if the path is not
	    found. 
	  */
	  public LinkedList<AStarNode> findPath(boolean showTarget, AStarNode startNode, AStarNode goalNode) {
	
		  PriorityList openList = new PriorityList();
		  LinkedList<AStarNode> closedList = new LinkedList<AStarNode>();
	
		  startNode.costFromStart = 0;
		  startNode.estimatedCostToGoal =
	      startNode.getEstimatedCost(goalNode);
		  
		  startNode.pathParent = null;
		  openList.add(startNode);
		  int iterations = 0;
		  while (!openList.isEmpty()) {
			  iterations++;
			  if(iterations > 40){
				  goalNode.targeted = false;
				  return null;
			  }
			  AStarNode node = (AStarNode)openList.removeFirst();
			  if (node == goalNode) {
				  // construct the path from start to goal
				  return constructPath(goalNode);
			  }
	
			  List<?> neighbors = node.getNeighbors();
			  
			  if(showTarget)goalNode.targeted = true;
//			  Game.tileAt((int)startNode.x, (int)startNode.y).color = Color.BLUE;
//			  
//			  Game.tileAt((int)goalNode.x, (int)goalNode.y).color = Color.RED;
			  
			  for (int i=0; i<neighbors.size(); i++) {
				  AStarNode neighborNode =
						  (AStarNode)neighbors.get(i);
				  boolean isOpen = openList.contains(neighborNode);
				  boolean isClosed =
						  closedList.contains(neighborNode);
				  float costFromStart = node.costFromStart +
						  node.getCost(neighborNode);
				  
				  
				 
	
				  // check if the neighbor node has not been
				  // traversed or if a shorter path to this
				  // neighbor node is found.
				  if ((!isOpen && !isClosed) ||
						  costFromStart < neighborNode.costFromStart)
				  {
					  	neighborNode.pathParent = node;
					  	
					  	
					  	 
					  	neighborNode.costFromStart = costFromStart;
					  	neighborNode.estimatedCostToGoal =
					  	neighborNode.getEstimatedCost(goalNode);
					  	if (isClosed) {
					  		closedList.remove(neighborNode);
					  	}
					  	if (!isOpen) {
					  		openList.add(neighborNode);
					  	}
				  }
			  }
			  closedList.add(node);
		  }
	
		  // no path found
		  return null;
	  }
}
