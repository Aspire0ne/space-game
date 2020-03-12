package com.gmail.matejpesl1.spacegame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.matejpesl1.spacegame.screens.MainMenuScreen;
import com.gmail.matejpesl1.spacegame.tools.GameCamera;
import com.gmail.matejpesl1.spacegame.tools.Sounds;

public class ControlCenter extends Game {
	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 750;
	
	private static final String ROOT_DIR_PATH = System.getProperty("user.home");
	private static final String GAME_DIR_PATH = ROOT_DIR_PATH + "\\AppData\\Local\\Space Game";
	private static final File GAME_DIR = new File(GAME_DIR_PATH);
	private static final File HIGHSCORE_FILE = new File(GAME_DIR_PATH + "\\Highscore.score");
	
	private GameCamera cam;
	public SpriteBatch batch;
	
	private int lastScore;
	private int lastScore2;
	private int score;
	public int retries;
	public boolean gamePaused;
	public float timeFromGameResume;

	
	
	@Override
	public void create() {
		 Sounds.getSound("STARTUP.0").play(0.5f);
		 cam = new GameCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
		 if (!GAME_DIR.exists()) createGameDir();
		 batch = new SpriteBatch();
		 /* needed only to initialize sounds and load them into RAM. It's not possible to
		 load only the one that is currently needed during runtime, because they cause lag spikes. */
		 new Sounds();
		 setScreen(new MainMenuScreen(this));
	}
	
	private static void createGameDir() {
		GAME_DIR.mkdir();
		setHighscore(0);
	}
	
	public GameCamera getCam() {
		return cam;
	}
	
	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined());
		super.render();
	}
	
	public int getLastScore(){
		return retries % 2 == 1 ? lastScore : lastScore2;
	}
	
	public void setScore(int score) {
		setLastScore(score);
		if (score > getHighscore()) {
			setHighscore(score);
		}
		this.score = score;
	}
	
	private void setLastScore(int score) {
		if (retries % 2 == 0) {
			lastScore = score;
		} else {
			lastScore2 = score;
		}
	}
	
	public int getScore(){
		return score;
	}
	
	public static int getHighscore() {
		int score = -1;
		try {
			byte[] contentBytes = Files.readAllBytes(HIGHSCORE_FILE.toPath());
			String content = new String(contentBytes);
			String lineWithScore = getLineOrNull(content, 1);
			if (lineWithScore == null) {
				createGameDir(); return 0;
			}

			score = Integer.parseInt(lineWithScore);
		
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nastala IO chyba pøi ètení obsahu souboru.");
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println("Nelze pøeèíst skóre, protože byl programu odmítnut pøístup do souboru.");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Nelze pøeèíst skóre, protože není správnì zapsáno.");
			createGameDir();
		}
		
		return score;
	}
	
	private static void setHighscore(int score) {
		byte[] scoreInBytes = Integer.toString(score).getBytes();
		try {
			Files.write(HIGHSCORE_FILE.toPath(), scoreInBytes);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nastala chyba pøi zapisování skóre do textového souboru");
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println("Nelze zapsat skóre do souboru, protože AntiVirus pravdìpodobnì zabránil pøístupu.");
		}
	}
	
	private static String getLineOrNull(String text, int index) {
        String[] lines = text.split("\\r?\\n");
        return lines.length < index ? null : lines[index-1];
	}
	
	public void setForegroundFPS(int value) {}
}






