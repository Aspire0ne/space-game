package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.entities.Bullet;
import com.gmail.matejpesl1.spacegame.entities.PowerUp.EffectEnum;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class BottomShot implements Effect {
public static final EffectEnum EFFECT_NAME = EffectEnum.BOTTOM_SHOT;
public boolean remove;
private static final float MAX_SHOTS = 3;
private float remainingShots = MAX_SHOTS;
private static final float SHOTS_DELAY = 1.5f; 
private final static Texture ICON = new Texture(Gdx.files.internal("textures/effect_icons/bottomShot_icon.png"));
private static final float MAX_DURATION = SHOTS_DELAY * MAX_SHOTS;
private float remainingDuration = MAX_DURATION;
private Hitbox hitbox;
private float timeFromLastShot;
	
	public BottomShot() {
		hitbox = new Hitbox(10, 10, 10, 10);
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
		Sounds.getSound("BULLET_WAVE_BACKGROUND.0").stop();
		remove = true;
	}

	@Override
	public Hitbox getHitbox() {
		return hitbox;
	}

	@Override
	public void update(float delta, float notNeeded, float notNeeded2) {
		remainingDuration -= delta;
		timeFromLastShot += delta;
		if (remainingDuration <= 0) {
			remove = true;
		} else {
			if (remainingShots != 0 && timeFromLastShot >= SHOTS_DELAY) {
				for (int x = 50; x < ControlCenter.WINDOW_WIDTH - 50; x += 20) {
					MainGameScreen.addBullet(new Bullet(x, 0));	
				}
				Sounds.getSound("WAVE.0").play();
				timeFromLastShot = 0;
				--remainingShots;
			}
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
		Sounds.getSound("BULLET_WAVE_BACKGROUND.0").play(0.4f);
		timeFromLastShot = SHOTS_DELAY;
	}
	@Override
	public void handleCollision() {
		
	}
}
