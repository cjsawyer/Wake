package com.reywas.testGameName.game.entities.feature_tests;

import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;


public class entity_textureTest extends engine_entity {

	int w,h;
	@Override
	public void sys_firstStep(){
		this.persistent = true;
		w = ref.main.get_screen_width();
		h = ref.main.get_screen_height();
	}
	
	@Override
	public void sys_step(){
		ref.draw.setDrawColor(1, 1, 1, 1);
		
		ref.draw.drawTexture(w/2, h/2 + 100, 128, 100, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
		
		ref.draw.drawTexture(w/2, h/2, 128, 128, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
		
		ref.draw.drawTexture(w/2, h/2 - 128, 128, 128, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
	}
}
