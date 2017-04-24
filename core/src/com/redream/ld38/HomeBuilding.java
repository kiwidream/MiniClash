package com.redream.ld38;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class HomeBuilding extends Building {
	public Unit unit;
	
	public HomeBuilding(boolean hud, World world, int tileX, int tileY){
		super(BodyType.StaticBody, hud, world, tileX, tileY, 9, 9, 3, 3);
		this.tex = 12;
		this.costWood = 20;
		this.costStone = 5;
		this.title = "Home";
		this.desc = "Houses citizens";
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
		return 15;
	}
	
	public void onAdd(){
		Unit u = new Unit(this.world);
		u.setPosition(this.tileX*48+20, this.tileY*48);
		u.home = this;
		this.unit = u;
		System.out.println(this.tileX+" "+this.tileY);
		Game.addQueue.add(u);
	}
}
