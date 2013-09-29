package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.gameEngine.*;

import android.os.SystemClock;
import android.util.Log;

public class entity_BLANK extends engine_entity {
	
	
	// Vars for stress test.
	float r = (float) (Math.random());
	float g = (float) (Math.random());
	float b = (float) (Math.random());
	float image_angle, x, y;
	
	@Override
	public void sys_step(){
		
		
		//Stress test for threading
		// If screen is being pressed
		if (ref.input.get_touch_state(0) > 0){
			// If touch is within the size of the sprite, delete this instance.
			//instance_destroy();
//			/*
			if (Math.hypot(Math.abs(x - ref.input.get_touch_x(0)), Math.abs(y - ref.input.get_touch_y(0))) < 50){
				instance_destroy();
			}
			
//			*/
			image_angle = (float) ( (180/Math.PI) * Math.atan2(y-ref.input.get_touch_y(0), x - ref.input.get_touch_x(0)));
			
		}
		ref.draw.setDrawColor(r, g, b, 1);
		ref.draw.drawTexture(x-50, y, 50, 50, 0, 0, image_angle+180, 0, 1, 3); // OBAMA!
		ref.draw.drawTexture(x+50, y, 50, 50, 0, 0, image_angle, 0, 2, 2); // I loaded a texture!
		ref.draw.drawTexture(x, y, 50, 50, 0, 0, image_angle, 0, 1, 2); // I loaded a texture!
		
		ref.draw.drawTexture(x, y+50, 50, 50, 0, 0, image_angle+180, 0, 1, 3); // OBAMA!
		ref.draw.drawTexture(x, y-50, 50, 50, 0, 0, image_angle+180, 0, 1, 3); // OBAMA!
		
		
//		for (int i = 0; i < 1000; i++) {
//			temp_i = (int) (Math.random() * Math.random() * Math.random() * Math.random() * Math.random());
//		}
		/*
		for (temp_i = 0; temp_i < 100; temp_i++) {
			//Math.ceil((double) Math.random() * Math.asin(Math.hypot(Math.random(), Math.random())));
		ref.draw.drawTexture(x, y, 50, 50, 0, 0, (360.0f / 2000.0f) * ((int) SystemClock.uptimeMillis() % 10000L), 400, 1, 2);
		}
//		*/
		
		
		/*
		// Draws two textures, one at touch point 1, which changes to full transparency when being touched, and another at the second touch.
		if(ref.android.sys_get_touch_state(0) == true){
			ref.draw.changeDrawColor(1, 0, 1, 1);
		} else {
			ref.draw.changeDrawColor(1, 0, 1, .5f);			
		}
		ref.draw.drawTexture(ref.android.sys_get_touch_x(0), ref.android.sys_get_touch_y(0), 100, 100, 0, 0, 0, 400, 1, 2);
		
		ref.draw.changeDrawColor(0, 1, 1, 1);
		ref.draw.drawTexture(ref.android.sys_get_touch_x(1), ref.android.sys_get_touch_y(1), 100, 100, 0, 0, 0, 400, 1, 2);
		 */

		/*
		// Example checking what type of entity this one is.
		if(ref.main.checkEntityType(get_id(), "entity_BLANK")){
			ref.draw.drawText(300, 300, 40, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 0, "YES, I am: " + entity_get_type(), 4);
		}
		*/
	}
	
	@Override
	public void sys_firstStep(){
		x = ((int) (Math.random()*ref.main.get_screen_width()));
		y = ((int) (Math.random()*ref.main.get_screen_height()));
	}
	
	//@Override
	//public void sys_beforeStep(){step();}
	
}

class entity_test_text extends engine_entity {
	float temp_size;
	@Override
	public void sys_step(){
		ref.draw.setDrawColor(1, 1, 1, 1);
		temp_size = (200.0f / 2000.0f) * ((int) SystemClock.uptimeMillis() % 2000L);
		// ref.draw.drawText(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, temp_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 0, "123456789 ;)", 4);
	}
}
