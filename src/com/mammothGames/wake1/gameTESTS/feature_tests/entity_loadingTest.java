package com.mammothGames.wake1.gameTESTS.feature_tests;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_loadingTest extends engine_entity {

	@Override
	public void sys_firstStep(){
	}
	
	private int touch_x;
	private float angle = 0;
	@Override
	public void sys_step(){
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		
		ref.draw.drawTexture(ref.screen_width/2, ref.screen_height/2, 300, 300, 0, 0, angle, 0, 1, textures.TEX_ERROR);
		angle += 60 * ref.main.time_scale;
		
		ref.draw.drawTexture(100, ref.screen_height/2, 100, 100, 0, 0, 0, 0, 1, textures.TEX_ERROR);
		ref.draw.drawTexture(ref.screen_width/3+100, ref.screen_height/2, 100, 100, 0, 0, 0, 0, 1, textures.TEX_ERROR);
		ref.draw.drawTexture(ref.screen_width*2/3+100, ref.screen_height/2, 100, 100, 0, 0, 0, 0, 2, textures.TEX_FONT1);
		
		if (ref.input.get_touch_state(0) >= ref.input.TOUCH_DOWN) {
			touch_x = ref.input.get_touch_x(0);
			if (touch_x < ref.screen_width/3)
				ref.textureLoader.loadTexture(textures.TEX_ERROR);
			
			if ((touch_x > ref.screen_width/3)&&((touch_x < ref.screen_width*2/3)))
				ref.textureLoader.loadTexture(textures.TEX_ERROR);
			
			if (touch_x > ref.screen_width*2/3) {
				ref.textureLoader.loadTexture(textures.TEX_FONT1);
			}
		}
		
	}
}
