package com.gmail.matejpesl1.spacegame.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds{
	private static final Random random = new Random();
	private final static Sound BULLET_WAVE_BACKGROUND1 = Gdx.audio.newSound(Gdx.files.internal("sounds/wave_background.ogg"));
	private final static Sound BULLET_SHOT1 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet_shot.ogg"));
	private final static Sound BULLET_WAVE1 = Gdx.audio.newSound(Gdx.files.internal("sounds/wave.ogg"));
	private final static Sound HIT_TAKEN1 = Gdx.audio.newSound(Gdx.files.internal("sounds/ship_hit1.ogg"));
	private final static Sound POWERUP_COLLECTED1 = Gdx.audio.newSound(Gdx.files.internal("sounds/powerup_collected.ogg"));
	private final static Sound SHIELD_COLLISION1 =  Gdx.audio.newSound(Gdx.files.internal("sounds/shield_collision1.ogg"));
	private final static Sound SHIELD_COLLISION2 =  Gdx.audio.newSound(Gdx.files.internal("sounds/shield_collision2.ogg"));
	private final static Sound SHIELD_COLLISION3 =  Gdx.audio.newSound(Gdx.files.internal("sounds/shield_collision3.ogg"));
	private final static Sound SHIELD_COLLISION4 =  Gdx.audio.newSound(Gdx.files.internal("sounds/shield_collision4.ogg"));
	private final static Sound SHIELD_COLLISION5 =  Gdx.audio.newSound(Gdx.files.internal("sounds/shield_collision5.ogg"));
	private final static Sound MENU_HIT1 = Gdx.audio.newSound(Gdx.files.internal("sounds/menuhit.ogg"));
	private final static Sound MENU_CLICK1 = Gdx.audio.newSound(Gdx.files.internal("sounds/menuclick.ogg"));
	private final static Sound COUNTDOWN_TICK1 = Gdx.audio.newSound(Gdx.files.internal("sounds/countdown_tick.ogg"));
	private final static Sound EXPLOSION1 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_sound.ogg"));
	private final static Sound STARTUP1 = Gdx.audio.newSound(Gdx.files.internal("sounds/startup_sound.ogg"));
	private final static Music BACKGROUND_MUSIC1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.ogg"));
	private final static Sound DEATH_SOUND = Gdx.audio.newSound(Gdx.files.internal("sounds/oof_death_sound.ogg"));
	public static boolean backgroundMusicIsEnabled = true;
	
	private static final ArrayList<Sound> STARTUP = new ArrayList<>(Arrays.asList(STARTUP1));
	private static final ArrayList<Sound> BULLET_SHOT = new ArrayList<>(Arrays.asList(BULLET_SHOT1));
	private static final ArrayList<Sound> EXPLOSION = new ArrayList<>(Arrays.asList(EXPLOSION1));
	private static final ArrayList<Sound> COUNTDOWN_TICK = new ArrayList<>(Arrays.asList(COUNTDOWN_TICK1));
	private static final ArrayList<Sound> BULLET_WAVE = new ArrayList<>(Arrays.asList(BULLET_WAVE1));
	private static final ArrayList<Sound> POWERUP_COLLECTED = new ArrayList<>(Arrays.asList(POWERUP_COLLECTED1));
	private static final ArrayList<Sound> BULLET_WAVE_BACKGROUND = new ArrayList<>(Arrays.asList(BULLET_WAVE_BACKGROUND1));
	private static final ArrayList<Sound> HIT_TAKEN = new ArrayList<>(Arrays.asList(HIT_TAKEN1));
	private static final ArrayList<Sound> SHIELD_COLLISION = new ArrayList<>(Arrays.asList(SHIELD_COLLISION1, SHIELD_COLLISION2, SHIELD_COLLISION3, SHIELD_COLLISION4,
	SHIELD_COLLISION5));
	
	public static Sound getRandom(String soundType) {
		switch(soundType) {
		case "SHIELD_COLLISION": return SHIELD_COLLISION.get(random.nextInt(SHIELD_COLLISION.size()));
		}
		return null;
	}
	
	public static Sound getSound(String sound) {
			String index = sound.substring(sound.lastIndexOf(".") + 1);
			sound = sound.replace("." + index, "");	
		
		switch(sound) {
			case "WAVE": return index.equals("BACKGROUND") ? BULLET_WAVE_BACKGROUND1 : BULLET_WAVE1;
			case "MENU": return index.equals("CLICK") ? MENU_CLICK1 : MENU_HIT1;
			case "SHIELD_COLLISION": return returnSoundAtIndex(SHIELD_COLLISION, index);
			case "BULLET_SHOT": return returnSoundAtIndex(BULLET_SHOT, index);
			case "BULLET_WAVE": return returnSoundAtIndex(BULLET_WAVE, index);
			case "BULLET_WAVE_BACKGROUND": return returnSoundAtIndex(BULLET_WAVE_BACKGROUND, index);
			case "POWERUP_COLLECTED": return returnSoundAtIndex(POWERUP_COLLECTED, index);
			case "HIT_TAKEN": return returnSoundAtIndex(HIT_TAKEN, index);
			case "COUNTDOWN_TICK": return returnSoundAtIndex(COUNTDOWN_TICK, index);
			case "EXPLOSION": return returnSoundAtIndex(EXPLOSION, index);
			case "STARTUP": return returnSoundAtIndex(STARTUP, index);
			case "DEATH": return DEATH_SOUND;
		}
		return null;
	}
	
	public static Sound returnSoundAtIndex(ArrayList<Sound> list, String index) {
		if (index.equalsIgnoreCase("dispose")) {
			for (int i = 0; i <= list.size() - 1; ++i) {
				list.get(i).dispose();
			}
			return null;
		}
		try { 
			return list.get(Integer.parseInt(index));
		} catch (InputMismatchException | IndexOutOfBoundsException e) {
			System.out.println("neexistující index nebo nesprávný formát indexu.");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Music getBackgroundMusic() {
		return BACKGROUND_MUSIC1;
	}
	
	public static void startBackgroundMusic() {
		Sounds.getBackgroundMusic().play();
		Sounds.getBackgroundMusic().setLooping(true);
		Sounds.getBackgroundMusic().setVolume(1f);
	}
}
