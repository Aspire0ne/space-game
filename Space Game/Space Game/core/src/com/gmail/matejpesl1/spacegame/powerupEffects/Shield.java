package com.gmail.matejpesl1.spacegame.powerupEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.entities.Ship;
import com.gmail.matejpesl1.spacegame.entities.PowerUp.EffectEnum;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class Shield implements Effect {
	public final static EffectEnum EFFECT_NAME = EffectEnum.SHIELD;
	private final static float MAX_DURATION = 10f;
	private final static int OFFSET_WIDTH = 42;
	private final static int OFFSET_HEIGHT = 20;
	private final static Texture ICON = new Texture(Gdx.files.internal("textures/effect_icons/shield_icon.png"));
	private final static Texture TEXTURE = new Texture(Gdx.files.internal("textures/others/shield.png"));
	private static final int HEIGHT = OFFSET_HEIGHT*2;
	private static final int WIDTH = Ship.getWidth() + OFFSET_WIDTH*2;
	private float remainingDuration;
	private boolean remove;
	private float x, y;
	private Hitbox hitbox;
	
	public Shield() {
		remainingDuration = MAX_DURATION;
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
	public void update(float delta, float x, float y) {
		this.x = x - OFFSET_WIDTH;
		this.y = y + Ship.getHeight() - OFFSET_HEIGHT;
		remainingDuration -= delta;
		if (remainingDuration <= 0) {
			remove = true;
			remove();
		}
		if (this.x < 0) {
			this.x = 0; 
		} else if (this.x + WIDTH > ControlCenter.WINDOW_WIDTH) {
			this.x = ControlCenter.WINDOW_WIDTH - WIDTH;
		}
		
		if (remove != true) {
			hitbox.move(this.x, this.y);
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(TEXTURE, x, y, WIDTH, HEIGHT);
	}

	@Override
	public void remove() {
		remove = true;
		hitbox.remove();
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
	public void start() {
		hitbox = new Hitbox(x, y, WIDTH, HEIGHT);
	}

	@Override
	public void handleCollision() {
		MainGameScreen.increaseScore(MainGameScreen.scorePerAsteroid * 2);
		Sounds.getRandom("SHIELD_COLLISION").play();
	}
}
