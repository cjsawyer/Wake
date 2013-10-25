package com.mammothGames.wake.game.entities.feature_tests;

import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_collisionTest extends engine_entity {

	@Override
	public void sys_firstStep(){
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
	}
	
	private int screen_width, screen_height, box_size = 200;
	@Override
	public void sys_step(){
		if (ref.input.get_touch_state(0) >= ref.input.TOUCH_DOWN) {
			
			ref.draw.setDrawColor(0, 0, 0, 1);
			ref.draw.drawLine(screen_width, ref.input.get_touch_y(0), 0, ref.input.get_touch_y(0), 5, 1);
			ref.draw.drawLine(ref.input.get_touch_x(0), 0, ref.input.get_touch_x(0), screen_height, 5, 1);
			
			if (ref.collision.point_circle(ref.input.get_touch_x(0), ref.input.get_touch_y(0), screen_width/2, screen_height/2, box_size/2)){
//				if (!ref.collision.point_AABB(box_size, box_size, screen_width/2, screen_height/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				ref.draw.setDrawColor(0.5f, 0.5f, 0.5f, 1);
//				ref.draw.drawTexture(screen_width/2, screen_height/2, box_size, box_size, 0, 0, 0, 0, 1, game_textures.TEX_SPRITES1);
			} else {
				ref.draw.setDrawColor(0.5f, 1, 1, 1);
			}
			ref.draw.drawCircle(screen_width/2, screen_height/2, box_size/2, 0, 0, 360, 0, 0);
		} else {
			ref.draw.setDrawColor(0.5f, 1, 1, 1);
			ref.draw.drawCircle(screen_width/2, screen_height/2, box_size/2, 0, 0, 360, 0, 0);
//			ref.draw.drawTexture(screen_width/2, screen_height/2, box_size, box_size, 0, 0, 0, 0, 1, game_textures.TEX_SPRITES1);
		}
	}
	
}
