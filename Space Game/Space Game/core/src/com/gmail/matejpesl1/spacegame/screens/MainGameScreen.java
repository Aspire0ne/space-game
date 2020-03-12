package com.gmail.matejpesl1.spacegame.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.entities.Asteroid;
import com.gmail.matejpesl1.spacegame.entities.Bullet;
import com.gmail.matejpesl1.spacegame.entities.Explosion;
import com.gmail.matejpesl1.spacegame.entities.PowerUp;
import com.gmail.matejpesl1.spacegame.entities.Ship;
import com.gmail.matejpesl1.spacegame.entities.Ship.Direction;
import com.gmail.matejpesl1.spacegame.powerupEffects.Effect;
import com.gmail.matejpesl1.spacegame.tools.HUD;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class MainGameScreen implements Screen {
public static final int scorePerAsteroid = 100;
private ControlCenter controlCenter;
private Ship ship;
private static ArrayList<Bullet> bullets;
private ArrayList<Asteroid> asteroids;
private ArrayList<Asteroid> collidedAsteroids;
private ArrayList<Explosion> explosions;
private ArrayList<PowerUp> powerUps;
private ArrayList<Effect> effects;

private float timeToNextAsteroidRespawn;
public static enum EndGameWay {RESTART, DIE, MENU};
private float timeFromLastContinuousShot;
private float timeFromLastSingleShot;
private float stateTime;
private static int score;
private float timeToNextPowerUp;
private HUD hud;
private boolean shipDied;
boolean hitTaken;
private Effect currentEffect;
PauseScreen pauseScreen;
static private boolean deltaIsManipulated;
static private float manipulatedDelta;
private static boolean escWasPressed;

	public MainGameScreen(ControlCenter controlCenter) {
		Sounds.getSound("STARTUP.dispose");
		Sounds.startBackgroundMusic();
		deltaIsManipulated = false;
		pauseScreen = new PauseScreen(controlCenter, this);
		score = 0;
		collidedAsteroids = new ArrayList<>();
		timeToNextAsteroidRespawn = Asteroid.getMinSpawnDelay();
		ship = new Ship();
		hud = new HUD(ship, this);
		explosions = new ArrayList<>();
		asteroids = new ArrayList<>(); 
		bullets = new ArrayList<>();
		powerUps = new ArrayList<>();
		effects = new ArrayList<>();
		timeToNextPowerUp = 5f;
		this.controlCenter = controlCenter;
	}
		
	@Override
	public void show() {
		
	}
	
	@Override
	public void render(float delta) {
		if (deltaIsManipulated) {
				delta = manipulatedDelta;
			}
		
		if (controlCenter.gamePaused) {
			delta = 0;
			renderEverything(0);
			return;
		} else {
			controlCenter.timeFromGameResume += Gdx.graphics.getDeltaTime();
			if (controlCenter.timeFromGameResume <= 1) {
				delta *= controlCenter.timeFromGameResume;
			}
		}
		
		if (!Sounds.getBackgroundMusic().isPlaying() && Sounds.backgroundMusicIsEnabled) {
			Sounds.getBackgroundMusic().play();
		}
		
		int bulletsOffset = setOffset();
		//handles entities respawn time
		timeToNextAsteroidRespawn -= delta;
		if (timeToNextAsteroidRespawn <= 0) {
			timeToNextAsteroidRespawn = Asteroid.getRandomRespawnDelay();
			asteroids.add(new Asteroid(controlCenter, ship));
		}
		
		timeToNextPowerUp -= delta;
		if (timeToNextPowerUp <= 0) {
			timeToNextPowerUp = PowerUp.getRandomSpawnDelay();
			powerUps.add(new PowerUp(ship));
		}

		//Handles single shots of a player
		timeFromLastSingleShot -= delta;
		if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.justTouched()) {
			if (Math.abs(timeFromLastSingleShot) >= ship.getSingleShotDelay()) {
				if (Bullet.isWaiting()) {
					Bullet.setWaiting(false);
				}
				timeFromLastSingleShot = 0;
				timeFromLastContinuousShot = 0;
				
				bullets.add(new Bullet(ship.getX() + bulletsOffset));
				bullets.add(new Bullet(ship.getX() + Ship.getWidth() - bulletsOffset));
			} else {
				Bullet.setWaiting(true);
			}
		}
		
		//shoots the bullet that the player tried to shoot too early, as soon as possible
		if (Bullet.isWaiting() && Math.abs(timeFromLastSingleShot) > ship.getSingleShotDelay()) {
			Bullet.setWaiting(false);
			timeFromLastSingleShot = 0;
			timeFromLastContinuousShot = 0;
			bullets.add(new Bullet(ship.getX() + bulletsOffset));
			bullets.add(new Bullet(ship.getX() + Ship.getWidth() - bulletsOffset));
		}
		
		//handles the holding of space key (continuous shooting)
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
			timeFromLastContinuousShot -= delta;
			if (Math.abs(timeFromLastContinuousShot) > ship.getContinuousShootingDelay()) {
				timeFromLastContinuousShot = 0;
				bullets.add(new Bullet(ship.getX() + bulletsOffset));
				bullets.add(new Bullet(ship.getX() + Ship.getWidth() - bulletsOffset));
			}
		}
		boolean bothKeysHeld = false;

		//adds entities that should be removed to "will be removed" list;
		ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
			for(Bullet bullet : bullets) {
				bullet.update(delta);
				if(bullet.remove) {
					bulletsToRemove.add(bullet);
				}
			}
		
		ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
			for (Asteroid asteroid : asteroids) {
				asteroid.update(delta);
				if (asteroid.remove) {
					asteroidsToRemove.add(asteroid);
				}
			}
			
		ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
			for (Explosion explosion : explosions) {
				explosion.update(delta);
				if(explosion.remove) {
					explosionsToRemove.add(explosion);
				}
			}
			
		//gets the ship back to neutral state if no movement button is held
		if (!Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.LEFT)) {
		ship.move(Direction.NEUTRAL, delta);
		}
		
		//handles the pressing of both movement buttons at the same time
		if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))) {
			bothKeysHeld = true;
		}
		
		//pauses game
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && controlCenter.timeFromGameResume >= 0.2f) {
			controlCenter.gamePaused = true;
			escWasPressed = true;
		}
		
		//moves ship to left
		if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && bothKeysHeld == false) {
			ship.move(Direction.LEFT, delta);
		}
		 
		//moves ship to right
		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && bothKeysHeld == false) {
			ship.move(Direction.RIGHT, delta);
		}
		
		//stops the ship from going behind the window
		
		ship.updateHitbox();
		ship.getOutOfWall();
		
		//checks for collision
		for (Bullet bullet : bullets) {
			for (Asteroid asteroid : asteroids) {
				if (bullet.getHitbox().collidesWith(asteroid.getHitbox())) {
					Asteroid.playCollisionSound();
					score += scorePerAsteroid;
					bulletsToRemove.add(bullet);
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), Explosion.Colors.DEFAULT));
				}
			}
		}
		for (Asteroid asteroid : asteroids) {
			if (asteroid.getHitbox().collidesWith(ship.getHitbox()) && !ship.isUnhittable()) {
				ship.decreaseHealth(0.1f);
				asteroidsToRemove.add(asteroid);
				explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), Explosion.Colors.RED));
				hitTaken = true;
				Asteroid.playCollisionSound();
				Sounds.getSound("HIT_TAKEN.0").play();
			}
			
			for (Asteroid asteroid2 : asteroids) {
				if (asteroid.getHitbox().collidesWith(asteroid2.getHitbox()) && !asteroid2.equals(asteroid)
						&& !collidedAsteroids.contains(asteroid) && !collidedAsteroids.contains(asteroid2)) {
					collidedAsteroids.add(asteroid);
					collidedAsteroids.add(asteroid2);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), Explosion.Colors.BLUE));
				}
			}
			
			for (Effect effect : effects) {
				if (asteroid.getHitbox().collidesWith(effect.getHitbox())) {
					Asteroid.playCollisionSound();
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), Explosion.Colors.DEFAULT));
					effect.handleCollision();
				}
			}
		}
		
		ArrayList<Effect> effectsToRemove = new ArrayList<>();
		ArrayList<PowerUp> powerUpsToRemove = new ArrayList<>();
		
		for(PowerUp powerUp : powerUps) {
			if (powerUp.getHitbox().collidesWith(ship.getHitbox())) {
				currentEffect = powerUp.getCurrentEffect();
				currentEffect.start();
				effects.add(currentEffect);
				powerUp.setCollected(true);
				Sounds.getSound("POWERUP_COLLECTED.0").play();
			}
			
			if (!powerUp.isCollected()) {
				if (powerUp.getPowerUpDuration() <= 0) {
					powerUpsToRemove.add(powerUp);
				} else {
					powerUp.decreasePowerUpDuration(delta);
				}
			}
			
			else if (powerUp.isCollected()) {
				if (currentEffect.shouldBeRemoved()) {
					powerUpsToRemove.add(powerUp);
					effectsToRemove.add(currentEffect);
				} else {
					currentEffect.update(delta, ship.getX(), ship.getY());
				}	
			}
		}
		
		//removes entities supposed to be removed
		bullets.removeAll(bulletsToRemove);
		asteroids.removeAll(asteroidsToRemove);
		explosions.removeAll(explosionsToRemove);
		powerUps.removeAll(powerUpsToRemove);
		effects.removeAll(effectsToRemove);
		collidedAsteroids.removeAll(asteroidsToRemove);
		
		for (Effect effect : effectsToRemove) {
			effect.remove();
			effect = null;
		}
		
		Asteroid.updateTimer(delta);
		if (shipDied) {
			endGame(EndGameWay.DIE);
		}
		
		renderEverything(delta);
	}
	
	public void renderEverything(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += delta;
		controlCenter.batch.begin();
		
		if (!escWasPressed) {
			hud.drawEsc(controlCenter.batch, delta);
		}
		
		//draws all entities from ArrayList by iterating
		for (Bullet bullet : bullets) {
			bullet.render(controlCenter.batch);
		}
		
		for (Asteroid asteroid : asteroids) {
			asteroid.render(controlCenter.batch);
		}
		
		for (Explosion explosion : explosions) {
			explosion.render(controlCenter.batch);
		}
		
		for (PowerUp powerUp : powerUps) {
			powerUp.render(controlCenter.batch, hud);
		}
		
		for (Effect effect : effects) {
			effect.render(controlCenter.batch);
		}
		
		hud.drawHealthBar(controlCenter.batch, ship.getHealth());
		hud.drawScore(controlCenter.batch, score);
		ship.render(stateTime, controlCenter.batch, hitTaken, delta);
		hitTaken = false;
		
		if (controlCenter.gamePaused == true) {
			Sounds.getBackgroundMusic().pause();
			pauseScreen.render();
		}
		controlCenter.batch.end();
	}
	
	//sets bullets offset depending on the ship's tilt
	public int setOffset() {
		int bulletOffset = 4;
		if (ship.getRollState() == 1 || ship.getRollState() == 3) {
			bulletOffset = 8;
		} else if (ship.getRollState() == 0 || ship.getRollState() == 4) {
			bulletOffset = 16;
		} 
		return bulletOffset;
	}
	
	//caled when ship runs out of health. Passes score to class controlCenter and sets GameOver Screen.
	public void endGame(EndGameWay way) {
		if (way == EndGameWay.DIE) {
			Sounds.getSound("DEATH").play();
			controlCenter.setScore(score);
		}
		
		if (currentEffect != null) {
			currentEffect.remove();
			currentEffect = null;
		}
		
		Sounds.getBackgroundMusic().stop();
		
		controlCenter.setScreen(new GameOverScreen(controlCenter, way));	
	}
	
	public static void manipulateDelta(float delta) {
		deltaIsManipulated = true;
		manipulatedDelta = delta;
	}
	
	public static void stopManipulatingDelta() {
		deltaIsManipulated = false;
	}
	
	public static void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void increaseScore(int newScore) {
		score += newScore;
	}
	
	public void setShipDied(boolean died) {
		shipDied = died;
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		controlCenter.gamePaused = true;
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		this.dispose();
	}
}
