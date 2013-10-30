package com.mammothGames.wake.testbed.feature_tests;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_loadingTest extends engine_entity {

	@Override
	public void sys_firstStep(){
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
	}
	
	private int touch_x, screen_width, screen_height;
	private float angle = 0;
	@Override
	public void sys_step(){
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		
		ref.draw.drawTexture(screen_width/2, screen_height/2, 300, 300, 0, 0, angle, 0, 1, game_textures.TEX_ERROR);
		angle += 60 * ref.main.time_scale;
		
		ref.draw.drawTexture(100, screen_height/2, 100, 100, 0, 0, 0, 0, 1, game_textures.TEX_ERROR);
		ref.draw.drawTexture(screen_width/3+100, screen_height/2, 100, 100, 0, 0, 0, 0, 1, game_textures.TEX_ERROR);
		ref.draw.drawTexture(screen_width*2/3+100, screen_height/2, 100, 100, 0, 0, 0, 0, 2, game_textures.TEX_FONT1);
		
		if (ref.input.get_touch_state(0) >= ref.input.TOUCH_DOWN) {
			touch_x = ref.input.get_touch_x(0);
			if (touch_x < screen_width/3)
				ref.textureLoader.loadTexture(game_textures.TEX_ERROR);
			
			if ((touch_x > screen_width/3)&&((touch_x < screen_width*2/3)))
				ref.textureLoader.loadTexture(game_textures.TEX_ERROR);
			
			if (touch_x > screen_width*2/3) {
				ref.textureLoader.loadTexture(game_textures.TEX_FONT1);
			}
		}
		
	}
}
