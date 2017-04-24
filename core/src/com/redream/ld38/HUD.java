package com.redream.ld38;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

public class HUD extends Sprite {
	public static final int HUD_Z = 10000;

	public int selected = 1;
	
	public static float time = 60;
	
	private int showGhostX;
	private int showGhostY;
	private World world;
	
	public static boolean dark = false;
	
	public Building[] buildings;

	private RayHandler rayHandler;

	private Vector2 dragAreaStart;

	private Vector2 dragAreaEnd;
	
	public float scrollY = 0;

	private boolean scrolling;

	private float scrollStartY;

	private int nightCount = 0;
	
	public HUD(World world, RayHandler rayHandler){
		this.world = world;
		this.rayHandler = rayHandler;
		buildings= new Building[]{
			new HomeBuilding(true, world, 0, 0),
			new Lamp(true, world, 0, 0, rayHandler),
			new ResourceBuilding(Resource.TYPE_WOOD, true, world, 0, 0),
			new ResourceBuilding(Resource.TYPE_GOLD, true, world, 0, 0),
			new ResourceBuilding(Resource.TYPE_STONE, true, world, 0, 0),
			new BarracksBuilding(true, world, 0, 0),
			new ArmoryBuilding(true, world, 0, 0),
		};
	}
	
	public void queueRender(Display display){
		Sprite bg = new Sprite();
		bg.applyCam = true;
		bg.tex = 13;
		bg.width = 1;
		bg.height = 1;
		bg.xScale = 100;
		bg.yScale = Game.HEIGHT+1;
		Vector2 pos = Camera.screenToCoords(new Vector2(0,0));
		bg.color = new Color(1,1,1,0.2f);
		bg.x = pos.x;
		bg.y = pos.y-Game.HEIGHT;
		bg.z = HUD_Z;
		bg.disableOrig = true;
		bg.queueRender(display);
		
		Sprite wood = new Sprite();
		wood.applyCam = true;
		wood.tex = 16;
		wood.width = 3;
		wood.height = 5;
		wood.xScale = 3;
		wood.yScale = 3;
		wood.color = Color.WHITE;
		wood.disableOrig = true;
		wood.z = HUD_Z+1;
		wood.x = pos.x + Game.WIDTH/2 - 190;
		wood.y = pos.y - 30;
		wood.queueRender(display);
		
		Sprite coin = new Sprite();
		coin.applyCam = true;
		coin.tex = 15;
		coin.width = 3;
		coin.height = 4;
		coin.xScale = 3;
		coin.yScale = 3;
		coin.color = Color.WHITE;
		coin.disableOrig = true;
		coin.z = HUD_Z+1;
		coin.x = pos.x + Game.WIDTH/2 - 190;
		coin.y = pos.y - 50;
		coin.queueRender(display);
		
		Sprite stone = new Sprite();
		stone.applyCam = true;
		stone.tex = 14;
		stone.width = 3;
		stone.height = 3;
		stone.xScale = 3;
		stone.yScale = 3;
		stone.color = Color.WHITE;
		stone.disableOrig = true;
		stone.z = HUD_Z+1;
		stone.x = pos.x + Game.WIDTH/2 - 190;
		stone.y = pos.y - 70;
		stone.queueRender(display);
		
		Sprite bg3 = new Sprite();
		bg3.applyCam = true;
		bg3.tex = 13;
		bg3.width = 1;
		bg3.height = 1;
		bg3.xScale = 100;
		bg3.yScale = 70;
		bg3.color = new Color(1,1,1,0.5f);
		bg3.x = pos.x + Game.WIDTH/2 - 200;
		bg3.y = pos.y - 80;
		bg3.z = HUD_Z;
		bg3.disableOrig = true;
		bg3.queueRender(display);
		
		Font f = new Font(Integer.toString(Game.resourceWood), 0, 10, Font.POS_LEFT);
		f.applyCam = true;
		f.z = HUD_Z;
		f.x = pos.x + Game.WIDTH/2 - 175;
		f.y = pos.y - 14;
		f.queueRender(display);
		
		Font f2 = new Font(Integer.toString(Game.resourceGold), 0, 10, Font.POS_LEFT);
		f2.applyCam = true;
		f2.z = HUD_Z;
		f2.x = pos.x + Game.WIDTH/2 - 175;
		f2.y = pos.y - 35;
		f2.queueRender(display);
		
		Font f3 = new Font(Integer.toString(Game.resourceStone), 0, 10, Font.POS_LEFT);
		f3.applyCam = true;
		f3.z = HUD_Z;
		f3.x = pos.x + Game.WIDTH/2 - 175;
		f3.y = pos.y - 56;
		f3.queueRender(display);
		
		int i = 1;
		for(Building b : buildings){
			if(selected == i){
				Sprite bg2 = new Sprite();
				bg2.applyCam = true;
				bg2.tex = 13;
				bg2.width = 1;
				bg2.height = 1;
				bg2.xScale = 100;
				bg2.yScale = 50;
				bg2.color = new Color(1,1,1,0.5f);
				bg2.x = pos.x;
				bg2.y = pos.y - i*50 + scrollY;
				bg2.z = HUD_Z;
				bg2.disableOrig = true;
				bg2.queueRender(display);
			}
			
			
			float by = pos.y - i*50 + 20 + scrollY;
			float bx = pos.x+20;
			b.y = by - b.height - 5;
			b.x = bx - b.width*1.5f;
			b.applyCam = true;
			b.disableOrig = true;
			b.z = HUD_Z;
			b.queueRender(display);
			i++;
			
			Font f4 = new Font(b.costWood+" wood", bx+20, by-5, Font.POS_LEFT);
			f4.applyCam = true;
			f4.z = HUD_Z;
			f4.scale = 0.5f;
			f4.queueRender(display);
			Font f5 = new Font(b.costGold+" gold", bx+20, by+5, Font.POS_LEFT);
			f5.applyCam = true;
			f5.z = HUD_Z;
			f5.scale = 0.5f;
			f5.queueRender(display);
			Font f6 = new Font(b.costStone+" stone", bx+20, by+15, Font.POS_LEFT);
			f6.applyCam = true;
			f6.z = HUD_Z;
			f6.scale = 0.5f;
			f6.queueRender(display);
			
			Font f7 = new Font(b.title, bx-14, by+29, Font.POS_LEFT);
			f7.applyCam = true;
			f7.z = HUD_Z;
			f7.scale = 0.5f;
			f7.queueRender(display);
		}
		
		Sprite scroll = new Sprite();
		scroll.applyCam = true;
		scroll.tex = 13;
		scroll.width = 1;
		scroll.height = 1;
		scroll.xScale = 10;
		scroll.yScale = 70;
		scroll.color = new Color(1,1,1,0.5f);
		scroll.x = pos.x + 100;
		scroll.y = pos.y - 70 - scrollY;
		scroll.z = HUD_Z;
		scroll.disableOrig = true;
		scroll.queueRender(display);
		
		if(selected > 0 && selected <= buildings.length 
				&& Game.isGrassTileAt(showGhostX, showGhostY)
				&& Game.tileAt(showGhostX, showGhostY).building == null
				&& !((GrassTile)Game.tileAt(showGhostX, showGhostY)).isOccupied()){
			Sprite ghost = new Sprite();
			Building b = buildings[selected-1];
			ghost.tex = b.tex;
			ghost.height = b.height;
			ghost.width = b.width;
			ghost.xScale = b.xScale;
			ghost.yScale = b.yScale;
			ghost.applyCam = true;
			ghost.x = showGhostX * 48;
			ghost.y = showGhostY * 48;
			ghost.color = new Color(1,1,1,0.5f);
			ghost.queueRender(display);
		}
		
		Sprite clock = new Sprite();
		clock.tex = 23;
		clock.width = 10;
		clock.height = 10;
		clock.applyCam = true;
		clock.xScale = 4;
		clock.yScale = 4;
		clock.x = 480 + pos.x;
		clock.y = pos.y-330;
		clock.z = HUD_Z;
		clock.color = new Color(1,1,1,0.5f);
		clock.queueRender(display);
		
		Sprite sun = new Sprite();
		sun.tex = 25;
		sun.width = 3;
		sun.height = 3;
		sun.xScale = 4;
		sun.applyCam = true;
		sun.yScale = 4;
		sun.x = (float) (475 + pos.x + Math.cos(Math.toRadians((time % 240) * 1.5 - 100))*30);
		sun.y = (float) (pos.y - 335 + Math.sin(Math.toRadians((time % 240) * 1.5 - 100))*30);
		sun.z = HUD_Z;
		sun.queueRender(display);
		
		Sprite moon = new Sprite();
		moon.tex = 24;
		moon.width = 3;
		moon.height = 3;
		moon.xScale = 4;
		moon.applyCam = true;
		moon.yScale = 4;
		moon.z = HUD_Z;
		moon.x = (float) (475 + pos.x + Math.cos(Math.toRadians((time % 240) * 1.5 - 100)+Math.PI)*30);
		moon.y = (float) (pos.y - 335 + Math.sin(Math.toRadians((time % 240) * 1.5 - 100)+Math.PI)*30);
		moon.queueRender(display);
		
		int unix = (int) (((time % 240) / 240) * 86400);
		
		RayHandler.useDiffuseLight(dark);
		if(dark && (unix > 21600 && unix < 64800) || !dark && (unix > 64800 || unix < 21600)){
			if(dark)nightCount++;
			dark = !dark;
			for(Tile t : Game.tiles){
				if(t != null && t.building instanceof Lamp){
					((Lamp)t.building).toggleLight();
				}
			}
		}
		
		if(unix > 50000 && unix < 64800){
			Font warn = new Font("It's getting dark soon! Be sure to place some lamps.", pos.x+280, pos.y-350, Font.POS_CENTER);
			warn.z = HUD_Z;
			warn.scale = 0.5f;
			warn.applyCam = true;
			warn.queueRender(display);
		}
		
		String timeStr = ((int)unix/3600)+":"+(String.format("%02d", (int)(unix % 3600) / 60));
		Font time = new Font(timeStr, 473 + pos.x, pos.y - 330, Font.POS_CENTER);
		time.z = HUD_Z;
		time.applyCam = true;
		time.scale = 0.5f;
		time.queueRender(display);
		
		if(this.dragAreaEnd != null && this.dragAreaStart != null){
			Sprite drag = new Sprite();
			drag.tex = 13;
			drag.width = 1;
			drag.height = 1;
			drag.applyCam = true;
			drag.x = dragAreaStart.x;
			drag.y = dragAreaStart.y;
			drag.xScale = dragAreaEnd.x - dragAreaStart.x;
			drag.yScale = dragAreaEnd.y - dragAreaStart.y;
			drag.disableOrig = true;
			drag.color = new Color(0, 0.8f, 0.17f, 0.5f);
			drag.z = HUD_Z;
			drag.queueRender(display);
		}
		
		Sprite bin = new Sprite();
		bin.tex = 40;
		bin.width = 5;
		bin.height = 5;
		bin.xScale = 3;
		bin.yScale = 3;
		bin.applyCam = true;
		bin.x = 120 + pos.x;
		bin.y = pos.y-20;
		bin.disableOrig = true;
		bin.z = HUD_Z;
		bin.queueRender(display);
		
		Font del = new Font("Delete", pos.x+155, pos.y-10, Font.POS_CENTER);
		del.z = HUD_Z;
		del.scale = 0.5f;
		del.applyCam = true;
		del.queueRender(display);
		
		Font count = new Font("Nights survived: "+nightCount, pos.x+155, pos.y-365, Font.POS_CENTER);
		count.z = HUD_Z;
		count.scale = 0.5f;
		count.applyCam = true;
		count.queueRender(display);
	}
	
