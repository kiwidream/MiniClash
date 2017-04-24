package com.redream.ld38;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import box2dLight.PointLight;

public class Lamp extends Building {
	private PointLight pointLight;
	private RayHandler rayHandler;
	private Color color = new Color(1,0.7f,0.7f,1f);

	public Lamp(boolean hud, World world, int tileX, int tileY, RayHandler rayHandler){
		super(BodyType.KinematicBody, hud, world, tileX, tileY, 3, 11, 3, 3);
		this.rayHandler = rayHandler;
		this.tex = 22;
		this.x = tileX*48;
		this.y = tileY*48;
		this.title = "Lamp";
		this.costGold = 10;
		this.costWood = 10;
		this.costStone = 10;
	}
	
	public void onAdd(){
		if(HUD.dark)this.toggleLight();
	}
	
	public void toggleLight(){
		if(pointLight == null){
			this.pointLight = new PointLight(rayHandler, Game.TOTAL_RAYS, color, 400, x, y+10);
			pointLight.setContactFilter(Game.filterGround);
		}
		
		if(!HUD.dark){
			this.pointLight.setColor(new Color(0,0,0,0));
		}else{
			this.pointLight.setColor(color);
		}
	}
}
