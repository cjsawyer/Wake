package com.reywas.testGameName.game.entities.feature_tests;

import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;


public class entity_saveTest extends engine_entity {
	
	private int[] x;
	private int number_buttons, size, screenx, screeny, y, local=0, external=-1000;
	
	@Override
	public void sys_firstStep(){
		number_buttons = 4;
		
		x = new int[number_buttons];
		
		screenx = ref.main.get_screen_width();
		screeny = ref.main.get_screen_height();
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
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "down"  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[0], y, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
		
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "up"  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[1], y, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
		
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "save"  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[2], y, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
		
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "load"  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[3], y, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
		
		
		ref.draw.setDrawColor(0, 0, 0, 1);
		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "local: "  );
		ref.strings.builder.append(  local  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[number_buttons/2]-(size*3/4), y+size, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);

		ref.strings.builder.setLength(0);
		ref.strings.builder.append(  "external: "  );
		ref.strings.builder.append(  external  );
		ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
		ref.draw.drawText(x[number_buttons/2]-(size*3/4), y-size, size/3, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 10, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);

		

		if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
			
			if (ref.collision.point_AABB(size, size, x[0], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				// Down
				local -= 1;
			} else if (ref.collision.point_AABB(size, size, x[1], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				// Up
				local += 1;
				
			} else if (ref.collision.point_AABB(size, size, x[2], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				// Save
				ref.file.save("test", String.valueOf(local));
//				ref.file.save("test", 123 + " test!! asdasdsd " + false);
				
			} else if (ref.collision.point_AABB(size, size, x[3], y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
				// Load
				String temp_string = ref.file.load("test");
				if (temp_string != "")
					external = Integer.valueOf(temp_string);
//				Log.e("asd", "here: " + load("test"));

			}
			
		}
		
	}
	
}