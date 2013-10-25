package com.mammothGames.wake.game.entities.feature_tests.old;

import com.mammothGames.wake.gameEngine.*;

import android.util.Log;


public class entity_lineTest extends engine_entity {
	
	
	@Override
	public void sys_step(){
		if (((ref.input.get_touch_state(0)==1) && (ref.input.get_touch_state(1)==1))){
			ref.draw.setDrawColor(0,0,0,1);
			ref.draw.drawLine(ref.input.get_touch_x(0), ref.input.get_touch_y(0), ref.input.get_touch_x(1), ref.input.get_touch_y(1), 50, 1);
		}
		if (((ref.input.get_touch_state(1)==1) && (ref.input.get_touch_state(2)==1))){
			ref.draw.setDrawColor(0,0,0,1);
			ref.draw.drawLine(ref.input.get_touch_x(1), ref.input.get_touch_y(1), ref.input.get_touch_x(2), ref.input.get_touch_y(2), 50, 1);
		}
		
	}
	
	@Override
	public void sys_firstStep(){
	}
	
}