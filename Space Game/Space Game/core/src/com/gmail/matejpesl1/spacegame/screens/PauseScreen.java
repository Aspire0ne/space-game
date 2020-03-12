package com.gmail.matejpesl1.spacegame.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen.EndGameWay;
import com.gmail.matejpesl1.spacegame.tools.Buttons;
import com.gmail.matejpesl1.spacegame.tools.Sounds;
public class PauseScreen {
	public boolean resume;
	ControlCenter controlCenter;
	private static final Texture BORDER = new Texture("textures/others/okraj.png");
	private static final int TRANSPARENT_BACKGROUND_WIDTH = ControlCenter.WINDOW_WIDTH;
	private static final int TRANSPARENT_BACKGROUND_HEIGHT = ControlCenter.WINDOW_HEIGHT;
	
	private static final int MUSIC_BUTTON_WIDTH = 80;
	private static final int MUSIC_BUTTON_HEIGHT = 80;
	private static final int MUSIC_BUTTON_OFFSET = 60;
	private final static int MUSIC_BUTTON_X = MUSIC_BUTTON_OFFSET; 
	private final static int MUSIC_BUTTON_Y = ControlCenter.WINDOW_HEIGHT - MUSIC_BUTTON_HEIGHT - MUSIC_BUTTON_OFFSET;
	private static final int CONTINUE_WIDTH = 400;
	private static final int CONTINUE_HEIGHT = 70;
	private final static int CONTINUE_X = ControlCenter.WINDOW_WIDTH/2 - CONTINUE_WIDTH/2; 
	private final static int CONTINUE_Y = 430;
	private final static int BORDER_X = ControlCenter.WINDOW_WIDTH - (ControlCenter.WINDOW_WIDTH);
	private final static int BORDER_Y = ControlCenter.WINDOW_HEIGHT - (ControlCenter.WINDOW_HEIGHT - 2);
	private final static int BORDER_WIDTH = ControlCenter.WINDOW_WIDTH;
	private final static int BORDER_HEIGHT = ControlCenter.WINDOW_HEIGHT;
	private final static int RESTART_WIDTH = 350;
	private final static int RESTART_HEIGHT = 50;
	private final static int RESTART_X = ControlCenter.WINDOW_WIDTH/2 - RESTART_WIDTH/2;
	private final static int RESTART_Y = CONTINUE_Y - 100;
	private final static int EXIT_WIDTH = 200;
	private final static int EXIT_HEIGHT = 80;
	private final static int EXIT_X = ControlCenter.WINDOW_WIDTH/2 - EXIT_WIDTH/2;
	private final static int EXIT_Y = RESTART_Y - 170;
	private static final Texture TRANSPARENT_BACKGROUND = new Texture("textures/backgrounds/transparent_background.png");
	private ArrayList<Boolean> hitAlreadyPlayed = new ArrayList<>(Arrays.asList(false, false, false, false, false));
	MainGameScreen mainGameScreen;
	
		public PauseScreen(ControlCenter controlCenter, MainGameScreen mainGameScreen) {
			this.controlCenter = controlCenter;
			this.mainGameScreen = mainGameScreen;
		}
		
