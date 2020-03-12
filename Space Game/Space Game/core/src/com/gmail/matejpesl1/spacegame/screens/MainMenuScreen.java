package com.gmail.matejpesl1.spacegame.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.tools.Buttons;

public class MainMenuScreen implements Screen {
	private final static BitmapFont segoeFont = new BitmapFont(Gdx.files.internal("fonts/segoe.fnt"));
	private final static GlyphLayout layout = new GlyphLayout();
	private final static Texture HELP_PAGE_1 = new Texture(Gdx.files.internal("textures/help/HELP_mechaniky1.png"));
	private final static Texture HELP_PAGE_2 = new Texture(Gdx.files.internal("textures/help/HELP_bonusy1.png"));
	private final static Texture HELP_PAGE_3 = new Texture(Gdx.files.internal("textures/help/HELP_ovladani1.png"));
	private final static Texture HELP_PAGE_4 = new Texture(Gdx.files.internal("textures/help/HELP_rozhrani1.png"));
	private static final Texture MADE_WITH_LOVE = new Texture(Gdx.files.internal("textures/others/made_with_love.png"));
	private final static float HELP_FLASH_DURATION = 2f;
	private float helpFlashDurationRemaining;
	private static final int MAX_PAGES = 4;
	private ArrayList<Boolean> hitAlreadyPlayed = new ArrayList<>(Arrays.asList(false, false, false, false, false));
	private int page;
	Timer timer;
	private boolean showHelp;
	
	private static final int MADE_WITH_LOVE_OFFSET = 10;
	private static final int MADE_WITH_LOVE_WIDTH = 265;
	private static final int MADE_WITH_LOVE_HEIGHT = 58;
	private static final int MADE_WITH_LOVE_X = ControlCenter.WINDOW_WIDTH - MADE_WITH_LOVE_WIDTH - MADE_WITH_LOVE_OFFSET; 
	private static final int MADE_WITH_LOVE_Y = MADE_WITH_LOVE_OFFSET;
	
	private static final int HELP_BUTTON_OFFSET = 10;
	
	private final static int HELP_BUTTON_WIDTH = 200;
	private final static int HELP_BUTTON_HEIGHT = 51;
	
	private final static int HELP_BUTTON_Y = HELP_BUTTON_OFFSET; 
	private final static int HELP_BUTTON_X = HELP_BUTTON_OFFSET;
	
	private static final int BACK_BUTTON_WIDTH = 100;
	private static final int BACK_BUTTON_HEIGHT = 70;
	
	private final static int BACK_BUTTON_Y = ControlCenter.WINDOW_HEIGHT - BACK_BUTTON_HEIGHT - HELP_BUTTON_OFFSET; 
	private final static int BACK_BUTTON_X = HELP_BUTTON_OFFSET;
	
	private static final int HOME_BUTTON_WIDTH = 95;
	private static final int HOME_BUTTON_HEIGHT = 100;
	
	private final static int HOME_BUTTON_X = HELP_BUTTON_OFFSET;
	private final static int HOME_BUTTON_Y = ControlCenter.WINDOW_HEIGHT - HOME_BUTTON_HEIGHT - HELP_BUTTON_OFFSET;
	
	private static final int NEXT_BUTTON_WIDTH = 100;
	private static final int NEXT_BUTTON_HEIGHT = 70;
	
	private final static int NEXT_BUTTON_Y = HELP_BUTTON_OFFSET;
	private final static int NEXT_BUTTON_X = ControlCenter.WINDOW_WIDTH - NEXT_BUTTON_WIDTH - HELP_BUTTON_OFFSET;

	private final static int EXIT_BUTTON_WIDTH = 250;
	private final static int EXIT_BUTTON_HEIGHT = 100;

	private final static int EXIT_BUTTON_Y = 190;
	private final static int EXIT_BUTTON_X = ControlCenter.WINDOW_WIDTH/2 - EXIT_BUTTON_WIDTH / 2;
	
	private final static int PLAY_BUTTON_WIDTH = 330;
	private final static int PLAY_BUTTON_HEIGHT = 150;

	private final static int PLAY_BUTTON_Y = 340;
	private final static int PLAY_BUTTON_X = ControlCenter.WINDOW_WIDTH/2 - PLAY_BUTTON_WIDTH / 2;
	private boolean gameShouldBeStarted;

	private ControlCenter controlCenter;
	
