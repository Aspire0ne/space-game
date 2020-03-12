package com.gmail.matejpesl1.spacegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class Bullet {
	private static final Texture BULLET_TEXTURE = new Texture("textures/others/bullet.png");
	private static final int SPEED = 500;
	private static final int DEFAULT_WIDTH = 4;
	private static final int DEFAULT_HEIGHT = 13;
	private static int width = Math.round((DEFAULT_WIDTH * ((float)ControlCenter.WINDOW_WIDTH/ControlCenter.WINDOW_WIDTH)));
	private static int height = Math.round((DEFAULT_HEIGHT * ((float)ControlCenter.WINDOW_WIDTH/ControlCenter.WINDOW_HEIGHT)));
	private static final int Y_OFFSET = 10;
	private static boolean bulletWaiting;
	private float x, y;
	public boolean remove;
	private Hitbox hitbox;
	
	public Bullet(float x) {
		Sounds.getSound("BULLET_SHOT.0").play(0.35f);
		this.x = x;
		this.y = Ship.getHeight() - Y_OFFSET;
		hitbox = new Hitbox(x, y, width, height);
	}
	
	public Bullet(float x, float y) {
		this.x = x;
		this.y = y;
		hitbox = new Hitbox(x, y, width, height);
	}
	
	public void update(float delta) {
		y += SPEED * delta;
		hitbox.move(x, y);
		remove = y > ControlCenter.WINDOW_HEIGHT;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(BULLET_TEXTURE, x, y, width, height);
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public static boolean isWaiting() {
		return bulletWaiting;
	}
	
	public static void setWaiting(boolean waiting) {
		bulletWaiting = waiting;
	}

}