	public void render() {
		if (!resume) {
			//render pauseScreen
			controlCenter.batch.draw(TRANSPARENT_BACKGROUND, 0, 0, TRANSPARENT_BACKGROUND_WIDTH, TRANSPARENT_BACKGROUND_HEIGHT);
			controlCenter.batch.draw(BORDER, BORDER_X, BORDER_Y, BORDER_WIDTH, BORDER_HEIGHT);
			
			if (Buttons.isButtonActive(CONTINUE_X, CONTINUE_Y, CONTINUE_WIDTH, CONTINUE_HEIGHT, controlCenter.getCam())) {
				controlCenter.batch.draw(Buttons.CONTINUE_ACTIVE, CONTINUE_X, CONTINUE_Y, CONTINUE_WIDTH, CONTINUE_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(0));
				hitAlreadyPlayed.set(0, true);
				if (Gdx.input.justTouched()) {
					playMenuSound("CLICK", false);
					resume();
				}
			} else {
				controlCenter.batch.draw(Buttons.CONTINUE_INACTIVE, CONTINUE_X, CONTINUE_Y, CONTINUE_WIDTH, CONTINUE_HEIGHT);
				hitAlreadyPlayed.set(0, false);
			}
			
			if (Buttons.isButtonActive(EXIT_X, EXIT_Y, EXIT_WIDTH, EXIT_HEIGHT, controlCenter.getCam())) {
				controlCenter.batch.draw(Buttons.EXIT_ACTIVE, EXIT_X, EXIT_Y, EXIT_WIDTH, EXIT_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(1));
				hitAlreadyPlayed.set(1, true);
				if (Gdx.input.justTouched()) {
					playMenuSound("CLICK", false);
					resume();
					unPauseGame();
					mainGameScreen.endGame(EndGameWay.MENU);
				}
			} else {
				hitAlreadyPlayed.set(1, false);
				controlCenter.batch.draw(Buttons.EXIT_INACTIVE, EXIT_X, EXIT_Y, EXIT_WIDTH, EXIT_HEIGHT);
			}
			
			if (Buttons.isButtonActive(RESTART_X, RESTART_Y, RESTART_WIDTH, RESTART_HEIGHT, controlCenter.getCam())) {
				controlCenter.batch.draw(Buttons.RESTART_ACTIVE, RESTART_X, RESTART_Y, RESTART_WIDTH, RESTART_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(2));
				hitAlreadyPlayed.set(2, true);
				if (Gdx.input.justTouched()) {
					playMenuSound("CLICK", false);
					resume();
					unPauseGame();
					mainGameScreen.endGame(EndGameWay.RESTART);
				}
			} else {
				hitAlreadyPlayed.set(2, false);
				controlCenter.batch.draw(Buttons.RESTART_INACTIVE, RESTART_X, RESTART_Y, RESTART_WIDTH, RESTART_HEIGHT);
			}
			
			if (Buttons.isButtonActive(MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT, controlCenter.getCam())) {
				if (Sounds.backgroundMusicIsEnabled) {
					controlCenter.batch.draw(Buttons.MUSIC_ON_ACTIVE, MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);	
				} else {
					controlCenter.batch.draw(Buttons.MUSIC_OFF_ACTIVE, MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);
				}
				
				playMenuSound("HIT", hitAlreadyPlayed.get(3));
				hitAlreadyPlayed.set(3, true);
				if (Gdx.input.justTouched()) {
					playMenuSound("CLICK", false);
					Sounds.backgroundMusicIsEnabled = Sounds.backgroundMusicIsEnabled ? false : true;
				}
			} else {
				hitAlreadyPlayed.set(3, false);
				if (Sounds.backgroundMusicIsEnabled) {
					controlCenter.batch.draw(Buttons.MUSIC_ON_INACTIVE, MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);	
				} else {
					controlCenter.batch.draw(Buttons.MUSIC_OFF_INACTIVE, MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT);
				}
				
			}
			
			if ((Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.ENTER)) && controlCenter.timeFromGameResume == 0) {
				resume();
			}
			
			if (controlCenter.timeFromGameResume > 0) {
				controlCenter.setForegroundFPS(30);
				controlCenter.timeFromGameResume = 0;
			}
		} else {
			unPauseGame();
		}
	}
	
	private void unPauseGame() {
		controlCenter.gamePaused = false;
		resume = false;
	}
	
	private void playMenuSound(String menuSound, boolean alreadyPlayed) {
		Buttons.playMenuSound(menuSound, alreadyPlayed);
	}
	
	public void resume() {
		resume = true;
		controlCenter.setForegroundFPS(60);
	}
}
