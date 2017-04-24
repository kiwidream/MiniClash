package com.redream.ld38;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Camera {
	public static boolean disableCollisionX = false;
	public static boolean disableCollisionY = false;
	public static int BoundaryX = 10000;
	public static int BoundaryY = 0;
	public static int BoundaryStartX = 0;
	public static int BoundaryStartY = 0;

	public static float angle = 0;
	public static float scrollspeed = 0.05f;

	public static boolean disable = false;

	public static OrthographicCamera cam;
	public static OrthographicCamera HUDcam;
	private static Sprite target;

	public static void tick() {
		if(!disable){
			if (Gdx.input.isKeyPressed(Keys.W)) {
				cam.position.y += 4;
			}
			
			if (Gdx.input.isKeyPressed(Keys.A)) {
				cam.position.x -= 4;
			}

			if (Gdx.input.isKeyPressed(Keys.S)) {
				cam.position.y -= 4;
			}

			if (Gdx.input.isKeyPressed(Keys.D)) {
				cam.position.x += 4;
			}
			
			if (Gdx.input.isKeyPressed(Keys.NUM_9)) {
				cam.zoom -= 0.01;
			}

			if (Gdx.input.isKeyPressed(Keys.NUM_8)) {
				cam.zoom += 0.01;
			}

			if (Gdx.input.isKeyPressed(Keys.NUM_0)) {
				cam.zoom = 1;
			}

			if (Gdx.input.isKeyPressed(Keys.NUM_6)) {
				cam.rotate(-0.5f, 0, 0, 1);
				Camera.angle += 0.5;
			}

			if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
				cam.rotate(0.5f, 0, 0, 1);
				Camera.angle -= 0.5;
			}

			if (Gdx.input.isKeyPressed(Keys.NUM_7)) {
				Camera.reset();
				Camera.updatePos();
				Camera.angle = 0;
			}
		}
		
		if(Game.debug)Camera.cam.zoom = 10f;
		
		if(!Camera.disableCollisionY){
			if(cam.position.x/cam.zoom - Game.WIDTH/2 + 48 < 0){
				cam.position.x += 4;
			}
		}
		if(!Camera.disableCollisionX){
			if(cam.position.y/cam.zoom - Game.HEIGHT/2 + 48 < 0){
				cam.position.y += 4;
			}
		}

		cam.update();
	}
	
	public static void updatePos(){
		return;
//		if(target == null)return;
//		float goY = (target.y + (target.height*target.yScale)/2 - Game.HEIGHT / 2);
//		float goX = (target.x + (target.width*target.xScale)/2 - Game.WIDTH / 2);
//		cam.translate(goX, goY, 0);
//		cam.update();
	}
	
	public static void setTarget(Sprite target){
		Camera.target = target;
	}

	public static void reset() {
		Camera.cam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		scrollspeed = 0.05f;
		cam.zoom = 0.4f;
	}

	public static Vector2 screenToCoords(Vector2 screen) {
		Vector3 coords = Camera.cam.unproject(new Vector3(screen.x, screen.y, 0));
		return new Vector2(coords.x, coords.y);
	}
}

