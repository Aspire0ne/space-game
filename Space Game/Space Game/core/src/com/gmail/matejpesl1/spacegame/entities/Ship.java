package com.gmail.matejpesl1.spacegame.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;

public class Ship{
	private final static Texture TRANSPARENT_BACKGROUND_RED = new Texture(Gdx.files.internal("textures/backgrounds/transparent_background_red.png"));
	public Animation<TextureRegion>[] rolls;
	public static final float DEFAULT_CONTINUOUS_SHOOTING_DELAY = 0.33f;
	public static final float DEFAULT_SINGLE_SHOT_DELAY = 0.26f;
	private static float continuousShootingDelay;
	private static float singleShotDelay;
	private static final float DEFAULT_SPEED = 450;
	private static final float ANIMATION_SPEED = 0.5f;
	private static final int REAL_WIDTH = 17;
	private static final int REAL_HEIGHT = 32;
	public static final int DEFAULT_HEIGHT = REAL_HEIGHT * 3;
	public static final int DEFAULT_WIDTH = REAL_WIDTH * 3;
	private static int height = DEFAULT_HEIGHT;
	private static int width = DEFAULT_WIDTH;
	private static final float ROLL_DELAY = 0.09f;
	private static final float DEFAULT_Y = 15;
	public static int hitboxHeightOffset = (int)(height/1.371f);
	private static int hitboxYOffset = (int)(height/2.133f);
	private float x, y;
	private static Hitbox hitbox;
	private int rollState;
	private float timeFromLastRoll;
	private float health;
	private static final float HIT_EFFECT_DURATION = 0.8f;
	private float hitEffectTimer;
	public static enum Direction {LEFT, RIGHT, NEUTRAL}; //Delete up and donwn TODO
	private final static float SHIP_FLOATING_DISTANCE = 25f;
	boolean floatUp = true;
	
	
	@SuppressWarnings("unchecked")
	public Ship() {
		continuousShootingDelay = DEFAULT_CONTINUOUS_SHOOTING_DELAY;
		singleShotDelay = DEFAULT_SINGLE_SHOT_DELAY;
		health = 0.5f;
		rolls = new Animation[5];
		rollState = 2;
		x = ControlCenter.WINDOW_WIDTH/2 - width/2;
		y = DEFAULT_Y;
		hitbox = new Hitbox(x, y, width, height - hitboxHeightOffset);
		
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("textures/ship/ship.png"), REAL_WIDTH, REAL_HEIGHT);
		rolls[0] = new Animation<TextureRegion>(ANIMATION_SPEED, rollSpriteSheet[2]); // all left
		rolls[1] = new Animation<TextureRegion>(ANIMATION_SPEED, rollSpriteSheet[1]);
		rolls[2] = new Animation<TextureRegion>(ANIMATION_SPEED, rollSpriteSheet[0]); // no tilt
		rolls[3] = new Animation<TextureRegion>(ANIMATION_SPEED, rollSpriteSheet[3]);
		rolls[4] = new Animation<TextureRegion>(ANIMATION_SPEED, rollSpriteSheet[4]); // all right
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		if (isBehindWindow(x)) {
			System.out.println("X can't be larger or smaller than the size of the window!");
		} else {
			this.x = x;
		}	
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		if (isBehindWindow(y)) {
				System.out.println("X can't be larger or smaller than the size of the window!");
		} else {
				this.y = y;
		}
	}
	
	public boolean isBehindWindow(float coor) {
		return coor + width > ControlCenter.WINDOW_WIDTH || coor < 0;
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public Animation<TextureRegion> getRoll(int rollIndex) {
		if (rollIndex > rolls.length - 1 || rollIndex < rolls.length - 1) {
			System.out.println("Invalid roll[] index, index must not be larger than " + (rolls.length - 1) +
					" or smaller than 0. \n Returned neutral position (2).");
			return rolls[2];
		} else {
			return rolls[rollIndex];
		}
	}
	
	public void setRollState(int rollState) {
		if (rollState >= 0 && rollState <= 4) {
			this.rollState = rollState;
		}
		else {
			System.out.println("Invalid rollState! rollState must not be larger than " + (rolls.length - 1) + " or smaller than 0!");
		}
	}
	
	public int getRollState() {
		return rollState;
	}
	
	public float getTimeFromLastRoll() {
		return timeFromLastRoll;
	}
	
	public void setTimeFromLastRoll(float timeFromLastRoll) {
		this.timeFromLastRoll = timeFromLastRoll;
	}
	
	public void render(float stateTime, SpriteBatch batch, boolean hitTaken, float delta) {
		if (floatUp) {
			y += SHIP_FLOATING_DISTANCE * delta/1.4;
			if (y >= SHIP_FLOATING_DISTANCE) {
				floatUp = false;
			}
		}
		
		if (!floatUp) {
			y -= SHIP_FLOATING_DISTANCE * delta/1.4;
			if (y <= DEFAULT_Y) {
				floatUp = true;
			}
		}
		
		if (hitTaken == true) {
			hitEffectTimer = HIT_EFFECT_DURATION;
			hitEffectTimer -= delta;
		}
		if (hitEffectTimer > 0) {
			batch.draw(TRANSPARENT_BACKGROUND_RED, 0, 0, ControlCenter.WINDOW_WIDTH, ControlCenter.WINDOW_HEIGHT);
			batch.setColor(Color.RED);
			hitEffectTimer -= delta;
		}
		batch.draw(rolls[rollState].getKeyFrame(stateTime, true), x, y, width, height);
		batch.setColor(Color.WHITE);
		
	}
	
	public void updateHitbox() {
		hitboxYOffset = (int)(height/2.133f);
			hitbox.move(x, y + hitboxYOffset);
	}
	
	public static void changeDimensions(int width, int height) {
		Ship.width = width;
		Ship.height = height;
		hitboxHeightOffset = (int)(height/1.371f);
		hitbox.changeDimensions(width, height - hitboxHeightOffset);
	}
	
	public static void revertDefaultDimensions() {
		changeDimensions(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public void move(Direction direction, float delta) {
		if (direction.equals(Direction.RIGHT)) {
			x += Ship.DEFAULT_SPEED * delta;
			timeFromLastRoll -= delta;
			if (Math.abs(timeFromLastRoll) >= Ship.ROLL_DELAY) {
				if (rollState < 4) {
					++rollState;
					timeFromLastRoll = 0;
				}
			}
		}
		else if (direction.equals(Direction.LEFT)) {
			x -= Ship.DEFAULT_SPEED * delta;
			timeFromLastRoll -= delta;
			if (Math.abs(timeFromLastRoll) >= Ship.ROLL_DELAY) {
				if (rollState != 0) {
					--rollState;
					timeFromLastRoll = 0;
				}
			}
		}
		else if (direction.equals(Direction.NEUTRAL)) {
			timeFromLastRoll -= delta;
			if (Math.abs(timeFromLastRoll) > Ship.ROLL_DELAY) {
				if (rollState > 2) {
				--rollState;
				} else if (rollState < 2) {
					++rollState;
				  }
				timeFromLastRoll = 0;
			}
		}
	}
	
	public float getHealth() {
		return health;
	}

	public void decreaseHealth(float health) {
		this.health -= health;
	}
	
	public float getContinuousShootingDelay() {
		return continuousShootingDelay;
	}
	
	public static void setContinuousShootingDelay(float newContinuousShootingDelay) {
		continuousShootingDelay = newContinuousShootingDelay;
	}
	
	public float getSingleShotDelay() {
		return singleShotDelay;
	}
	
	public static void setSingleShotDelay(float newSingleShotDelay) {
		singleShotDelay = newSingleShotDelay;
	}
	
	public void getOutOfWall() {
		if (x + width > ControlCenter.WINDOW_WIDTH) {
			x = ControlCenter.WINDOW_WIDTH - Ship.width;
		} else if (x < 0) {
			x = 0;
		}
	}
	
	public static int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		Ship.width = width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		Ship.height = height;
	}
	
	public static int getDefaultWidth() {
		return DEFAULT_WIDTH;
	}
	
	public static int getDefaultHeight() {
		return DEFAULT_HEIGHT;
	}
	
	public boolean isUnhittable() {
		if (hitEffectTimer <= 0) {
			return false;
		} else {
			return true;
		}
	}
}
