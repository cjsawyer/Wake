package com.mammothGames.wake.gameEngine;

import java.util.HashMap;
import java.util.Map;

import com.mammothGames.wake.game.*;

public class engine_reference {
	
	public engine_android android; // ie engine_android
	public engine_gl_draw draw;
	public engine_main main;
	public engine_gl_textureLoader textureLoader;
	public engine_room room;
	public engine_input input; 
	public engine_strings strings;
	public engine_gl_text text;
	public engine_collision collision;
	public engine_sound sound;
	public engine_file file;
	public engine_adMob ad;
	public engine_loadHelper loadHelper;

	
	protected engine_gl_texture texture;
	protected engine_gl_renderer renderer;
	protected engine_gl_rectangle rectangle;
	protected engine_gl_circle circle;
	protected engine_gl_floatbuffers floatbuffers;
	protected engine_particlePool partPool;
	protected engine_hud hud;
	protected engine_gl_matrix matrix;
	 
	protected int current_texture_sheet = 0;
	protected int current_texture_id = 0;
	
	protected float system_clearColor_r = 1f;
	protected float system_clearColor_g = 1f;
	protected float system_clearColor_b = 1f;
	
	
	public int screen_width, screen_height;

	
	public engine_reference() {
		g_textures = new loaded_assets();
	}
	
	protected loaded_assets g_textures;
	
	
}

