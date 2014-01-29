package com.mammothGames.wake.game.textures;

import com.mammothGames.wake.gameEngine.base_texture;

public class tex_blank extends base_texture {
	
	public tex_blank(){
		
		nameAndExtension = "fileName";
		
		float[] tex = {1,2,3,4};
		texCoords = tex;

		// Delete below this line if not a font //
		isFont = false;
		fontSize = 0;
		stokeSize = 0;
		
		float[] colors = { 1,1,1,  0,0,0 };
		stokeColor = colors;
		
	}
}