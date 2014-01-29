package com.mammothGames.wake.gameEngine;

public class base_texture {
	protected float[] texCoords;
	
	// The name of the image file in assets/textures or the font in assets/fonts
	protected String nameAndExtension;
	
	protected boolean isFont = false;
	protected int fontSize = 15;

	protected int stokeSize = 0;
	// Inner color, outer color, RBG
	// { 0,0,0,  1,1,1 }
	protected float[] stokeColor = { 1,1,1,  0,0,0 };
}