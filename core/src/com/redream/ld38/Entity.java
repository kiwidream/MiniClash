package com.redream.ld38;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Entity extends Sprite {

	private Vector2 pull;
	public Body body;
	public World world;
	public BodyType bodyType;
	
	public boolean remove = false;
	
	public Entity(BodyType bodyType, boolean collidable, World world, int width, int height, int xScale, int yScale){
		
		this.width = width;
		this.height = height;
		this.xScale = xScale;
		this.yScale = yScale;
		this.world = world;
		this.bodyType = bodyType;
		
		if(collidable){
			this.genBox(bodyType);
		}
	}
	
	public void genBox(BodyType bodyType){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(x, y);
		this.body = world.createBody(bodyDef);
		
		body.setUserData(this);
		PolygonShape box = new PolygonShape();  

		box.setAsBox(boxWidth(),  boxHeight());
		
		FixtureDef def = new FixtureDef();
		if(this instanceof Unit || this instanceof Enemy){
			def.filter.groupIndex = -1;
		}else if(this instanceof WaterTile){
			def.filter.groupIndex = -1;
		}else{
			def.filter.groupIndex = 1;
		}
		
		def.shape = box;
		def.density = 0.4f; 
		def.friction = 4f;
		def.restitution = 0f;
		body.createFixture(def);
		box.dispose();
	}
	
	public float boxWidth(){
		return this.width*this.xScale/2;
	}
	
	public float boxHeight(){
		return this.height*this.yScale/2;
	}
	
	public void tick(){
		if(pull != null){
			Vector2 pull2 = this.pull.cpy();
			body.applyForce(pull2.sub(this.getCenter()).scl(100), this.getCenter(), true);
		}
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		if(this.body != null){
			this.body.setTransform(x, y, 0);
		}
	}
	
	public void setPosition(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
		if(this.body != null){
			this.body.setTransform(x, y, 0);
		}
	}

	public void setRotation(float f) {
		this.rot = f;
	}
	
	public boolean touchDown(int x, int y, int pointer) {
		this.pull = Camera.screenToCoords(new Vector2(x,y));
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		this.pull = Camera.screenToCoords(new Vector2(x,y));
		return true;
	}

	public boolean touchUp(int x, int y, int pointer) {
		this.pull = null;
		return true;
	}

}
