package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.entities.PowerUp.EffectEnum;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public class SlowerTime implements Effect {
	public static final EffectEnum EFFECT_NAME = EffectEnum.SLOWER_TIME;
	private final static Texture ICON = new Texture(Gdx.files.internal("textures/effect_icons/slowed_time.png"));
	private final static int MAX_EFFECT_DURATION = 8;
	private float remainingDuration;
	private final static float CHANGED_TIME_VALUE = 1.45f;
	Hitbox hitbox;
	public boolean remove;
	
	public SlowerTime() {
		hitbox = new Hitbox(10, 10, 10, 10);
		hitbox.remove();
		remainingDuration = MAX_EFFECT_DURATION;
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
		MainGameScreen.stopManipulatingDelta();
	}

	@Override
	public Hitbox getHitbox() {
		return hitbox;
	}

	@Override
	public void update(float delta, float x, float y) {
		remainingDuration -= Gdx.graphics.getDeltaTime();
		if (remainingDuration <= 0) {
			remove();
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
		MainGameScreen.manipulateDelta(Gdx.graphics.getDeltaTime()/CHANGED_TIME_VALUE);
	}

	@Override
	public void handleCollision() {
		
	}
}
