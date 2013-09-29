package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.gameEngine.*;


public class entity_roomTest extends engine_entity {

	entity_roomTestButton button_back;
	entity_roomTestButton button_next;
	
	@Override
	public void sys_firstStep(){
		
		persistent = true;
		
		button_back = new entity_roomTestButton();
		button_next = new entity_roomTestButton();
		
		ref.main.addEntity(button_back);
		button_back.x = 100;
		button_back.y = 100;
		
		ref.main.addEntity(button_next);
		button_next.x = 300;
		button_next.y = 100;
		
		button_back.button_text = "back";
		button_next.button_text = "next";
	}
	
	@Override
	public void sys_step(){
		if (button_back.touched) {
			if (ref.room.get_current_room() > 1){
				ref.room.changeRoom(ref.room.get_current_room()-1);
			}
		} else if (button_next.touched) {
				button_next.touched = false;
				ref.room.changeRoom(ref.room.get_current_room()+1);
		}
		button_back.touched = false;
		button_next.touched = false;
		
		if(ref.input.get_touch_state(0)==ref.input.TOUCH_UP){
			
		}
		
	}
	
}

class entity_roomTestButton extends engine_entity {

	//Boolean buttonTouched = false;
	//Boolean buttonHeld = false;
	//private int steps_held = 0;
	String button_text = "default";
	float x, y;
	
	@Override
	public void sys_firstStep(){
		persistent = true;
	}
	
	private int temp_touchState = 0;
	boolean touched = false;
	@Override
	public void sys_step(){
		
		temp_touchState = ref.input.get_touch_state(0);
		
		if (temp_touchState == ref.input.TOUCH_UP){
			if (Math.hypot(Math.abs(x - ref.input.get_touch_x(0)), Math.abs(y - ref.input.get_touch_y(0))) < 100){
				touched = true;
			}
		}
		
		if ((temp_touchState > 1)&&(Math.hypot(Math.abs(x - ref.input.get_touch_x(0)), Math.abs(y - ref.input.get_touch_y(0))) < 100)){
			ref.draw.setDrawColor(0, 1, 0, 1);
		} else {
			ref.draw.setDrawColor(0, 0, 0, 1);
		}
		ref.draw.drawRectangle(x, y, 100, 100, 0, 0, 0, 100);
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		// FIX ME ref.draw.drawText(x, y, 40, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 101, button_text, 4);
		
	}
	
}
