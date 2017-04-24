package com.redream.ld38;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class WaterTile extends Tile {
	public int waterTimer = 100;
	
	public WaterTile(World world) {
		super(BodyType.KinematicBody, true, world, 16, 16, 3, 3);
		this.tex = 7;
		this.z = 9;
		this.origX = 24;
		this.origY = 24;
	}
	
	public void tick(){
		waterTimer--;
		if(waterTimer < 0){
			this.tex = this.tex == 7 ? 9 : 7;	
			this.waterTimer = new Random().nextInt(100);
		}
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
		body.setTransform(tileX*48, tileY*48, 0);
		super.tick();
	}
	
	public void queueRender(Display display){
		super.queueRender(display);
		
		if(Game.isGrassTileAt(tileX-1, tileY+1)
				&& Game.isGrassTileAt(tileX, tileY+1)
				&& Game.isGrassTileAt(tileX-1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 6;
			corner.width = 8;
			corner.height = 8;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x-corner.width-4;
			corner.y = y+corner.height+4;
			corner.z = 10;
			corner.applyCam = true;
			corner.queueRender(display);
		}
		
		if(Game.isGrassTileAt(tileX+1, tileY+1)
				&& Game.isGrassTileAt(tileX, tileY+1)
				&& Game.isGrassTileAt(tileX+1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 6;
			corner.width = 8;
			corner.height = 8;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x+corner.width+4;
			corner.y = y+corner.height+4;
			corner.z = 10;
			corner.mirrorX = true;
			
			corner.applyCam = true;
			corner.queueRender(display);
		}
		
		if(Game.isGrassTileAt(tileX-1, tileY-1)
				&& Game.isGrassTileAt(tileX, tileY-1)
				&& Game.isGrassTileAt(tileX-1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 6;
			corner.width = 8;
			corner.height = 8;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x-corner.width-4;
			corner.y = y-corner.height-4;
			corner.z = 10;
			corner.mirrorY = true;
			corner.applyCam = true;
			corner.queueRender(display);
		}

		if(Game.isGrassTileAt(tileX+1, tileY-1)
				&& Game.isGrassTileAt(tileX, tileY-1)
				&& Game.isGrassTileAt(tileX+1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 6;
			corner.width = 8;
			corner.height = 8;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x+corner.width+4;
			corner.y = y-corner.height-4;
			corner.z = 10;
			corner.mirrorX = true;
			corner.mirrorY = true;
			corner.applyCam = true;
			corner.queueRender(display);
		}
	}

}
