package com.gmail.matejpesl1.spacegame.tools;


import com.gmail.matejpesl1.spacegame.ControlCenter;

public class Hitbox {
	private float x, y;
	private int width, height;


	public Hitbox(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean collidesWith(Hitbox checkedEntity) {
		 return x < checkedEntity.x + checkedEntity.width && y < checkedEntity.y + checkedEntity.height &&
				x + width > checkedEntity.x && y + height > checkedEntity.y;
	}
	
	public void remove() {
		this.y = ControlCenter.WINDOW_HEIGHT + 1000;
		this.x = ControlCenter.WINDOW_WIDTH + 1000;
	}
	
	public void changeDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
}