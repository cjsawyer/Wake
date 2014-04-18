package com.mammothGames.wake1.game;

import com.mammothGames.wake1.game.textures.*;
import com.mammothGames.wake1.gameEngine.loaded_assets;

public final class game_textures {

	public static final int TEX_ERROR = 1;
	public static final int TEX_FONT1 = 2;
	
	public static final int TEX_SPRITES = 3;
	
	public static final int SUB_MUTED = 1;
	public static final int SUB_MUTE = 2;
	public static final int SUB_PAUSE = 3;
	public static final int SUB_LOGO = 4;
	public static final int SUB_MAMMOTH = 5;
	public static final int SUB_PARTICLE = 6;
	
	public static final int TEX_STARS = 4;
	
	public static void addTextures(loaded_assets loaded_textures) {
		
		loaded_textures.addTexture(TEX_ERROR, new tex_error());
		loaded_textures.addTexture(TEX_FONT1, new tex_font1());
		loaded_textures.addTexture(TEX_SPRITES, new tex_sprites());
		loaded_textures.addTexture(TEX_STARS, new tex_stars());
		
	}
}