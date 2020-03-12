package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.entities.Ship;
import com.gmail.matejpesl1.spacegame.entities.PowerUp.EffectEnum;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public class FasterShooting implements Effect {
	public static final EffectEnum EFFECT_NAME = EffectEnum.FASTER_SHOOTING;
	private static final float MAX_DURATION = 8f;
	private static final Texture ICON = new Texture(Gdx.files.internal("textures/effect_icons/fasterShooting_icon.png"));
	private static final float FASTER_SHOOTING = 0.14f;
	private float remainingDuration;
	private boolean remove;
	private Hitbox hitbox;
	
	public FasterShooting() {
		remainingDuration = MAX_DURATION;
		hitbox = new Hitbox(0, 0, 0, 0);
		hitbox.remove();
	}
	
	@Override
	public Texture getIcon() {
		return ICON;
	}
	
	@Override
	public float getMaxEffectDuration() {
		return MAX_DURATION;
	}
	
	@Override
	public float getRemainingDuration() {
		return remainingDuration;
	}
	
	@Override
	public void remove() {
		Ship.setContinuousShootingDelay(Ship.DEFAULT_CONTINUOUS_SHOOTING_DELAY);
		Ship.setSingleShotDelay(Ship.DEFAULT_SINGLE_SHOT_DELAY);
		remove = true;
	}

	@Override
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	@Override
	public boolean shouldBeRemoved() {
		return remove;
	}
	
	@Override
	public void update(float delta, float x, float y) {
		remainingDuration -= delta;
		if (remainingDuration <= 0) {
			remove = true;
		}	
	}
	
	@Override
	public void render(SpriteBatch batch) {
	}

	@Override
	public void start() {
		Ship.setContinuousShootingDelay(FASTER_SHOOTING);
		Ship.setSingleShotDelay(FASTER_SHOOTING);
	}

	@Override
	public void handleCollision() {
		
	}
}