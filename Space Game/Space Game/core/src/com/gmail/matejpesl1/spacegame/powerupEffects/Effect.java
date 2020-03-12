package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public interface Effect {
	public Texture getIcon();
	public float getMaxEffectDuration();
	public float getRemainingDuration();
	public void remove();
	public Hitbox getHitbox();
	public void update(float delta, float x, float y);
	public void render(SpriteBatch batch);
	public boolean shouldBeRemoved();
	public void start();
	public void handleCollision();
}
