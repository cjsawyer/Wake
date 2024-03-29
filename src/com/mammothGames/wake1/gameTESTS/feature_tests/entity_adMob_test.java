package com.mammothGames.wake1.gameTESTS.feature_tests;

import com.mammothGames.wake1.game.sounds;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_adMob_test extends engine_entity {

	private int[] x;
	private int number_buttons, size, screenx, screeny, y;

	@Override
	public void sys_firstStep(){
		
		number_buttons = 2;
		
		x = new int[number_buttons];
		
		screenx = ref.screen_width;
		screeny = ref.screen_height;
		y= screeny/2;
		size = screenx/((number_buttons+1)*3/2);
		
		for(int temp_i=0; temp_i<number_buttons; temp_i++) {
			x[temp_i] = (int) (size/2f + temp_i*size*3f/2f);
		}
		
		int tmp = (int) screenx/2 - x[(number_buttons)/2] + ((number_buttons-1)%2)*size/2 + ((number_buttons-1)%2)*size/4;
		for(int temp_i=0; temp_i<number_buttons; temp_i++) {
			x[temp_i] += tmp;//(int) (x[number_buttons-1]);
		}
		
	}
	
	@Override
	public void sys_step(){
		
		for(int temp_i=0; temp_i<number_buttons; temp_i++) {
			ref.draw.setDrawColor(0, 0, 0, 1);
			ref.draw.drawRectangle(x[temp_i], y, size, size, 0, 0, 0, 0);
		}
		
		
		ref.draw.setDrawColor(0, 1, 0, 1);
		ref.draw.text.append(  "load"  );
		ref.draw.drawText(x[0], y, size/4, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 10, textures.TEX_FONT1);
		
		ref.draw.text.append(  "unload"  );
		ref.draw.drawText(x[1], y, size/4, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 10, textures.TEX_FONT1);
		
		
		if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
			
			if (ref.collision.point_AABB(size, size, x[0], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				ref.ad.loadBannerAd(ref.ad.H_RIGHT, ref.ad.V_BOTTOM);
			}
			if (ref.collision.point_AABB(size, size, x[1], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				ref.ad.unLoadBannerAd();
			}
		}
	}
	
}
