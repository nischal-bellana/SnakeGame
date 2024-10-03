package com.snake;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class scell {
	public static final float ox = 6;
	public static final float oy = 6;
	public scell next;
	public scell prev;
	public int x = 0;
	public int y = 0;
	public Sprite sp;
	public scell(int x, int y,TextureRegion tex){
		this.x = x;
		this.y = y;
		sp = new Sprite(tex);
		sp.setOrigin(6, 6);
		sp.setCenter(ox+12*x,oy+12*y);
	}
	
	public void sprpos() {
		sp.setCenter(ox+12*x,oy+12*y);
	}
}
