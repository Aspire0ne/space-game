package com.gmail.matejpesl1.spacegame.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.ControlCenter;
import com.gmail.matejpesl1.spacegame.tools.Sounds;


public class Timer {
	private static final float MAX_DURATION = 3;
	private float remainingDuration;
	private static final int TIMER_Y_OFFSET = 50;
	private static final Texture TIMER3 = new Texture("textures/timer/timer3.png");
	private static final Texture TIMER1 = new Texture("textures/timer/timer1.png");
	private static final Texture TIMER2 = new Texture("textures/timer/timer2.png");
	private static final int TIMER_X = ControlCenter.WINDOW_WIDTH/2 - TIMER3.getWidth()/2;
	private static final int TIMER_Y = ControlCenter.WINDOW_HEIGHT/2 - TIMER3.getHeight()/2 + TIMER_Y_OFFSET;
	private boolean isDone;
	private int timerState;
	SpriteBatch batch;

	public Timer(SpriteBatch batch) {
		remainingDuration = MAX_DURATION;
		this.batch = batch;
	}
	
	public void updateTimer(float delta) {
			remainingDuration -= delta;
		if (remainingDuration <= 0) {
			isDone = true;
			return;
		}
			if (remainingDuration > 2) {
				drawTimer(TIMER3);
				playTick(3);
			}
			else if (remainingDuration > 1 && remainingDuration < 2) {
				drawTimer(TIMER2);
				playTick(2);
			} 
			else if (remainingDuration > 0 && remainingDuration < 1) {
				drawTimer(TIMER1);
				playTick(1);
			}
	}
	
	private void drawTimer(Texture timer) {
		batch.draw(timer, TIMER_X, TIMER_Y);
	}
	
	private void playTick(int newTimerState) {
		if (newTimerState != this.timerState) {
			Sounds.getSound("COUNTDOWN_TICK.0").play();
			this.timerState = newTimerState;
		}
	}
	
	public boolean isDone() {
		return isDone;
	}
}