	public MainMenuScreen(ControlCenter controlCenter) {
		helpFlashDurationRemaining  = HELP_FLASH_DURATION;
		page = 1;
		this.controlCenter = controlCenter;
		timer = new Timer(controlCenter.batch);
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
		
		if (showHelp) {
			showHelp(delta);
			controlCenter.batch.end();
			return;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			gameShouldBeStarted = true;
		}
		
		controlCenter.batch.draw(MADE_WITH_LOVE, MADE_WITH_LOVE_X, MADE_WITH_LOVE_Y, MADE_WITH_LOVE_WIDTH, MADE_WITH_LOVE_HEIGHT);
		segoeFont.getData().setScale(1f, 1f);
		layout.setText(segoeFont, "nejvyšší skóre: " + ControlCenter.getHighscore());
		drawLayout(layout, 80, segoeFont);
		
		if (Buttons.isButtonActive(EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT, controlCenter.getCam())) {
			controlCenter.batch.draw(Buttons.EXIT_ACTIVE, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(0));
				hitAlreadyPlayed.set(0, true);
			if (Gdx.input.justTouched()) {
				playMenuSound("CLICK", false);
				Gdx.app.exit();
			}
		} else {
			controlCenter.batch.draw(Buttons.EXIT_INACTIVE, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			hitAlreadyPlayed.set(0, false);
		}
		
		if (Buttons.isButtonActive(PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, controlCenter.getCam())) {
			controlCenter.batch.draw(Buttons.PLAY_ACTIVE, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			playMenuSound("HIT", hitAlreadyPlayed.get(1));
			hitAlreadyPlayed.set(1, true);
			if (Gdx.input.justTouched()) {
				playMenuSound("CLICK", false);
				gameShouldBeStarted = true;
			}	
		} else {
			hitAlreadyPlayed.set(1, false);
			controlCenter.batch.draw(Buttons.PLAY_INACTIVE, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		if (Buttons.isButtonActive(HELP_BUTTON_X, HELP_BUTTON_Y, HELP_BUTTON_WIDTH, HELP_BUTTON_HEIGHT, controlCenter.getCam())) {
			controlCenter.batch.draw(Buttons.HELP_ACTIVE, HELP_BUTTON_X, HELP_BUTTON_Y, HELP_BUTTON_WIDTH, HELP_BUTTON_HEIGHT);
			playMenuSound("HIT", hitAlreadyPlayed.get(2));
			hitAlreadyPlayed.set(2, true);
			if (Gdx.input.justTouched()) {
				playMenuSound("CLICK", false);
				showHelp = true;
			}	
		} else {
			hitAlreadyPlayed.set(2, false);
			controlCenter.batch.draw(Buttons.HELP_INACTIVE, HELP_BUTTON_X, HELP_BUTTON_Y, HELP_BUTTON_WIDTH, HELP_BUTTON_HEIGHT);
		}
		
		controlCenter.batch.end();
	}
	
	public void showHelp(float delta) {
		helpFlashDurationRemaining -= delta;
		
		if (helpFlashDurationRemaining > 0) {
			layout.setText(segoeFont, "Nápovìda");
			drawLayout(layout, 300, segoeFont);
			return;
		} else {
			switch(page) {
			case 1: drawPage(HELP_PAGE_1); break;
			case 2: drawPage(HELP_PAGE_2); break;
			case 3: drawPage(HELP_PAGE_3); break;
			case 4: drawPage(HELP_PAGE_4); break;
			}
		}
		segoeFont.getData().setScale(0.8f, 0.8f);
		layout.setText(segoeFont, page + "/" + MAX_PAGES);
		segoeFont.draw(controlCenter.batch, layout, ControlCenter.WINDOW_WIDTH - layout.width - 20, ControlCenter.WINDOW_HEIGHT - 20);
		if (Buttons.isButtonActive(BACK_BUTTON_X, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT, controlCenter.getCam()) ||
				(Buttons.isButtonActive(HOME_BUTTON_X, HOME_BUTTON_Y, HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT, controlCenter.getCam()) &&
						page == 1)) {
			if (page != 1) {
				controlCenter.batch.draw(Buttons.BACK_ACTIVE, BACK_BUTTON_X, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);	
			} else {
				controlCenter.batch.draw(Buttons.HOME_ACTIVE, HOME_BUTTON_X, HOME_BUTTON_Y, HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
			}
			
			playMenuSound("HIT", hitAlreadyPlayed.get(3));
			hitAlreadyPlayed.set(3, true);
			if (Gdx.input.justTouched()) {
				playMenuSound("CLICK", false);
				if (page == 1) {
					showHelp = false;	
				} else {
					--page;
				}
			}
		} else {
			hitAlreadyPlayed.set(3, false);
			if (page != 1) {
				controlCenter.batch.draw(Buttons.BACK_INACTIVE, BACK_BUTTON_X, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);	
			} else {
				controlCenter.batch.draw(Buttons.HOME_INACTIVE, HOME_BUTTON_X, HOME_BUTTON_Y, HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
			}
		}
		
		if (Buttons.isButtonActive(NEXT_BUTTON_X, NEXT_BUTTON_Y, NEXT_BUTTON_WIDTH, NEXT_BUTTON_HEIGHT, controlCenter.getCam()) && page != MAX_PAGES) {
			controlCenter.batch.draw(Buttons.NEXT_ACTIVE, NEXT_BUTTON_X, NEXT_BUTTON_Y, NEXT_BUTTON_WIDTH, NEXT_BUTTON_HEIGHT);
				playMenuSound("HIT", hitAlreadyPlayed.get(4));
				hitAlreadyPlayed.set(4, true);
				
			if (Gdx.input.justTouched()) {
				playMenuSound("CLICK", false);
				if (page < MAX_PAGES) {
					++page;
				}
			}
		} else {
			if (page != MAX_PAGES) {
				controlCenter.batch.draw(Buttons.NEXT_INACTIVE, NEXT_BUTTON_X, NEXT_BUTTON_Y, NEXT_BUTTON_WIDTH, NEXT_BUTTON_HEIGHT);
			}
			hitAlreadyPlayed.set(4, false);
		}
	}
	
	private void drawPage(Texture PAGE) {
		controlCenter.batch.draw(PAGE, 0, 0, ControlCenter.WINDOW_WIDTH, ControlCenter.WINDOW_HEIGHT);
	}
	
	private void playMenuSound(String menuSound, boolean alreadyPlayed) {
		Buttons.playMenuSound(menuSound, alreadyPlayed);
	}
	
	public void drawLayout(GlyphLayout layout, int distanceFromTop, BitmapFont font) {
		font.draw(controlCenter.batch, layout, ControlCenter.WINDOW_WIDTH/2 - layout.width/2, ControlCenter.WINDOW_HEIGHT - distanceFromTop);
	}
	
	public void startGame(float delta) {
		timer.updateTimer(delta);
		if (timer.isDone()) {
			this.dispose();
			controlCenter.setScreen(new MainGameScreen(controlCenter));
		}
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
