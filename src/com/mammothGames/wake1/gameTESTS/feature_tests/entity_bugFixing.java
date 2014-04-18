package com.mammothGames.wake1.gameTESTS.feature_tests;

import com.mammothGames.wake1.game.game_textures;
import com.mammothGames.wake1.gameEngine.engine_entity;


public class entity_bugFixing extends engine_entity {

	@Override
	public void sys_firstStep(){
		int a;;;
	}

	int i;
	@Override
	public void sys_step(){
		
		ref.draw.setDrawColor(0, 0, 0, 1);
		
//		for(i=0; i<20; i++) {
			/*
			ref.strings.temp_stringBuilder.setLength(0);
			ref.strings.temp_stringBuilder.append(  "Testing123ABC"  );
			ref.strings.temp_stringBuilder.getChars(0, ref.strings.temp_stringBuilder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText((float)Math.random()*ref.main.get_screen_width(), (float)Math.random()*ref.main.get_screen_height(), 30, ref.draw.X_ALIGN_CENTER, ref.draw.X_ALIGN_CENTER, 0, 0, ref.strings.stringChars, ref.strings.temp_stringBuilder.length(), game_textures.TEX_FONT1);
			//*/
//		ref.draw.drawText((float)Math.random()*ref.main.get_screen_width(), (float)Math.random()*ref.main.get_screen_height(), 30, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, "Testing123ABC", game_textures.TEX_FONT1);
//		}
		
		
		
	}
	
	/*
	 
	float x1=0, y1=0, x2=0, y2=0;
	@Override
	public void sys_step(){
		if(ref.input.get_touch_state(0)>=ref.input.TOUCH_DOWN) {
			x1 = ref.input.get_touch_x(0);
			y1 = ref.input.get_touch_y(0);
			
			x2 = ref.input.get_touch_x(1);
			y2 = ref.input.get_touch_y(1);
		}
		
		ref.draw.changeDrawColor(0, 0, 0, 1);
		ref.draw.drawLine(x2, y2, x1, y1, 10, 10);
	}
	 
	 */
}
