package com.redream.ld38;


import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Unit extends Entity {

	private boolean down;
	private boolean up;
	private boolean left;
	private boolean right;
	public LinkedList<AStarNode> path;
	public AStarNode targetedNode;
	public int timer = 100;
	private float randVel = 150;
	public boolean selected;
	
	public int resourceGold = 0;
	public int resourceWood = 0;
	public int resourceStone = 0; 
	
	public int swordHealth = 0;
	public int shieldHealth = 0;
	public int health = 10;
	
	public ResourceBuilding lastResourceBuilding;
	public boolean isBeingAttacked = false;
	
	public int attackCooldown = 20;
	
	public Building home;
	public int sliding;
	public Enemy targetedBy;
	
	public Unit(World world) {
		super(BodyType.KinematicBody, true, world, 6, 8, 2, 2);
		this.tex = 19;
		this.applyCam = true;
	}
	
	public void queueRender(Display display){
		if(swordHealth > 0){
			Sprite sword = new Sprite();
			sword.tex = 38;
			sword.width = 3;
			sword.height = 4;
			sword.applyCam = true;
			sword.xScale = 2;
			sword.yScale = 2;
			sword.x = x+8;
			sword.y = y+2;
			sword.z = 20;
			sword.queueRender(display);
		}
		if(shieldHealth > 0){
			Sprite shield = new Sprite();
			shield.tex = 37;
			shield.width = 3;
			shield.height = 4;
			shield.applyCam = true;
			shield.xScale = 2;
			shield.yScale = 2;
			shield.x = x-6;
			shield.y = y+2;
			shield.z = 20;
			shield.queueRender(display);
		}
		if(this.isBeingAttacked){
			Sprite health = new Sprite();
			health.tex = 13;
			health.width = 1;
			health.height = 1;
			health.applyCam = true;
			health.xScale = this.health;
			health.yScale = 4;
			health.x = this.x-3;
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
			health2.xScale = 10-this.health;
			health2.yScale = 4;
			health2.x = x-3+health.xScale;
			health2.y = y+17;
			health2.color = Color.RED;
			health2.disableOrig = true;
			health2.queueRender(display);
		}
		super.queueRender(display);
	}
	
	public void tick(){
		if(this.targetedBy != null && targetedBy.remove){
			targetedBy = null;
			this.isBeingAttacked = false;
		}
		if(new Random().nextInt(200) == 0 && this.health < 10){
			this.health++;
		}
		if(attackCooldown > 0){
			attackCooldown--;
		}else if(this.targetedBy != null && this.swordHealth > 0 && this.targetedBy.getCenter().sub(getCenter()).len() < 50){
			targetedBy.body.setLinearVelocity(targetedBy.getCenter().sub(getCenter()).scl(20));
			this.setPosition(targetedBy.getCenter());
			targetedBy.color = Color.RED;
			targetedBy.sliding = 20;
			targetedBy.health--;
			System.out.println(targetedBy.health);
			if(targetedBy.health == 0){
				this.targetedBy = null;
				this.isBeingAttacked = false;
			}
			attackCooldown = 100;
		}
		if(sliding > 0){
			sliding--;
		}else{
			color = Color.WHITE;
			body.setLinearVelocity(0, 0);
		}
		if(health <= 0){
			this.remove = true;
		}
		if(sliding == 0)this.color = selected ? Color.GREEN : Color.WHITE;
		this.mirrorX = this.body.getLinearVelocity().x < 0;
				
 		if(this.targetedNode != null && targetedNode.targeted){
			timer--;
		}else{
			this.tex = 19;
		}
		if(timer < 0){
			timer = 10;
			this.tex = tex == 19 || tex == 21 ? 20 :  21;
		}
//		float vY = 0;
//		float vX = 0;
//		int origTex = tex;
//		if(down){
//			vY = -40;
//			if(origTex == 5 && !left && !right){
//				this.body.setTransform(x-2, y, 0);
//			}
//			this.tex = 3;
//			this.width = 7;
//		}
//		if(up){
//			vY = 40;
//			this.tex = 4;
//			if(origTex == 5 && !left && !right){
//				this.body.setTransform(x-2, y, 0);
//			}
//			this.width = 7;
//		}
//		if(left){
//			vX = -40;
//			this.tex = 5;
//			this.mirrorX = true;
//			this.width = 3;
//			if(origTex == 3 || origTex == 4){
//				this.body.setTransform(x+2, y, 0);
//			}
//		}
//		if(right){
//			vX = 40;
//			this.tex = 5;
//			if(origTex == 3 || origTex == 4){
//				this.body.setTransform(x+2, y, 0);
//			}
//			this.mirrorX = false;
//			this.width = 3;
//		}
//		this.body.setLinearVelocity(vX, vY);
		
		if(path != null && !path.isEmpty()){
			AStarNode next_node = path.getFirst();
			targetedNode = path.getLast();
			Vector2 next_pos = new Vector2(next_node.x*48-12, next_node.y*48-12);
			if(path.size() == 1){
				next_pos.x += new Random().nextFloat()*20;
				next_pos.y += new Random().nextFloat()*20;
			}
			Vector2 pos = new Vector2(this.x,this.y);
			this.body.setLinearVelocity(next_pos.sub(pos).scl(this.randVel));
//			System.out.println(next_pos.sub(pos).len());
			if(next_pos.sub(pos).len() < 2100){
//				System.out.println("AAAAA");
				this.randVel = new Random().nextFloat()* 250 + 50;
				path.pop();
				next_node.targeted = false;
				this.body.setLinearVelocity(0, 0);
				if(path.size() == 0){
					int index = (int) (next_node.x+next_node.y*Game.levelWidth);
					if(index > 0 && index < Game.tiles.length && Game.tiles[index] != null){
//						System.out.println(Game.tiles[index].building+" "+x+" "+y);
//						Game.tiles[index].color = Color.BLUE;
						if(Game.tiles[index].building instanceof ResourceBuilding){
							ResourceBuilding r = ((ResourceBuilding)Game.tiles[index].building);
							Game.resourceWood += this.resourceWood;
							Game.resourceGold += this.resourceGold;
							Game.resourceStone += this.resourceStone;
							this.resourceWood = 0;
							this.resourceStone = 0;
							this.resourceGold = 0;
							this.path = r.getNearestResource();
							this.swordHealth = 0;
							this.shieldHealth = 0;
							this.lastResourceBuilding = r;
						}else if(Game.tiles[index] instanceof GrassTile){
							if(((GrassTile)Game.tiles[index]).trees.size() > 0){
								Tree t = ((GrassTile)Game.tiles[index]).trees.get(0);
								this.resourceWood += t.getResource();
								this.path = t.getNearestResourceBuilding(Resource.TYPE_WOOD);
							}else if(((GrassTile)Game.tiles[index]).stone != null){
								Stone s = ((GrassTile)Game.tiles[index]).stone;
								this.resourceStone += s.getResource();
								this.path = s.getNearestResourceBuilding(Resource.TYPE_STONE);
							}else if(((GrassTile)Game.tiles[index]).rock != null){
								GoldRock g = ((GrassTile)Game.tiles[index]).rock;
								this.resourceGold += g.getResource();
								this.path = g.getNearestResourceBuilding(Resource.TYPE_GOLD);
							}else if(Game.tiles[index].building != null && Game.tiles[index].building instanceof ArmoryBuilding){
								this.shieldHealth = 10;
							}else if(Game.tiles[index].building != null && Game.tiles[index].building instanceof BarracksBuilding){
								this.swordHealth = 10;
							}else if(this.lastResourceBuilding != null){
							
								this.path = lastResourceBuilding.getNearestResource();
							}
						}
					}
				}
			}
		}
	}
	
	public boolean keyDown(int keycode){
//		if(keycode == Keys.W){
//			up = true;
//			down = false;	
//		}
//		if(keycode == Keys.A){
//			left = true;
//			right = false;
//		}
//		if(keycode == Keys.S){
//			up = false;
//			down = true;
//		}
//		if(keycode == Keys.D){
//			left = false;
//			right = true;
//		}
		return true;
	}
	
	public boolean keyUp(int keycode){
//		if(keycode == Keys.W){
//			up = false;
//		}
//		if(keycode == Keys.A){
//			left = false;
//		}
//		if(keycode == Keys.S){
//			down = false;
//		}
//		if(keycode == Keys.D){
//			right = false;
//		}
		return true;
	}

	public boolean touchDown(int x, int y, int pointer){	
		
		if(pointer == 1){
			if(!selected)return true;
			this.lastResourceBuilding = null;
			Vector2 pos = Camera.screenToCoords(new Vector2(x, y));
			
			x = (int) pos.x + 24;
			y = (int) pos.y + 24;
			
			if(targetedNode != null)targetedNode.targeted = false;
			
			int i = (int)(x/48)+Game.levelWidth*((int)y/48);
			
			if(i > Game.nodes.length || i < 0){
				return true;
			}
			AStarNode target = Game.nodes[i];
			
			if(target == null){
				return true;
			}
			
			int s_i = (int)((this.x+this.width*this.xScale/2+24)/48)+Game.levelWidth*((int)(this.y+24+this.height*this.yScale/2)/48);
			
			if(s_i > Game.nodes.length || s_i < 0){
				return true;
			}
			
			AStarNode start = Game.nodes[s_i];
			
			if(start == null){
				return true;
			}
			
			AStarSearch search = new AStarSearch();
			path = search.findPath(true, start, target);
		
		}
		return true;
	}
	
	public boolean touchCollTest(){
		return false;
	}
	
	public float origY(){
		return 2;
	}
	
	public float boxHeight(){
		return 4;
	}
	
	public float boxWidth(){
		return 2;
	}
	

}
