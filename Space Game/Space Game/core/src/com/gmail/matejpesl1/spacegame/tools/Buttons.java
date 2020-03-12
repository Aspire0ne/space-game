package com.gmail.matejpesl1.spacegame.tools;


import com.badlogic.gdx.graphics.Texture;
import com.gmail.matejpesl1.spacegame.ControlCenter;

public class Buttons {
	public final static Texture CONTINUE_INACTIVE = new Texture("textures/buttons/continue_button_inactive.png");
	public static final Texture CONTINUE_ACTIVE = new Texture("textures/buttons/continue_button_active.png");
	public final static Texture NE_INACTIVE = new Texture("textures/buttons/ne_button_inactive.png");
	public final static Texture ANO_INACTIVE = new Texture("textures/buttons/ano_button_inactive.png");
	public final static Texture NE_ACTIVE = new Texture("textures/buttons/ne_button_active.png");
	public final static Texture ANO_ACTIVE = new Texture("textures/buttons/ano_button_active.png");
	public final static Texture EXIT_ACTIVE = new Texture("textures/buttons/exit_button_active.png"); 
	public final static Texture EXIT_INACTIVE = new Texture("textures/buttons/exit_button_inactive.png");
	public final static Texture PLAY_ACTIVE = new Texture("textures/buttons/play_button_active.png");
	public final static Texture PLAY_INACTIVE = new Texture("textures/buttons/play_button_inactive.png");
	public final static Texture HELP_INACTIVE = new Texture("textures/buttons/help_button_inactive.png");
	public final static Texture HELP_ACTIVE = new Texture("textures/buttons/help_button_active.png");
	public final static Texture BACK_INACTIVE = new Texture("textures/buttons/back_button_inactive.png");
	public final static Texture BACK_ACTIVE = new Texture("textures/buttons/back_button_active.png");
	public final static Texture NEXT_INACTIVE = new Texture("textures/buttons/next_button_inactive.png");
	public final static Texture NEXT_ACTIVE = new Texture("textures/buttons/next_button_active.png");
	public final static Texture HOME_INACTIVE = new Texture("textures/buttons/home_button_inactive.png");
	public final static Texture HOME_ACTIVE = new Texture("textures/buttons/home_button_active.png");
	public final static Texture MUSIC_ON_INACTIVE = new Texture("textures/buttons/pause_music_button_inactive_on.png");
	public final static Texture MUSIC_ON_ACTIVE = new Texture("textures/buttons/pause_music_button_active_on.png");
	public final static Texture MUSIC_OFF_INACTIVE = new Texture("textures/buttons/pause_music_button_inactive_off.png");
	public final static Texture MUSIC_OFF_ACTIVE = new Texture("textures/buttons/pause_music_button_active_off.png");
	public final static Texture RESTART_INACTIVE = new Texture("textures/buttons/restartovat_button_inactive.png");
	public final static Texture RESTART_ACTIVE = new Texture("textures/buttons/restartovat_button_active.png");
	public final static Texture MENU_INACTIVE = new Texture("textures/buttons/menu_button_inactive.png");
	public final static Texture MENU_ACTIVE = new Texture("textures/buttons/menu_button_active.png");
	
	public static boolean isButtonActive(int x, int y, int width, int height, GameCamera cam) {
		return (cam.getInputInGameWorld().x > x && cam.getInputInGameWorld().x < x
				+ width && ControlCenter.WINDOW_HEIGHT - cam.getInputInGameWorld().y > y &&
				ControlCenter.WINDOW_HEIGHT - cam.getInputInGameWorld().y < y + height);
	}
	
	public static void playMenuSound(String menuSound, boolean alreadyPlayed) {
		if (alreadyPlayed) {
			return;
		} else {
			switch (menuSound) {
			case "CLICK": Sounds.getSound("MENU.CLICK").play();
			case "HIT": Sounds.getSound("MENU.HIT").play();
			}
		}
	}
}
