package com.mammothGames.wake.gameEngine;

import java.util.ArrayList;

import android.util.Log;

import com.mammothGames.wake.game.game_textures;

public class loaded_assets {
	engine_reference ref;
	
	public loaded_assets(engine_reference r) {
		ref = r;
		
		texCoords = new ArrayList<float[]>();
		nameAndExtension = new ArrayList<String>();
		isFont= new ArrayList<Boolean>();
		fontSize= new ArrayList<Integer>();
		stokeSize= new ArrayList<Integer>();
		stokeColor = new ArrayList<float[]>();
		
		
		float[] tmp = {1};
		texCoords.add(0, tmp);
		nameAndExtension.add(0, " ");
		isFont.add(0, false);
		fontSize.add(0, 0);
		stokeSize.add(0, 0);
		stokeColor.add(0, tmp);
		
		game_textures.addTextures(this);
	}
	
	public void addTexture(int id, base_texture tex) {
		texCoords.add(id-1, tex.texCoords);
		nameAndExtension.add(id-1, tex.nameAndExtension);
		isFont.add(id-1, tex.isFont);
		fontSize.add(id-1, tex.fontSize);
		stokeSize.add(id-1, tex.stokeSize);
		stokeColor.add(id-1, tex.stokeColor);
	}
	
	ArrayList<float[]> texCoords;
	ArrayList<String> nameAndExtension;
	ArrayList<Boolean> isFont;
	ArrayList<Integer> fontSize;
	ArrayList<Integer> stokeSize;
	ArrayList<float[]> stokeColor;
	
	public int get_numTextures() {
		return nameAndExtension.size();
	}
	public float[] get_texCoords(int id) {
		return texCoords.get(id);
	}
	public void set_texCoords(int id, float[] array) {
		texCoords.set(id,array);
	}
	public String get_texNameAndExtension(int id) {
		return nameAndExtension.get(id);
	}
	public boolean getIsFont(int id) {
		return isFont.get(id);
	}
	public int getFontSize(int id) {
		return fontSize.get(id);
	}
	public int getStrokeSize(int id) {
		return stokeSize.get(id);
	}
	public float[] getStrokeColor(int id) {
		return stokeColor.get(id);
	}
	
}
