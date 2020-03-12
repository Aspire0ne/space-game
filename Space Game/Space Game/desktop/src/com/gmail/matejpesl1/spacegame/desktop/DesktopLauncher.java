package com.gmail.matejpesl1.spacegame.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.gmail.matejpesl1.spacegame.ControlCenter;

public class DesktopLauncher {
	public final static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	
		public static void main (String[] arg) {
			config.width = ControlCenter.WINDOW_WIDTH;
			config.height = ControlCenter.WINDOW_HEIGHT;
			config.resizable = false;
			config.allowSoftwareMode = true;
			config.vSyncEnabled = true;
			config.initialBackgroundColor = Color.DARK_GRAY;
			config.title = "Space Game";
			config.addIcon("textures/game_icons/icon_32.png", FileType.Internal);
			
			new LwjglApplication(new ControlCenter() {
	        	@Override
	            public void setForegroundFPS(int value) {
	                config.foregroundFPS = value;
	            }
	        }, config);
	    }
}