	public boolean mouseMoved(int x, int y){
		if(selected > 0){
			Vector2 pos = Camera.screenToCoords(new Vector2(x,y));
			showGhostX = (int)((pos.x+24)/48);
			showGhostY = (int)((pos.y+24)/48);
		}
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer) {
		this.scrolling = false;
		if(pointer == 0){
			this.dragAreaEnd = null;
			this.dragAreaStart = null;
		}
		return true;
	}
	
	public boolean scrolled(int amount){
		this.scrollY += amount*10;
		this.scrollY = Math.max(Math.min(scrollY, 186), 0);
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		if(scrolling){
			this.scrollY += (y - this.scrollStartY)*Camera.cam.zoom;
			this.scrollStartY = y;
			this.scrollY = Math.max(Math.min(scrollY, 186), 0);
		}else if(pointer == 0 && dragAreaStart != null){ 
			this.dragAreaEnd = Camera.screenToCoords(new Vector2(x,y));
			Rectangle drag = new Rectangle(
					Math.min(dragAreaStart.x, dragAreaEnd.x),
					Math.min(dragAreaStart.y, dragAreaEnd.y),
					Math.abs(dragAreaStart.x - dragAreaEnd.x),
					Math.abs(dragAreaStart.y - dragAreaEnd.y));
			for(Unit u : Game.units){
				if(u != null && dragAreaStart != null){
//					u.color = Color.GREEN;
					if(drag.contains(new Vector2(u.x + u.width*u.xScale/2, u.y + u.height*u.yScale/2))){
						u.selected = true;
					}
				}
			}
			for(Tile t : Game.tiles){
				if(t != null && dragAreaStart != null){
					Building b = t.building;
					if(b != null && drag.contains(new Vector2(b.x + b.width*b.xScale/2, b.y + b.height*b.yScale/2))){
						b.selected = true;
					}
				}
			}
		}
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer){
//		System.out.println(x+" "+y);
		if(x > 300 && x < 338 && y > 10 && y < 50){
			for(Unit u : Game.units){
				if(u.selected){
					u.remove = true;
				}
			}
			for(Tile t : Game.tiles){
				if(t != null && t.building != null && t.building.selected){
					if(t.building instanceof HomeBuilding){
						((HomeBuilding)t.building).unit.remove = true;
					}
					t.building = null;
				}
			}
		}else if(x < 250){
			this.selected = (int)((y+125+scrollY*2.5)/125);
//			System.out.println(scrollY);
		}else if(selected > 0 && selected <= buildings.length && pointer == 0){
			Building b = (Building) buildings[selected-1].clone();
			b.genBox(b.bodyType);
			if(Game.isGrassTileAt(showGhostX, showGhostY) 
					&& Game.resourceWood >= b.costWood
					&& Game.resourceGold >= b.costGold
					&& Game.resourceStone >= b.costStone
					&& Game.tileAt(showGhostX, showGhostY).building == null
					&& !((GrassTile)Game.tileAt(showGhostX, showGhostY)).isOccupied()){
				Game.resourceGold -= b.costGold;
				Game.resourceStone -= b.costStone;
				Game.resourceWood -= b.costWood;
				Game.tileAt(showGhostX, showGhostY).addBuilding(b);
			}
			this.selected = 0;
		}else{
			if(x < 270){
				this.scrolling = true;
				this.scrollStartY = y;
			}else if(pointer == 0){ 
				this.dragAreaStart = Camera.screenToCoords(new Vector2(x,y));
				this.dragAreaEnd = null;
				for(Unit u : Game.units){
					u.selected = false;
				}
				for(Tile t : Game.tiles){
					if(t != null && t.building != null){
						t.building.selected = false;
					}
				}
			}
			this.selected = 0;
		}
		return true;
	}
	
	public boolean touchCollTest(){
		return false;
	}
	
	public void tick(){
		this.time += Gdx.graphics.getDeltaTime();
	}
	
}
