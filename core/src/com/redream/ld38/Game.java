package com.redream.ld38;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Game extends ApplicationAdapter implements InputListener {
	public static int WIDTH;
	public static int HEIGHT;
	
	public static float DIAGDIST;
	
	public static float screenRatioY;
	public static float screenRatioX;
	public static boolean debug;
	
	private SpriteBatch batch;
	private Display display;
	private Input input;
	private RayHandler rayHandler;
	
	private double unprocessed;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	public static final double TICK_TIME = 0.0166667;
	public static final int TOTAL_RAYS = 1000;
	private Array<Body> bodies;
	
	static Tile[] tiles = new Tile[30000];
	static AStarNode[] nodes = new AStarNode[30000];
	public static boolean[] occupied = new boolean[30000];
	static Array<Unit> addQueue;
	
	private PointLight pointLight;
	
	static ArrayList<Unit>	units;
	private int dragLastX;
	private int dragLastY;
	private boolean dragging;
	static int levelWidth = 100;
	
	public HUD hud;
	private int startX;
	private int startY;
	private boolean startGame;
	
	public static Filter filterGround = new Filter();
	public static int resourceWood = 100;
	public static int resourceGold = 100;
	public static int resourceStone = 100;
	
	public static final int MENU = 1;
	public static final int PLAYING = 2;
	public static final int WON = 3;
	public static final int LOST = 4;
	public static final int INTRO = 5;
	public static int state = Game.MENU;
	
	public String[] dialog = new String[]{
			"Many moons ago, the gods created a miniature universe for their entertainment.",
			"Every night, dark spirits are unleashed and attack the villagers.",
			"It is your job to protect these villagers and provide the leadership needed to survive.",
			"The night is very dark, and lamps must be placed to help protect against the spirits."
	};
	
	public int dialogFade = 0;
	public int dialogTimer = 5;
	public int dialogStep = 0;
	private String activeDialog = "";
	
	@Override
	public void create () {
		Input.registerListener(this);
		this.resize(1280, 960);
		
		this.batch = new SpriteBatch();
		this.display = new Display(this.batch);
		this.debugRenderer = new Box2DDebugRenderer();
		
		initGame();
	}
	
	public void registerListeners(){
		Input.registerListener(hud);
	}
	
	public void initGame(){
		resourceWood = 100;
		resourceGold = 100;
		resourceStone = 100;
		world = new World(new Vector2(0, 0), true);
		this.rayHandler = new RayHandler(world);
		rayHandler.setBlur(true);
		filterGround.groupIndex = -1;
		
		this.input = new Input();
		Gdx.input.setInputProcessor(input);
		
		hud = new HUD(world, rayHandler);
		
		
		
		bodies = new Array<Body>();
		new ArrayList<GrassTile>();
		addQueue = new Array<Unit>();
		
		units = new ArrayList<Unit>();
		
		Game.levelWidth = Resources.level.getWidth();
		int levelHeight = Resources.level.getHeight();

		
		for(int x = 0;x<levelWidth;x++){
			for(int y = 0; y<levelHeight; y++){
				Color c = new Color();
				Color.rgba8888ToColor(c, Resources.level.getPixel(x, levelHeight-y));
				int r = (int)(c.r*255);
				if(r != 0)System.out.println(r);
				if(r == 1){
					WaterTile wt = new WaterTile(world);
					wt.setPosition(x*48, y*48);
					wt.tileX = x;
					wt.tileY = y;
					wt.applyCam = true;
					tiles[x+y*levelWidth] = wt;
				}else{
					GrassTile gt = new GrassTile(world);
					gt.setPosition(x*48, y*48);
					gt.tileX = x;
					gt.tileY = y;
					gt.applyCam = true;
					if(r == 2){
						gt.addTrees(4);
					}else if(r == 4){
						gt.addGoldRock();
					}else if(r == 3){
						gt.addStone();
					}else if(r == 5){
						startX = x*48;
						startY = y*48;
					}else if(r == 6){
						gt.canSpawn = true;
					}
					
					nodes[x+y*levelWidth] = new AStarNode();
					nodes[x+y*levelWidth].x = gt.tileX;
					nodes[x+y*levelWidth].y = gt.tileY;
					tiles[x+y*levelWidth] = gt;
				}
			}
		}
		
		Camera.cam.position.x += startX;
		Camera.cam.position.y += startY;
		Camera.cam.update();
		
		for(int i = 0;i<tiles.length;i++){
			if(tiles[i] != null && nodes[i] != null){
				tiles[i].addNeighbors();
			}
		}
	}
	
	public static Tile tileAt(int x, int y){
		if(x < 0 || x > levelWidth - 1 || y < 0 || y > levelWidth - 1){
			return null;
		}
		return tiles[x+y*levelWidth];
	}
	
	public static boolean isTileAt(int x, int y){
		return tileAt(x,y) != null;
	}
	
	public static boolean isGrassTileAt(int x, int y) {
		return tileAt(x, y) instanceof GrassTile;
	}
	
	public static boolean isWaterTileAt(int x, int y) {
		return tileAt(x, y) instanceof WaterTile;
	}

	@Override
	public void render () {
		if (this.unprocessed < 3) {
			this.unprocessed += Game.timeDelta();
		}

		if (this.unprocessed > 1) {
			this.tick();
			this.unprocessed -= 1;
		}
		
		this.batch.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for(int i=0; i<tiles.length;i++){
			if(tiles[i] != null){
				tiles[i].queueRender(display);
			}
		}
		
		if(state == Game.MENU || state == Game.INTRO || state == Game.LOST){
			Gdx.gl.glClearColor(1,1,1,1);
			Sprite logo = new Sprite();
			logo.tex = 41;
			logo.width = 27;
			logo.height = 18;
			logo.xScale = 5;
			logo.yScale = 5;
			logo.applyCam = true;
			logo.disableOrig = true;
			logo.x = -logo.width*logo.xScale/2+500;
			logo.y = 50+2000;
			logo.queueRender(display);
			Camera.cam.position.x = 500;
			Camera.cam.position.y = 2000;
			Camera.cam.update();
		}
		
		if(state == Game.MENU){
			Font f1 = new Font("Press SPACEBAR to begin.", 500, 2000, Font.POS_CENTER);
			f1.applyCam = true;
			f1.queueRender(display);
		}else if(state == Game.LOST){
			Font f1 = new Font("You have lost :(", 500, 2000, Font.POS_CENTER);
			f1.applyCam = true;
			f1.queueRender(display);
		}else if(state == Game.INTRO){
			if(dialogTimer == 0 && dialogFade < dialog[this.dialogStep].length()){
				activeDialog = activeDialog + dialog[this.dialogStep].charAt(dialogFade);
				dialogFade++;
				dialogTimer = 3;
			}else{
				this.dialogTimer--;
			}
			if(activeDialog != ""){
				Font text = new Font(activeDialog, 500, 2000, Font.POS_CENTER);
				text.applyCam = true;
				text.scale = 0.5f;
				text.z = HUD.HUD_Z;
				text.queueRender(display);
			}
		}else if(state == Game.PLAYING){
			Gdx.gl.glClearColor(0,0,0,1);
			
			
			for(Unit u : units){
				u.queueRender(display);
			}
			hud.queueRender(display);
		}
		
//		this.resourceStone = 1000;
//		this.resourceWood = 1000;
		
		
		this.display.render();
//		debugRenderer.render(this.world, Camera.cam.combined);
		
		this.display.renderQueue.clear();
		this.display.renderQueueHUD.clear();
		if(state == Game.PLAYING){
			rayHandler.setAmbientLight(Color.BLACK);
			rayHandler.setCombinedMatrix(Camera.cam.combined);
			rayHandler.updateAndRender();
		}
		this.batch.end();
	}
	
	private void tick() {
		if(startGame && state == Game.INTRO){
			this.registerListeners();
			state = Game.PLAYING;
			Camera.cam.position.x = startX;
			Camera.cam.position.y = startY;
		}
		if(state == Game.PLAYING){
			for(Unit u : addQueue){
				if(u != null){
					bodies.add(u.body);
					Input.registerListener(u);
					units.add(u);
				}
			}
			for(int i=0; i<tiles.length;i++){
				if(tiles[i] != null){
					tiles[i].tick();
				}
			}
			
			for(int i=0;i<units.size();i++){
				units.get(i).tick();
				if(units.get(i).remove){
					if(units.get(i).home != null){
						Tile t = Game.tileAt(units.get(i).home.tileX,units.get(i).home.tileY);
						if(world != null && t != null && t.building != null)world.destroyBody(t.building.body);
						t.building = null;
					}
					units.remove(i--);
				}
			}
			hud.tick();
			
			addQueue.clear();
			world.step((float) TICK_TIME, 6, 2);
			world.getBodies(bodies);
			for (Body b : bodies) {
			    Entity e = (Entity) b.getUserData();

			    if (e != null) {
			        e.setPosition(b.getPosition().x, b.getPosition().y);
			        e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
			    }
			}
			if(Camera.cam.position.x < 20){
				Camera.cam.position.x = startX;
				Camera.cam.position.y = startY;
			}
			Camera.tick();

			if(units.size() == 0 && (resourceWood < 20 || resourceStone < 5)){
				Game.state = Game.LOST;
			}
		}
	}
	
	public void resize(int width, int height) {
		float dratio = (float)width/(float)height;

		Game.WIDTH = (int) (960 * dratio);
		Game.HEIGHT = 960;
		
		Gdx.graphics.setWindowedMode(Game.WIDTH, Game.HEIGHT);
		
		Game.screenRatioX = (float)Game.WIDTH / (float)width;
		Game.screenRatioY = (float)Game.HEIGHT / (float)height;
		
		DIAGDIST = (float) Math.abs(Math.sqrt(Math.pow(-Game.WIDTH, 2.0D) + Math.pow(Game.HEIGHT, 2.0D)));
		
		Camera.cam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.HUDcam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.cam.zoom = 0.4f;
		Camera.cam.update();
	}

	public static double timeDelta() {
		return Gdx.graphics.getDeltaTime() / TICK_TIME;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public boolean touchCollTest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE && Game.state == Game.INTRO){
			if(this.dialogStep + 1 == this.dialog.length){
				this.startGame = true;
			}else{
				this.dialogStep++;
				this.dialogFade = 0;
				this.activeDialog = "";
				this.dialogTimer = 3;
			}
		}else if(keycode == Keys.SPACE && Game.state == Game.MENU){
			state = Game.INTRO;
		}else if(keycode == Keys.SPACE && Game.state == Game.LOST){
			state = Game.MENU;
			this.initGame();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) {
		if(pointer == 2){
			this.dragging = true;
			y = Game.HEIGHT - y;
			this.dragLastX = x;
			this.dragLastY = y;
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) {
		if(pointer == 2){
			this.dragging = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		if(dragging){
			y = Game.HEIGHT - y;
			Camera.cam.position.x -= (x - this.dragLastX) * Camera.cam.zoom;
			Camera.cam.position.y -= (y - this.dragLastY) * Camera.cam.zoom;
			this.dragLastX = x;
			this.dragLastY = y;
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
