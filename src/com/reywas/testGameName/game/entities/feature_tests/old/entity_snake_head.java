package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.gameEngine.*;

public class entity_snake_head extends engine_entity {
	
	int number_of_body_segments = 80;
	float[] segments_x = new float[number_of_body_segments];
	float[] segments_y = new float[number_of_body_segments];
	
	int temp_i;
	float x, y;
	
	int touch_id = 0;

	public void setTouchID(int id){
		touch_id = id;
	}
	
	@Override
	public void sys_step(){

		// If the screen is being touched
		if (ref.input.get_touch_state(touch_id)==1){
			
			// Get the touch position
			x = ref.input.get_touch_x(touch_id);
			y = ref.input.get_touch_y(touch_id);
			
			// Ratchet all of the segments up one spot
			for(temp_i=0;temp_i<number_of_body_segments-1; temp_i++){
				segments_x[temp_i] = segments_x[temp_i+1];
				segments_y[temp_i] = segments_y[temp_i+1];
			}

			// Move the last segment to the touch position
			segments_x[number_of_body_segments-1] = x;
			segments_y[number_of_body_segments-1] = y;
			
		}

		//Draw body segments
		ref.draw.setDrawColor(0, 0f, 0f, .4f);
		//image_angle = (360.0f / 2000.0f) * ((int) SystemClock.uptimeMillis() % 10000L);
		for(temp_i=0;temp_i<number_of_body_segments-1; temp_i++){
			ref.draw.drawRectangle(segments_x[temp_i], segments_y[temp_i], 50, 50, 0, 0, 0, 10);			
			//ref.draw.drawTexture(x, y, 50, 50, 0, 0, image_angle, 10, 1, 2);			
		}
		
		// Draw the head
		if (touch_id == 0){
			ref.draw.setDrawColor(1, .5f, .5f, 1);
		} else if (touch_id == 1){
			ref.draw.setDrawColor(.5f, 1, .5f, 1);
		} else if (touch_id == 2){
		ref.draw.setDrawColor(.5f, .5f, 1, 1);
		}
		ref.draw.drawRectangle(x, y, 50, 50, 0, 0, 0, 20);
	}
}
