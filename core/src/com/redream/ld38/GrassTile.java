package com.redream.ld38;

import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GrassTile extends Tile {
	
	public ArrayList<Tree> trees;
	public GoldRock rock;
	public Stone stone;
	public boolean canSpawn = false;
	public ArrayList<Enemy> enemies;

	public GrassTile(World world) {
		super(BodyType.KinematicBody, false, world, 16, 16, 3, 3);
		this.tex = 2;
		this.origX = 24;
		this.origY = 24;
		this.trees = new ArrayList<Tree>();
		this.enemies = new ArrayList<Enemy>();
	}
	
	public void tick(){
		for(int i=0;i<trees.size();i++){
			trees.get(i).tick();
			if(trees.get(i).destroy){
				trees.remove(i--);
			}
		}
		if(rock != null){
			rock.tick();
			if(rock.destroy)rock = null;
		}
		if(stone != null){
			stone.tick();
			if(stone.destroy)stone = null;
		}
		
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).tick();
//			System.out.println(enemies.get(i).x+" "+enemies.get(i).y);
			if(enemies.get(i).remove){
				enemies.remove(i--);
			}
		}
		
		if(HUD.dark && canSpawn && new Random().nextInt((int) Math.max(500,(2500-(HUD.time/20)))) == 0){
			Enemy e = new Enemy(world);
			e.setPosition(tileX*48, tileY*48);
			this.enemies.add(e);
		}
	}
	
	public void addTrees(int n){
		for(int i=0;i<n;i++){
			Tree t = new Tree();
			t.x = tileX*48 + new Random().nextFloat()*48;
			t.y = tileY*48 + new Random().nextFloat()*48;
			t.tileX = tileX;
			t.tileY = tileY;
			t.z = HUD.HUD_Z-tileY;
			this.trees.add(t);
		}
	}
	
	public void addGoldRock(){
		GoldRock g = new GoldRock();
		g.x = tileX*48;
		g.y = tileY*48 ;
		g.tileX = tileX;
		g.tileY = tileY;
		g.z = HUD.HUD_Z-tileY;
		this.rock = g;
	}
	
	public boolean isOccupied(){
		return stone != null || rock != null;
	}
	
	public void addStone(){
		Stone s = new Stone();
		s.x = tileX*48;
		s.y = tileY*48 ;
		s.tileX = tileX;
		s.tileY = tileY;
		s.z = HUD.HUD_Z-tileY;
		this.stone = s;
	}

	
	public void queueRender(Display display){
		super.queueRender(display);
		
		if(Game.isWaterTileAt(tileX-1, tileY+1)
				&& Game.isWaterTileAt(tileX, tileY+1)
				&& Game.isWaterTileAt(tileX-1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 8;
			corner.width = 4;
			corner.height = 4;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x-corner.width*4-2;
			corner.y = y+corner.height*4+2;
			corner.applyCam = true;
			corner.mirrorY = true;
			corner.queueRender(display);
		}
		
		if(Game.isWaterTileAt(tileX+1, tileY+1)
				&& Game.isWaterTileAt(tileX, tileY+1)
				&& Game.isWaterTileAt(tileX+1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 8;
			corner.width = 4;
			corner.height = 4;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x+corner.width*4+2;
			corner.y = y+corner.height*4+2;
			corner.mirrorX = true;
			corner.mirrorY = true;
			corner.applyCam = true;
			corner.queueRender(display);
		}
		
		if(Game.isWaterTileAt(tileX-1, tileY-1)
				&& Game.isWaterTileAt(tileX, tileY-1)
				&& Game.isWaterTileAt(tileX-1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 8;
			corner.width = 4;
			corner.height = 4;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x-corner.width*4-2;
			corner.y = y-corner.height*4-2;
			corner.mirrorX = false;
			corner.applyCam = true;
			corner.queueRender(display);
		}

		if(Game.isWaterTileAt(tileX+1, tileY-1)
				&& Game.isWaterTileAt(tileX, tileY-1)
				&& Game.isWaterTileAt(tileX+1, tileY)){
			Sprite corner = new Sprite();
			corner.tex = 8;
			corner.width = 4;
			corner.height = 4;
			corner.xScale = 3;
			corner.yScale = 3;
			corner.x = x+corner.width*4+2;
			corner.y = y-corner.height*4-2;
			corner.mirrorX = true;
			corner.applyCam = true;
			corner.queueRender(display);
		}
		
		for(Tree t : trees){
			t.queueRender(display);
		}
		
		for(Enemy e : enemies){
			e.queueRender(display);
		}
		
		if(rock != null){
			rock.queueRender(display);
		}
		
		if(stone != null){
			stone.queueRender(display);
		}
	}

}
