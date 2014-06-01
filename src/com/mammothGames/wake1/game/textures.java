package com.mammothGames.wake1.game;

import com.mammothGames.wake1.gameEngine.base_texture;
import com.mammothGames.wake1.gameEngine.loaded_assets;

public final class textures {

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

class tex_error extends base_texture {
	
	public tex_error(){

		nameAndExtension = "error.png";
		
		float[] tex = { 32, 32,   0,32, 32,32, 0,0, 32,0, };
		texCoords = tex;
		
	}
}

class tex_font1 extends base_texture {
	
	public tex_font1(){
		
		nameAndExtension = "Square.ttf";
		
		isFont = true;
		fontSize = 50;
		stokeSize = 5;
		
		float[] colors = { 0,    0,    0,       1,    1,    1};
		stokeColor = colors;
		
	}
}


class tex_sprites extends base_texture {
	
	public tex_sprites(){
		
		nameAndExtension = "wakesheet.png";
		
		float[] tex = { 1024, 1024,  0,950+64, 0+64,950+64, 0,950, 0+64,950,  0,886+64, 0+64,886+64, 0,886, 0+64,886,  0,822+64, 0+64,822+64, 0,822, 0+64,822,  0,0+370, 0+600,0+370, 0,0, 0+600,0,  0,370+452, 0+454,370+452, 0,370, 0+454,370,  64,822+58, 64+58,822+58, 64,822, 64+58,822,  };
		texCoords = tex;

	}
}

class tex_stars extends base_texture {
	
	public tex_stars(){
		
		nameAndExtension = "stars.png";
		
		float[] tex = { 512, 512, 0,512, 512,512, 0,0, 512,0};
		texCoords = tex;
		
	}
}