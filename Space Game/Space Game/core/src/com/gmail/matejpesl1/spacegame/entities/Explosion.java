package com.gmail.matejpesl1.spacegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
	private static final float ANIMATION_SPEED = 0.19f;
	private static final int OFFSET = 8;
	public static final int SIZE = 32;
	
	private Animation<TextureRegion> animation;
	private float x, y;
	private float stateTime;
	public boolean remove;
	public static enum Colors {DEFAULT, BLUE, RED};
	private Colors color;
	
	public Explosion(float x, float y, Colors color) {
		this.color = color;
		animation = new Animation<TextureRegion>(ANIMATION_SPEED, TextureRegion.split(new Texture("textures/asteroid/explosion.png"), SIZE, SIZE) [0]);
		this.x = x - OFFSET;
		this.y = y - OFFSET;
	}
	
	public void update(float delta) {
		stateTime += delta;
		remove = animation.isAnimationFinished(stateTime);
	}
	
	public void render(SpriteBatch batch) {
		switch (color) {
		case DEFAULT: break;
		case RED: batch.setColor(Color.MAROON); break;
		case BLUE: batch.setColor(Color.BLUE); break;
		}
		batch.draw(animation.getKeyFrame(stateTime), x, y);
		batch.setColor(Color.WHITE);
	}
}
