package com.redream.ld38;

import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class ResourceBuilding extends Building {
	public int type = Resource.TYPE_WOOD;

	public ResourceBuilding(int type, boolean hud, World world, int tileX, int tileY) {
		super(BodyType.StaticBody, hud, world, tileX, tileY, 9, 11, 3, 3);
		this.type = type;
		this.tex = type == Resource.TYPE_WOOD ? 27 : type == Resource.TYPE_GOLD ? 30 : 31;
		this.applyCam = true;
		this.title = type == Resource.TYPE_WOOD ? "Wood Deposit" : type == Resource.TYPE_GOLD ? "Gold Deposit" : "Stone Deposit";
		
		if(type == Resource.TYPE_WOOD){
			this.costGold = 35;
			this.costStone = 35;
		}
		
		if(type == Resource.TYPE_STONE){
			this.costGold = 35;
			this.costWood = 35;
		}
		
		if(type == Resource.TYPE_GOLD){
			this.costWood = 35;
			this.costStone = 35;
		}
	}
	
	public float origY(){
		return 0;
	}
	
	public float origX(){
		return 4;
	}
	
	public float boxHeight(){
		return 2;
	}
	
	public float boxWidth(){
		return 14;
	}
	
	public LinkedList<AStarNode> getNearestResource(){
		float minDist = 1000;
		AStarNode minNode = null;
		for(Tile t : Game.tiles){
			if(t != null && Game.isGrassTileAt(t.tileX, t.tileY) && 
					(
					(type == Resource.TYPE_WOOD && ((GrassTile)Game.tileAt(t.tileX, t.tileY)).trees.size() > 0)
					|| (type == Resource.TYPE_GOLD && ((GrassTile)Game.tileAt(t.tileX, t.tileY)).rock != null)
					|| (type == Resource.TYPE_STONE && ((GrassTile)Game.tileAt(t.tileX, t.tileY)).stone != null)
					)
				){
				float dist = Game.tileAt(t.tileX,t.tileY).getCenter().sub(this.getCenter()).len();
				if(dist < minDist){
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
