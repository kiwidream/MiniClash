package com.redream.ld38;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;


public class Sprite implements InputListener{
	public float x;
	public float y;
	public int z;
	
	public int tex;
	
	public int width;
	public int height;
	
	public boolean applyCam;
	
	public boolean mirrorX;
	public boolean mirrorY;
	
	public AtlasRegion ar;
	
	public int loadedTex;
	
	public float xScale;
	public float yScale;
	
	public Color color = Color.WHITE;
	public float origX;
	public float origY;
	public float rot;
	
	public AtlasRegion flipX(AtlasRegion ar,boolean x) {
		
		if((!Resources.flipX[this.tex] && x) || (Resources.flipX[this.tex] && !x)){
			ar.flip(true, false);
		}
		
		this.mirrorX = x;
		
		Resources.flipX[this.tex] = x;
		
		return ar;
	}
	
	public AtlasRegion flipY(AtlasRegion ar,boolean y){
		
		if((!Resources.flipY[this.tex] && y) || (Resources.flipY[this.tex] && !y)){
			ar.flip(false, true);
		}
		
		this.mirrorY = y;
		Resources.flipY[this.tex] = y;
		return ar;
	}
	
	public AtlasRegion flip(AtlasRegion ar,boolean x, boolean y){
		return flipX(flipY(ar,y),x);
	}

	public void queueRender(Display display) {
		display.queueRender(this);
	}

	public void tick() {

	}

	public AtlasRegion getTexture() {
		if(loadedTex != this.tex){
			if(Resources.regions[this.tex] == null){
				ar = Resources.atlas.findRegion(Integer.toString(this.tex));
				Resources.regions[this.tex] = ar;
			}else{
				ar = Resources.regions[this.tex];
			}
		}
		return flip(ar,this.mirrorX,this.mirrorY);
	}

	public boolean touchCollTest() {
		return false;
	}

	public Rectangle getBounds() {
		return new Rectangle(x,y,width*xScale,height*yScale);
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int x, int y, int pointer) {
		return false;
	}

	public boolean touchUp(int x, int y, int pointer) {
		return false;
	}

	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	public boolean touchMoved(int x, int y) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	public void render(SpriteBatch batch) {
		float dx = x-origX;// - Game.WIDTH / 2;
		float dy = y-origY;// - Game.HEIGHT / 2;

		AtlasRegion ar = getTexture();
		if(width == 0 && height == 0){
			width = ar.getRegionWidth();
			height = ar.getRegionHeight();
		}
		color = Color.WHITE;
		batch.setColor(color);
		batch.draw(ar, dx, dy, origX, origY, width, height, xScale, yScale, rot);
	}
	
}
