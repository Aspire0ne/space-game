package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.entities.Ship;
import com.gmail.matejpesl1.spacegame.entities.PowerUp.EffectEnum;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public class SmallerShip implements Effect {
	public static final EffectEnum EFFECT_NAME = EffectEnum.SMALLER_SHIP;
	private static final Texture ICON = new Texture(Gdx.files.internal("textures/effect_icons/smallerShip_icon.png"));
	private static final float MAX_EFFECT_DURATION = 10f;
	private float remainingDuration = MAX_EFFECT_DURATION;
	private Hitbox hitbox;
	private boolean remove;
	private static final int WIDTH = Ship.getDefaultWidth()/3;
	private static final int HEIGHT = Ship.getDefaultHeight()/3;
	
	
	public SmallerShip() {
		hitbox = new Hitbox(10, 10, 10, 10);
		hitbox.remove();
	}

	@Override
	public Texture getIcon() {
		return ICON;
	}

	@Override
	public float getMaxEffectDuration() {
		return MAX_EFFECT_DURATION;
	}

	@Override
	public float getRemainingDuration() {
		return remainingDuration;
	}

	@Override
	public void remove() {
		remove = true;
		Ship.revertDefaultDimensions();
		Ship.setContinuousShootingDelay(Ship.DEFAULT_CONTINUOUS_SHOOTING_DELAY);
		Ship.setSingleShotDelay(Ship.DEFAULT_SINGLE_SHOT_DELAY);
	}

	@Override
	public Hitbox getHitbox() {
		return hitbox;
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
	public boolean shouldBeRemoved() {
		return remove;
	}

	@Override
	public void start() {
		Ship.changeDimensions(WIDTH, HEIGHT);
		Ship.setContinuousShootingDelay(0.29f);
		Ship.setSingleShotDelay(0.22f);
	}
	
	@Override
	public void handleCollision() {
		
	}
}
