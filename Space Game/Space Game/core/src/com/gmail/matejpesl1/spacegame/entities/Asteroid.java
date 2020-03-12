package com.gmail.matejpesl1.spacegame.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen;
import com.gmail.matejpesl1.spacegame.tools.Hitbox;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class Asteroid {
	private final Texture TEXTURE = new Texture("textures/asteroid/asteroid.png");
	private final int DEFAULT_Y = ControlCenter.WINDOW_HEIGHT;
	private static final Random random = new Random();
	private int speed;
	private final static int MAX_SPEED = 300;
	private final static int MIN_SPEED = 250;
	private static final int WIDTH = 35, HEIGHT = 35;
	private static final float BEGINNING_MAX_SPAWN_DELAY = 0.54f;
	private static final float BEGINNING_MIN_SPAWN_DELAY = 0.3f;
	private float x, y;
	private static float currentMaxSpawnDelay = BEGINNING_MAX_SPAWN_DELAY;
	private static float currentMinSpawnDelay = BEGINNING_MIN_SPAWN_DELAY;
	private static final float MAX_SPAWN_DELAY_LOWER_LIMIT = 0.14f;
	public boolean remove;
	private Hitbox hitbox;
	private static boolean constantMaxSpeed;
	private static float timer;
	private static final int DEFAULT_NAVIGATED_ASTEROID_PROBABILITY = 90;
	private static int navigatedAsteroidProbability = DEFAULT_NAVIGATED_ASTEROID_PROBABILITY;
	public static enum Angle {LEFT, RIGHT, STRAIGHT, NAVIGATED};
	private static final float MINUS_SPAWN_DELAY = 0.02f;
	Angle angle;
	ControlCenter center;
	Ship ship;
	
	public Asteroid(ControlCenter center, Ship ship) {
		this.ship = ship;
		this.center = center;
		angle = chooseAngle();
		chooseStartingPoint();
		if (!constantMaxSpeed) {
			speed = MIN_SPEED + random.nextInt(MAX_SPEED - MIN_SPEED + 1);	
		} else {
			speed = MAX_SPEED;
		}
		hitbox = new Hitbox(x, y, WIDTH, HEIGHT);
		
	}
	
	public void update(float delta) {
		if (angle.equals(Angle.LEFT)) {
			x -= speed * delta;
		} else if (angle.equals(Angle.RIGHT)) {
			x += speed * delta;	
		} else if (angle.equals(Angle.NAVIGATED)) {
			if (x >= ship.getX() && x <= ship.getX() + Ship.getWidth() - WIDTH) {
				//don't update x
			} else {
				x = ship.getX() > x ? x + speed * delta : x - speed * delta;	
				y += speed/3f * delta;
			}
		}
		y -= speed * delta;
		hitbox.move(x, y);
		if (y < -HEIGHT) {
			remove = true;
		}
		if (MainGameScreen.getScore() >= 50000 && navigatedAsteroidProbability == DEFAULT_NAVIGATED_ASTEROID_PROBABILITY) {
			navigatedAsteroidProbability /= 2;
		}
	}
	
	public void chooseStartingPoint() {
		int xOffset = 0;
		int yOffset = 0;
		switch (random.nextInt(8)+1) {
		case 1: yOffset = 100; xOffset = 0; break;
		case 2: xOffset = 100; yOffset = 0; break;
		case 3: yOffset = 150; xOffset = 0; break;
		case 4: xOffset = 150; yOffset = 0; break;
		case 5: yOffset = 240; xOffset = 0; break;
		case 6: xOffset = 255; yOffset = 0; break;
		case 7: yOffset = 50; xOffset = 0; break;
		case 8: xOffset = 50; yOffset = 0; break;
		}
		switch (angle) {
		case LEFT: {
			x = ControlCenter.WINDOW_WIDTH - xOffset;
			y = DEFAULT_Y - yOffset;
		} break;
		case RIGHT: {
			x = 0 + xOffset;
			y = DEFAULT_Y - yOffset;
		} break;
		case STRAIGHT: {
			x = random.nextInt(ControlCenter.WINDOW_WIDTH - Asteroid.getWidth());
			y = DEFAULT_Y;
		} break;
		case NAVIGATED: {
			x = random.nextInt(2) == 1 ? 0 : ControlCenter.WINDOW_WIDTH;
			y = DEFAULT_Y;
		} 
		}
		
	}
	
	public Angle chooseAngle() {
		int randomNum = random.nextInt(4)+1;
		if ((random.nextInt(navigatedAsteroidProbability) + 1) == 1)  {
			return Angle.NAVIGATED;
		}
		
		if (randomNum == 1) {
			return Angle.LEFT; //25%
			
		} else if (randomNum == 2) {
			return Angle.RIGHT; //25%
			
		} else {
			return Angle.STRAIGHT; //50%
		}
	}

	public void render(SpriteBatch batch) {
		if (angle.equals(Angle.NAVIGATED)) {
			batch.setColor(Color.CYAN);
		}
		batch.draw(TEXTURE, x, y, WIDTH, HEIGHT);
		batch.setColor(Color.WHITE);
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static float getMaxSpawnDelay() {
		return BEGINNING_MAX_SPAWN_DELAY;
	}
	
	public static float getMinSpawnDelay() {
		return BEGINNING_MIN_SPAWN_DELAY;
	}
	
	public static void updateTimer(float delta) {
		if (currentMaxSpawnDelay <= MAX_SPAWN_DELAY_LOWER_LIMIT ||
				((currentMaxSpawnDelay <= (MAX_SPAWN_DELAY_LOWER_LIMIT + (MINUS_SPAWN_DELAY * 6)))
						&& MainGameScreen.getScore() < 50000) || //0.22
				((currentMaxSpawnDelay <= (MAX_SPAWN_DELAY_LOWER_LIMIT + (MINUS_SPAWN_DELAY * 4)))
						&& MainGameScreen.getScore() < 70000) || //0.18
				((currentMaxSpawnDelay <= (MAX_SPAWN_DELAY_LOWER_LIMIT + (MINUS_SPAWN_DELAY * 2)))
						&& MainGameScreen.getScore() < 100000)) { //0.14 (0.12)
			return;
		}
		timer += delta;
		if (timer >= 6) {
			timer = 0;
			currentMaxSpawnDelay -= MINUS_SPAWN_DELAY;
			if (currentMinSpawnDelay > 0.03f) {
				currentMinSpawnDelay -= 0.02f;	
			}
		}
		if (MainGameScreen.getScore() > 70000) {
			constantMaxSpeed = true;
		}
	}
	
	public static float getRandomRespawnDelay() {
		return random.nextFloat() * (currentMaxSpawnDelay - currentMinSpawnDelay) + currentMinSpawnDelay;
	}
	
	public static void reset() {
		currentMaxSpawnDelay = BEGINNING_MAX_SPAWN_DELAY;
		currentMinSpawnDelay = BEGINNING_MIN_SPAWN_DELAY;
		navigatedAsteroidProbability = DEFAULT_NAVIGATED_ASTEROID_PROBABILITY;
		constantMaxSpeed = false;
		timer = 0;
	}
	
	public static void playCollisionSound() {
		Sounds.getSound("EXPLOSION.0").play(0.72f);
	}
}
