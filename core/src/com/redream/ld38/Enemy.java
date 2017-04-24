package com.redream.ld38;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Entity {
	
	public LinkedList<AStarNode> path;
	public AStarNode targetedNode;
	private float randVel = 150;
	private Unit target;
	private int attackCooldown = 20;
	
	public int sliding = 0;
	public int health = 5;
	private int waitTimer = 1000;

	public Enemy(World world) {
		super(BodyType.KinematicBody, true, world, 8, 8, 3, 3);
		this.tex = 39;
		this.z = 20;
		this.applyCam = true;
	}
	
	public void queueRender(Display display){
		Sprite health = new Sprite();
		health.tex = 13;
		health.width = 1;
		health.height = 1;
		health.applyCam = true;
		health.xScale = this.health*2;
		health.yScale = 4;
		health.x = this.x-4;
		health.y = this.y+17;
		health.z = 20;
		health.disableOrig = true;
		health.color = Color.GREEN;
		health.queueRender(display);
		Sprite health2 = new Sprite();
		health2.tex = 13;
		health2.width = 1;
		health2.height = 1;
		health2.applyCam = true;
		health2.xScale = 10-this.health*2;
		health2.yScale = 4;
		health2.x = this.x-4+health.xScale;
		health2.y = this.y+17;
		health2.z = 20;
		health2.disableOrig = true;
		health2.color = Color.RED;
		health2.queueRender(display);
		
		super.queueRender(display);
	}
	
	public void tick(){
		if(sliding > 0){
			sliding--;
		}else{
			color = Color.WHITE;
			body.setLinearVelocity(0, 0);
		}
		if(health <= 0 || !HUD.dark){
			this.remove = true;
		}

		if(waitTimer == 0){
			this.remove = true;
		}else{
			waitTimer--;
		}
		if(path != null && !path.isEmpty()){
			waitTimer = 500;
			AStarNode next_node = path.getFirst();
			targetedNode = path.getLast();
			Vector2 next_pos = new Vector2(next_node.x*48-12, next_node.y*48-12);
			if(path.size() == 1){
				next_pos.x += new Random().nextFloat()*20;
				next_pos.y += new Random().nextFloat()*20;
			}
			Vector2 pos = new Vector2(this.x,this.y);
			this.body.setLinearVelocity(next_pos.sub(pos).scl(this.randVel));
			if(next_pos.sub(pos).len() < 3000){
				this.randVel = new Random().nextFloat()* 250 + 50;
				path.pop();
				next_node.targeted = false;
				this.body.setLinearVelocity(0, 0);
				if(path.size() == 0){
					target.color = Color.RED;
					
					target.body.setLinearVelocity(target.getCenter().sub(getCenter()).scl(20));
					this.setPosition(target.getCenter());
					
					target.sliding = 20;
					
					target.shieldHealth--;
					target.swordHealth--;
					target.health--;
					if(target.shieldHealth == 0)target.health--;
					if(target.health == 0){
						target.remove = true;
						this.target = null;
					}
					
					this.attackCooldown = 50;
				}
			}
		}else{
			if(attackCooldown  > 0)attackCooldown--;
			for(Unit u : Game.units){
				if(u != null && (!u.isBeingAttacked || this.target == u || u.targetedBy == this) && attackCooldown == 0){
					int i = (int)(u.x/48)+Game.levelWidth*((int)u.y/48);
					
					if(i > Game.nodes.length || i < 0){
						return;
					}
					AStarNode target = Game.nodes[i];
					if(target == null){
						return;
					}
					
					int s_i = (int)((this.x+this.width*this.xScale/2+24)/48)+Game.levelWidth*((int)(this.y+24+this.height*this.yScale/2)/48);
					
					if(s_i > Game.nodes.length || s_i < 0){
						return;
					}
					
					AStarNode start = Game.nodes[s_i];
					
					if(start == null){
						return;
					}
					
					AStarSearch search = new AStarSearch();
					path = search.findPath(false, start, target);
					
					if(path != null){
						if(path.size() == 0){
							path.add(target);
						}
						u.isBeingAttacked = true;
						this.target = u;
						u.targetedBy = this;
					}
					return;
				}
			}

		}
	}

}
