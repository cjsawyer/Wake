package com.mammothGames.wake1.gameEngine;

import com.mammothGames.wake1.game.game_constants;
import com.mammothGames.wake1.game.game_textures;


public class engine_hud {

	engine_reference ref;
	
	int temp_i;
	
	int system_x = 0;
	
	int screen_width, screen_height, hud_width;
	
	boolean hidden = true;
	private int target_x = 0;
	

	
	// These are for the average FPS from the last <adjustedFPSLength> frames.
	public float adjusted_FPS;
	private float[] adjusted_FPS_array;
	
	private int adjusted_FPS_graph_length;
	private int adjusted_FPS_counter_length = 15;
	
	protected int debug_hud_text_size;//px
	
	private float graph_height;
	
	private float graph_x_scale;
	private float graph_temp_height;
	private float graph_height_index;
	
	public engine_hud(engine_reference r) {
		ref = r;
	}
	
	protected void initiate() {
		if (game_constants.devmode) {
			
			// A little hacky, but it keeps it the same size regardless of orientation.
			if (game_constants.is_landscape) {
				screen_width = ref.screen_width;
				screen_height = ref.screen_height;
			} else {
				screen_width = ref.screen_height;
				screen_height = ref.screen_width;
			}
			
			debug_hud_text_size = (int) (screen_height * 0.04);
			graph_height = 2*debug_hud_text_size;
			
			hud_width = screen_width/4;
			
			adjusted_FPS_graph_length = hud_width/2;
			adjusted_FPS_array = new float[adjusted_FPS_graph_length];
			for (temp_i = 0; temp_i<adjusted_FPS_graph_length; temp_i++) {
				adjusted_FPS_array[temp_i] = 10000;
			}
			
			graph_x_scale = hud_width/adjusted_FPS_graph_length;
			
			
			button_back = new entity_hudButton();
			button_next = new entity_hudButton();
			
			int button_height = debug_hud_text_size*4;
			int button_width = hud_width/2;
			
			button_back.height = button_height;
			button_next.height = button_height;
			
			button_back.width = button_width;
			button_next.width = button_width;
			
			ref.main.addEntity(button_back);
			button_back.x = button_width/2;
			button_back.y = button_width/2;
			
			ref.main.addEntity(button_next);
			button_next.x = button_width*3/2;
			button_next.y = button_width/2;
			
			button_back.button_text = "back";
			button_next.button_text = "next";
			
			button_back.y = ref.screen_height - 9 * debug_hud_text_size;
			button_next.y = ref.screen_height - 9 * debug_hud_text_size;
		}
	}
	
	void update() {
		
		button_back.update();
		button_next.update();
		
		//Logic for the room changing buttons
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
		
		
		if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
			if ((ref.input.get_touch_x(0) < 100)&&(ref.input.get_touch_y(0) > ref.screen_height - 100)) {
				hidden = !hidden;
			}
		}

		
		
		// Set the slide destination point
		if (hidden) {
			target_x = -hud_width;
			
			// Draw JUST the FPS, tiny in the corner.
			ref.draw.setDrawColor(1, 0, 0, 1);
			
			ref.draw.text.append(  adjusted_FPS   );
			ref.draw.drawText(0, ref.screen_height, debug_hud_text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);
			
		} else {
			button_next.draw = true;
			button_back.draw = true;
			target_x = 0;
		}
		
			
			
			
			/// FPS graph
			// Ratchet recorded fps values up once place in the array
			for (temp_i = adjusted_FPS_graph_length-1; temp_i>0; temp_i--) {
				adjusted_FPS_array[temp_i] = adjusted_FPS_array[temp_i-1];
			}
			// Add the new one to the bottom
			adjusted_FPS_array[0] = ref.main.time_delta;
			
			// Average the values of the part of the list that we're using for the FPS counter
			for (temp_i = 0; temp_i<adjusted_FPS_counter_length-1; temp_i++) {
				adjusted_FPS += 1000f/adjusted_FPS_array[temp_i];
			}
			adjusted_FPS /= adjusted_FPS_counter_length;
			adjusted_FPS = Math.round(adjusted_FPS);
			
