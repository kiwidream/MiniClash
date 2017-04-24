package com.redream.ld38;

import java.util.LinkedList;

public class Resource extends Sprite {
	static final int TYPE_WOOD = 1;
	static final int TYPE_GOLD = 2;
	static final int TYPE_STONE = 3;
	
	public int tileX;
	public int tileY;
	
	public int type;
	public int quantity;
	public boolean destroy = false;
	
	public int getResource(){
		this.quantity--;
		if(quantity <= 0){
			this.destroy = true;
		}
		return 1;
	}
	
	public LinkedList<AStarNode> getNearestResourceBuilding(int type){
		float minDist = 1000;
		AStarNode minNode = null;
		for(Tile t : Game.tiles){
			if(t != null && Game.isTileAt(t.tileX, t.tileY) && Game.tileAt(t.tileX, t.tileY).building instanceof ResourceBuilding){
				float dist = Game.tileAt(t.tileX,t.tileY).getCenter().sub(this.getCenter()).len();
				if(dist < minDist && ((ResourceBuilding)Game.tileAt(t.tileX, t.tileY).building).type == type){
					minNode = Game.nodes[t.tileX+t.tileY*Game.levelWidth];
					minDist = dist;
				}
			}
		}
		if(minNode != null){
			AStarNode startNode = Game.nodes[this.tileX+this.tileY*Game.levelWidth];
			if(startNode == null){
				return null;
			}
			AStarSearch search = new AStarSearch();
			return search.findPath(true, startNode, minNode);
		}else{
			return null;
		}
	} 
}
