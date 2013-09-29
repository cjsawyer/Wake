package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;

public class entity_multiTouchTestPoint extends engine_entity {

	float r=1;
	float g=1;
	float b=1;
	
	public int touch_index;
	float x,y;
	
	@Override
	public void sys_step(){
		if ( (ref.input.get_touch_state(touch_index)==ref.input.TOUCH_DOWN) || (ref.input.get_touch_state(touch_index)==ref.input.TOUCH_HELD) ) {
			
			x = ref.input.get_touch_x(touch_index);
			y = ref.input.get_touch_y(touch_index);
			
			ref.draw.setDrawColor(r, g, b, 1);
		} else {
			ref.draw.setDrawColor(r, g, b, .2f);
		}
		
//		ref.draw.drawTexture(x, y, 100, 100, 0, 0, 0, 400, 1, 2);
		ref.draw.drawCircle(x, y, 50, 0, 0, 360, 0, 400);
		
		
		ref.draw.setDrawColor(0, 0, 0, 1);
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(touch_index);
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x, y, 50, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 400, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
	}
	
	@Override
	public void sys_firstStep(){
	}
}