		if (system_x > -hud_width + 5) {
			// Draw the background
			ref.draw.setDrawColor(.75f, .75f, .75f, .6f);
			ref.draw.drawRectangle(system_x + hud_width/2, ref.screen_height - 7*debug_hud_text_size/2, hud_width, 7*debug_hud_text_size, 0, 0, 0, 499);
	
			//Draw the graph
			for(temp_i=0; temp_i<adjusted_FPS_graph_length-1; temp_i++) {
				graph_height_index = (adjusted_FPS_array[temp_i]-16.6666f)/50f;//Range is 16.6666 to 66.6666, ofset by -16.6666 to zero on both ends.
				graph_temp_height = graph_height_index*graph_height;
				ref.draw.setDrawColor(graph_height_index, (1f-graph_height_index), 0, 1);
				
				ref.draw.drawRectangle(system_x + hud_width - (graph_x_scale*(temp_i+1)), (ref.screen_height - graph_height), graph_x_scale, graph_temp_height, -graph_x_scale/2, graph_temp_height/2, 0, 500);			
			}
			/// End FPS graph
	
			
			
			//Draw the text
			ref.draw.setDrawColor(0, 0, 0, 1);
	
			ref.draw.text.append("FPS: ");
			ref.draw.text.append(  adjusted_FPS   );
			ref.draw.drawText(system_x, ref.screen_height-(2*debug_hud_text_size), debug_hud_text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);
			
			ref.draw.text.append("Room:");
			ref.draw.text.append(  " ("   );
			ref.draw.text.append(  ref.room.get_current_room()  );
			ref.draw.text.append(  ") "   );
			ref.draw.text.append(  ref.room.get_current_room_name()   );
			ref.draw.drawText(system_x, ref.screen_height-(3*debug_hud_text_size), debug_hud_text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);
			
			ref.draw.text.append("Entities: ");
			ref.draw.text.append(  "("   );
			ref.draw.text.append(  ref.main.entities_total   );
			ref.draw.text.append(  ") "   );
			ref.draw.text.append(  ref.main.entities_current   );
			ref.draw.drawText(system_x, ref.screen_height-(4*debug_hud_text_size), debug_hud_text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);
			
			ref.draw.text.append("Particles: ");
			ref.draw.text.append(  ref.partPool.particles_current   );
			ref.draw.drawText(system_x, ref.screen_height-(5*debug_hud_text_size), debug_hud_text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);

			
			ref.draw.text.append("Touch: ");
			ref.draw.text.append(  ref.input.get_touch_state(0)   );
			ref.draw.text.append(  ref.input.get_touch_state(1)   );
			ref.draw.text.append(  ref.input.get_touch_state(2)   );
			ref.draw.text.append(  ref.input.get_touch_state(3)   );
			ref.draw.text.append(  ref.input.get_touch_state(4)   );
			ref.draw.drawText(system_x, ref.screen_height-(6*debug_hud_text_size), debug_hud_text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, 500, game_textures.TEX_FONT1);
		} else {
			button_next.draw = false;
			button_back.draw = false;
		}
		
		
		system_x += (target_x - system_x) * 10 * ref.main.time_scale;
	}
	
	entity_hudButton button_back;
	entity_hudButton button_next;
	

	
	
}

class entity_hudButton extends engine_entity {

	String button_text = "null";
	float x,y;
	
	@Override
	public void sys_firstStep(){
		persistent = true;
	}
	
	int width = 100;
	int height = 100;
	
	private int temp_touchState = 0;
	protected boolean touched = false;
	
	boolean draw = true;
	
	public void update(){
		
		if (draw) {
			temp_touchState = ref.input.get_touch_state(0);
			
			if (temp_touchState == ref.input.TOUCH_UP){
				if (Math.hypot(Math.abs(x - ref.input.get_touch_x(0)), Math.abs(y - ref.input.get_touch_y(0))) < width/2){
					touched = true;
				}
			}
			
			if ((temp_touchState >= ref.input.TOUCH_DOWN)&&(Math.hypot(Math.abs(x - ref.input.get_touch_x(0)), Math.abs(y - ref.input.get_touch_y(0))) < width/2.5f)){
				ref.draw.setDrawColor(0, 1, 0, .65f);
			} else {
				ref.draw.setDrawColor(0, 0, 0, .65f);
			}
			ref.draw.drawRectangle(ref.hud.system_x + x, y, width, height, 0, 0, 0, 500);
			
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			
			ref.draw.text.append(  button_text   );
			ref.draw.drawText(ref.hud.system_x +x, y, ref.hud.debug_hud_text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 500, game_textures.TEX_FONT1);
		}
		
	}
	
}
