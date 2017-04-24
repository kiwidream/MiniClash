package com.redream.ld38;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Tile extends Entity {
	public int tileX;
	public int tileY;
	public Building building;
	
	public Tile(BodyType bodyType, boolean collidable, World world, int width, int height, int xScale, int yScale) {
		super(bodyType, collidable, world, 16, 16, 3, 3);
		this.tex = 2;
	}

	public void addNeighbors() {
		if(Game.nodes[tileX+tileY*Game.levelWidth] != null){
			Game.nodes[tileX+tileY*Game.levelWidth].neighbors.clear();
			for(int x=tileX-1;x<=tileX+1;x++){
				for(int y=tileY-1;y<=tileY+1;y++){
					if((x != tileX || y != tileY) && (x == tileX || y == tileY) && Game.isGrassTileAt(x, y) && Game.nodes[x+y*Game.levelWidth] != null && !Game.occupied[x+y*Game.levelWidth]){
						Game.nodes[tileX+tileY*Game.levelWidth].addNeighbor(Game.nodes[x+y*Game.levelWidth]);
					}
				}
			}
		}
	}
	
	public void addBuilding(Building b){
		this.building = b;
		
		
		building.tileX = tileX;
		building.tileY = tileY;
		building.x = tileX*48;
		building.y = tileY*48-building.height*building.yScale/6;
		building.disableOrig = false;
		building.setPosition(building.x, building.y);
		this.building.onAdd();
//		Game.occupied[tileX+tileY*Game.levelWidth] = true;
		for(int x=tileX-1;x<=tileX+1;x++){
			for(int y=tileY-1;y<=tileY+1;y++){
				if(Game.tiles[x+y*Game.levelWidth] != null){
					Game.tiles[x+y*Game.levelWidth].addNeighbors();
				}
			}
		}
		
	}
	
	public void queueRender(Display display){
		if(Game.nodes[tileX+tileY*Game.levelWidth] != null){
			if(Game.nodes[tileX+tileY*Game.levelWidth].targeted){
				Sprite crosshair = new Sprite();
				crosshair.applyCam = true;
				crosshair.tex = 11;
				crosshair.width = 5;
				crosshair.height = 5;
				crosshair.xScale = 3;
				crosshair.yScale = 3;
				crosshair.x = x;
				crosshair.y = y;
				crosshair.z = 5;
				crosshair.queueRender(display);
			}
		}
		if(building != null){
			building.queueRender(display);
		}
		super.queueRender(display);
	}
	
	public void tick(){
		if(building != null){
			building.tick();
			if(building.remove){
				world.destroyBody(body);
				building = null;
			}
		}
	}

}
