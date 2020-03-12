package com.gmail.matejpesl1.spacegame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.entities.PowerUp;
import com.gmail.matejpesl1.spacegame.entities.Ship;
import com.gmail.matejpesl1.spacegame.powerupEffects.Effect;
import com.gmail.matejpesl1.spacegame.screens.MainGameScreen;

public class HUD {
	private static final BitmapFont scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
	private static final GlyphLayout scoreLayout = new GlyphLayout();
	Ship ship;
	private static final Texture blank = new Texture(Gdx.files.internal("textures/others/blank.png"));
	MainGameScreen mainScreen;
	private float effectBarFull;
	private boolean healthBarFirstCall;
	private float healthBarFull;
	private static final int SCORE_Y_OFFSET = 50;
	
	private static final float EFFECT_ICON_WIDTH = PowerUp.getPowerUpWidth() * 3.5f;
	private static final float EFFECT_ICON_HEIGHT = PowerUp.getPowerUpHeight() * 3.5f;
	private static final float EFFECT_ICON_OFFSET = 20;
	private static final float EFFECT_ICON_X = ControlCenter.WINDOW_WIDTH - EFFECT_ICON_WIDTH - EFFECT_ICON_OFFSET;
	private static final float EFFECT_ICON_Y = ControlCenter.WINDOW_HEIGHT - EFFECT_ICON_HEIGHT - EFFECT_ICON_OFFSET;

	public HUD(Ship ship, MainGameScreen mainScreen) {
		healthBarFirstCall = true;
		this.mainScreen = mainScreen;
		this.ship = ship;
	}
	
	public void drawHealthBar(SpriteBatch batch, float health) {
		if (healthBarFirstCall) {
			healthBarFull = 1/(health);
			healthBarFirstCall = false;
		}
		
		colourHealthBar(batch);
		batch.draw(blank, 0, 0, ControlCenter.WINDOW_WIDTH *(healthBarFull*health), 5);
		batch.setColor(Color.WHITE);
	}
	
	private void colourHealthBar(SpriteBatch batch) {
		if (ship.getHealth() > 0.3f) {
			batch.setColor(Color.GREEN);
		}
		else if (ship.getHealth() <= 0.3f && ship.getHealth() > 0.2f) {
			batch.setColor(Color.ORANGE);
		}
		else if (ship.getHealth() < 0.1f) {
			mainScreen.setShipDied(true);
		} else {
			batch.setColor(Color.RED);
		}
	}
	
	public void drawScore(SpriteBatch batch, int score) {
		scoreFont.getData().setScale(1f, 1f);
		scoreLayout.setText(scoreFont, "" + score);
		scoreFont.draw(batch, scoreLayout, ControlCenter.WINDOW_WIDTH/2 - scoreLayout.width/2, ControlCenter.WINDOW_HEIGHT - SCORE_Y_OFFSET);
	}
	
	public void drawEsc(SpriteBatch batch, float delta) {
		scoreFont.getData().setScale(0.6f, 0.7f);
		scoreLayout.setText(scoreFont, "[ESC]");
		scoreFont.draw(batch, scoreLayout, 20, ControlCenter.WINDOW_HEIGHT - 20);
	}
	
	public void renderEffectInfo(SpriteBatch batch, Effect currentEffect, boolean firstCall) {
		batch.draw(currentEffect.getIcon(), EFFECT_ICON_X, EFFECT_ICON_Y, EFFECT_ICON_WIDTH, EFFECT_ICON_HEIGHT);
		renderEffectDurationBar(batch, currentEffect, firstCall);
		batch.setColor(Color.PURPLE);
		batch.draw(blank, EFFECT_ICON_X, EFFECT_ICON_Y - 20, (EFFECT_ICON_WIDTH*(effectBarFull*currentEffect.getRemainingDuration()/10)), 10);
		batch.setColor(Color.WHITE);
	}
	
	private void renderEffectDurationBar(SpriteBatch batch, Effect currentEffect, boolean firstCall) {
		if (firstCall) {
			effectBarFull = 1/(currentEffect.getMaxEffectDuration()/10);
			System.out.println("effectBarFull: " + effectBarFull);
		}
		batch.setColor(Color.PURPLE);
		batch.draw(blank, EFFECT_ICON_X, EFFECT_ICON_Y - 20, (EFFECT_ICON_WIDTH*(effectBarFull*currentEffect.getRemainingDuration()/10)), 10);
		batch.setColor(Color.WHITE);
	}
}
