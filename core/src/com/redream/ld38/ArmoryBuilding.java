package com.redream.ld38;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class ArmoryBuilding extends Building {

	public ArmoryBuilding(boolean hud, World world, int tileX, int tileY) {
		super(BodyType.StaticBody, hud, world, tileX, tileY, 9, 9, 3, 3);
		this.tex = 36;
		this.costStone = 100;
		this.costWood = 20;
		this.title = "Armory";
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

}
