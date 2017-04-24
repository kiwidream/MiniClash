package com.redream.ld38;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Building extends Entity implements Cloneable{
	public int tileX;
	public int tileY;
	public String title = "";
	public String desc = "";
	public int costWood;
	public int costGold;
	public int costStone;
	public boolean selected = false;
	
	public Building(BodyType bodyType, boolean hud, World world, int tileX, int tileY, int width, int height, int xScale, int yScale){
		super(bodyType, !hud, world, width, height, xScale, yScale);
		this.tileX = tileX;
		this.tileY = tileY;
	}
	
	public void queueRender(Display display){
		this.color = selected ? Color.GREEN : Color.WHITE;
		super.queueRender(display);
	}
	
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}

	public void onAdd() {
		
	}
}
