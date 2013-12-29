package com.mammothGames.wake.testbed.feature_tests;

import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_textureTest extends engine_entity {

	@Override
	public void sys_firstStep(){
		this.persistent = true;
	}
	
	@Override
	public void sys_step(){
		ref.draw.setDrawColor(1, 1, 1, 1);
		
		ref.draw.drawTexture(ref.screen_width/2, ref.screen_height/2 + 100, 128, 100, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
		
		ref.draw.drawTexture(ref.screen_width/2, ref.screen_height/2, 128, 128, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
		
		ref.draw.drawTexture(ref.screen_width/2, ref.screen_height/2 - 128, 128, 128, 0, 0, 0, 1, 1, game_textures.TEX_ERROR);
	}
}
