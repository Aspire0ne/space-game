package com.gmail.matejpesl1.spacegame.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.powerupEffects.BottomShot;
import com.gmail.matejpesl1.spacegame.powerupEffects.Effect;
import com.gmail.matejpesl1.spacegame.powerupEffects.FasterShooting;
import com.gmail.matejpesl1.spacegame.powerupEffects.Shield;
import com.gmail.matejpesl1.spacegame.powerupEffects.SlowerTime;
import com.gmail.matejpesl1.spacegame.powerupEffects.SmallerShip;
import com.gmail.matejpesl1.spacegame.tools.HUD;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public class PowerUp {
private Hitbox hitbox;
private static Random random = new Random();
private static final float MIN_SPAWN_DELAY = 17f;
private static final float MAX_SPAWN_DELAY = 22f;
private float duration;
private float x, y;
private static final int WIDTH = 50;
private static final int HEIGHT = 50;
private static final float DEFAULT_POWERUP_DURATION = 8f;
private EffectEnum currentEffectName;
private Effect currentEffect;
private boolean collected;
private static final float OFFSET = 80f;
private boolean justCollected;
public enum EffectEnum {FASTER_SHOOTING, SHIELD, BOTTOM_SHOT, SMALLER_SHIP, SLOWER_TIME};
private static EffectEnum lastEffectName = null;

	public PowerUp(Ship ship) {
		do {
			currentEffectName = EffectEnum.values()[random.nextInt(EffectEnum.values().length)];
		} while (currentEffectName == lastEffectName);
		lastEffectName = currentEffectName;
		
		if (Shield.EFFECT_NAME == currentEffectName) {
			Shield shield = new Shield();
			currentEffect = shield;
		} else if (FasterShooting.EFFECT_NAME == currentEffectName) {
			FasterShooting fasterShooting = new FasterShooting();
			currentEffect = fasterShooting;
		} else if (BottomShot.EFFECT_NAME == currentEffectName) {
			BottomShot bottomShot = new BottomShot();
			currentEffect = bottomShot;
		} else if (SmallerShip.EFFECT_NAME == currentEffectName) {
			SmallerShip smallerShip = new SmallerShip();
			currentEffect = smallerShip;
		} else if (SlowerTime.EFFECT_NAME == currentEffectName) {
			SlowerTime slowerTime = new SlowerTime();
			currentEffect = slowerTime;
		}
		
		duration = DEFAULT_POWERUP_DURATION;
		y = ship.getY() + Ship.getHeight()/2;
		
		do {
		x = random.nextInt(ControlCenter.WINDOW_WIDTH - WIDTH);
		} while (x > ship.getX() - OFFSET && x < ship.getX() + Ship.getWidth() + OFFSET);
		
		this.hitbox = new Hitbox(x, y, WIDTH, HEIGHT);
	}
	
	public void render(SpriteBatch batch, HUD hud) {
		if (collected) {
			hitbox.remove();
			hud.renderEffectInfo(batch, currentEffect, justCollected);
			if (justCollected) {
				justCollected = false;
			}
		} else {
			batch.draw(currentEffect.getIcon(), x, y, WIDTH, HEIGHT);
		}
	}
	
	public static float getRandomSpawnDelay() {
		return random.nextFloat() * (MAX_SPAWN_DELAY - MIN_SPAWN_DELAY) + MIN_SPAWN_DELAY;
	}
	
	public static int getPowerUpWidth() {
		return WIDTH;
	}
	
	public static int getPowerUpHeight() {
		return HEIGHT;
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public float getPowerUpDuration() {
		return duration;
	}
	
	public void decreasePowerUpDuration(float minusDuration) {
		this.duration -= minusDuration;
	}
	
	public void setCollected(boolean collected) {
		this.collected = collected;
		justCollected = true;
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public Effect getCurrentEffect() {
		return currentEffect;
	}
}
