package com.gmail.matejpesl1.spacegame.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.entities.Asteroid;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen.EndGameWay;
import com.gmail.matejpesl1.spacegame.tools.Buttons;

public class GameOverScreen implements Screen {
	int score;
	ControlCenter controlCenter;
	private final static BitmapFont segoeFont = new BitmapFont(Gdx.files.internal("fonts/segoe.fnt"));
	private final static GlyphLayout layout = new GlyphLayout();
	private final static Texture youDiedTexture = new Texture("textures/others/you_died_text.png");
	private Timer timer;
	
	public static final int BUTTON_OFFSET = 100;
	public static final int YOU_DIED_WIDTH = 30;
	public static final int ANO_WIDTH = 240;
	public static final int ANO_HEIGHT = 100;
	public static final int NE_WIDTH = 185;
	public static final int NE_HEIGHT = 100;
	public static final int ANO_X = (ControlCenter.WINDOW_WIDTH - ANO_WIDTH) - 200;
	public static final int ANO_Y = 130; 
	public static final int NE_X = 200;
	public static final int NE_Y = 130;
	public static final int YOU_DIED_HEIGHT = 30;
	private boolean gameShouldBeStarted;
	private boolean menuShouldBeOpened;
	ArrayList<Boolean> hitAlreadyPlayed = new ArrayList<>(Arrays.asList(false, false));
	 
	public GameOverScreen(ControlCenter controlCenter, EndGameWay way) {
		this.controlCenter = controlCenter;
		if (way == EndGameWay.RESTART) {
			gameShouldBeStarted = true;
		} else if (way == EndGameWay.MENU) {
			menuShouldBeOpened = true;
		}
		timer = new Timer(controlCenter.batch);
		segoeFont.getData().setScale(0.7f, 0.7f);
	}

	@Override
	public void show() {
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		controlCenter.batch.begin();
		
		if (gameShouldBeStarted) {
			startGame(delta);
			controlCenter.batch.end();
			return;
		}
		
		if (menuShouldBeOpened) {
			openMainMenu();
			controlCenter.batch.end();
			return;
		}
		controlCenter.batch.setColor(Color.SALMON);
		controlCenter.batch.draw(youDiedTexture, ControlCenter.WINDOW_WIDTH/2 - youDiedTexture.getWidth()/2, ControlCenter.WINDOW_HEIGHT - 200);
		controlCenter.batch.setColor(Color.WHITE);
		
		layout.setText(segoeFont, "Pøeješ si zkusit to znovu?");
		drawLayout(layout, 220, segoeFont);
		layout.setText(segoeFont, "pøedešlé skóre: " + controlCenter.getLastScore());
		drawLayout(layout, 320, segoeFont);
		layout.setText(segoeFont, "aktuální skóre: " + controlCenter.getScore());
		drawLayout(layout, 270, segoeFont);
		
		if (Buttons.isButtonActive(NE_X, NE_Y, NE_WIDTH, NE_HEIGHT, controlCenter.getCam())) {
			controlCenter.batch.draw(Buttons.NE_ACTIVE, NE_X, NE_Y, NE_WIDTH, NE_HEIGHT);
			playMenuSound("HIT", hitAlreadyPlayed.get(0));
			hitAlreadyPlayed.set(0, true);
			if (Gdx.input.isTouched()) {
				playMenuSound("CLICK", false);
				menuShouldBeOpened = true;
			}
		} else {
			controlCenter.batch.draw(Buttons.NE_INACTIVE, NE_X, NE_Y, NE_WIDTH, NE_HEIGHT);
			hitAlreadyPlayed.set(0, false);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			playMenuSound("CLICK", false);
			gameShouldBeStarted = true;
		}
		
		if (Buttons.isButtonActive(ANO_X, ANO_Y, ANO_WIDTH, ANO_HEIGHT, controlCenter.getCam())) {
			controlCenter.batch.draw(Buttons.ANO_ACTIVE, ANO_X, ANO_Y, ANO_WIDTH, ANO_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(1));
				hitAlreadyPlayed.set(1, true);
			if (Gdx.input.isTouched()) {
				gameShouldBeStarted = true;
				playMenuSound("CLICK", false);
			}
		} else {
			hitAlreadyPlayed.set(1, false);
			controlCenter.batch.draw(Buttons.ANO_INACTIVE, ANO_X, ANO_Y, ANO_WIDTH, ANO_HEIGHT);
		}
		controlCenter.batch.end();
	}
	
	private void openMainMenu() {
		Asteroid.reset();
		this.dispose();
		controlCenter.setScreen(new MainMenuScreen(controlCenter));
	}
	
	private void playMenuSound(String menuSound, boolean alreadyPlayed) {
		Buttons.playMenuSound(menuSound, alreadyPlayed);
	}
	
	public void startGame(float delta) {
			timer.updateTimer(delta);
			if (timer.isDone()) {
				++controlCenter.retries;
				this.dispose();
				Asteroid.reset();
				controlCenter.setScreen(new MainGameScreen(controlCenter));	
			}
	}
	
	public void drawLayout(GlyphLayout layout, int distanceFromTop, BitmapFont font) {
		font.draw(controlCenter.batch, layout, ControlCenter.WINDOW_WIDTH/2 - layout.width/2, ControlCenter.WINDOW_HEIGHT - distanceFromTop);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}
