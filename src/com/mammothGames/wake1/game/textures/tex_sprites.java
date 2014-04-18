package com.mammothGames.wake1.game.textures;

import com.mammothGames.wake1.gameEngine.base_texture;

public class tex_sprites extends base_texture {
	
	public tex_sprites(){
		
		nameAndExtension = "wakesheet.png";
		
		float[] tex = { 1024, 1024,  0,950+64, 0+64,950+64, 0,950, 0+64,950,  0,886+64, 0+64,886+64, 0,886, 0+64,886,  0,822+64, 0+64,822+64, 0,822, 0+64,822,  0,0+370, 0+600,0+370, 0,0, 0+600,0,  0,370+452, 0+454,370+452, 0,370, 0+454,370,  64,822+58, 64+58,822+58, 64,822, 64+58,822,  };
		texCoords = tex;

	}
}