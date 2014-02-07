package com.mammothGames.wake.game.entities;

import android.util.Log;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuDifficulty extends engine_entity {

	masterGameReference mgr;
	public entity_menuDifficulty(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = false;
	}
	
	int num_buttons = 4;
	private boolean fade_main, fade_secondary;
	private int non_fading_button;
	
	@Override
	public void sys_firstStep() {
	}
	
	@Override
	public void sys_step() {
		
		
		if (ref.room.get_current_room() == game_rooms.ROOM_DIFFICULTY) {

			float box_w = ref.screen_width - 2*mgr.gameMain.padding_x;
			float box_h = ref.screen_height - 2*mgr.gameMain.padding_y;
			
			float button_gap = 3; // so 1/3 gap vs 1 button height
			float button_height = box_h/( num_buttons + (num_buttons-1)/button_gap);
			float button_height_gap = button_height / button_gap;
			float realative_y = ref.screen_height - mgr.gameMain.padding_y;
//			Log.e("error", "" + realative_y);

			int pressed_button = -1;
			
			// Draw the 4 buttons
			for(int i=0; i<num_buttons; i++) {
				if(i!=0)
					realative_y -= button_height_gap;
				
				float draw_x = ref.screen_width/2;
				float draw_y = realative_y - button_height/2;
				float draw_width = box_w;
				float draw_height = button_height;
				
				float button_alpha = mgr.gameMain.shade_alpha;
				
				// Slide in/out the buttons based on the shade alpha
				if ( i==non_fading_button ) {
					if (fade_main)
						button_alpha = 1;
					if (fade_secondary)
						button_alpha = mgr.gameMain.shade_alpha;
				} else {
					
					if (fade_main)
						button_alpha =  mgr.gameMain.shade_alpha;
					if (fade_secondary)
						button_alpha = 0;
					
					draw_y = draw_y * button_alpha;
					// if i is even, flip the side
//					if(i%2==0) {
//						draw_x *= -1;
//						draw_x += ref.screen_width;
//					}
					
				}
				
				
				
				//draw back, blue rectangle
				ref.draw.setDrawColor(0, 1, 1, 0.3f * button_alpha );
				ref.draw.drawRectangle(draw_x, draw_y, draw_width, draw_height, 0, 0, 0, game_constants.layer6_HUD);
				
				// draw inner black rectangle
				float button_border_size = draw_height/6;
				ref.draw.setDrawColor(0, 0, 0, 0.9f * button_alpha);
				ref.draw.drawRectangle(draw_x, draw_y, draw_width-button_border_size, draw_height-button_border_size, 0, 0, 0, game_constants.layer6_HUD);
				
				
				ref.draw.setDrawColor(1, 1, 1, 1 * button_alpha);
				switch (i) {
					case 0:
						ref.draw.drawTextSingleString(draw_x, draw_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "EASY", game_textures.TEX_FONT1);
						break;
					case 1:
						ref.draw.drawTextSingleString(draw_x, draw_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "MEDIUM", game_textures.TEX_FONT1);
						break;
					case 2:
						ref.draw.drawTextSingleString(draw_x, draw_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "HARD", game_textures.TEX_FONT1);
						break;
					case 3:
						
						float shake_range = mgr.gameMain.text_size/20;
						float dx = ref.main.randomRange(-shake_range, shake_range);
						float dy = ref.main.randomRange(-shake_range, shake_range);
						ref.draw.setDrawColor(1, 0, 0, 1 * button_alpha);
						ref.draw.drawTextSingleString(draw_x+dx, draw_y+dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "HELL", game_textures.TEX_FONT1);
						break;
				}
				
				// Check if a button is pressed.
				if ( (!fade_main) && (!fade_secondary) && (mgr.gameMain.shade_alpha > 0.98f) ) // only if we haven't already pressed one, and we're already transitioned into the room
					if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN)
						if (ref.collision.point_AABB(draw_width, draw_height, draw_x, draw_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
							pressed_button = i;
						}
				
				realative_y -= button_height;
				
				if (pressed_button != -1)
					non_fading_button = pressed_button;
				
				switch (pressed_button) {
					case 0:
						prepLeave(PREP_game);
						mgr.gameMain.setDifficulty(mgr.gameMain.DIF_EASY);
						break;
					case 1:
						prepLeave(PREP_game);
						mgr.gameMain.setDifficulty(mgr.gameMain.DIF_MEDIUM);
						break;
					case 2:
						prepLeave(PREP_game);
						mgr.gameMain.setDifficulty(mgr.gameMain.DIF_HARD);
						break;
					case 3:
						prepLeave(PREP_game);
						mgr.gameMain.setDifficulty(mgr.gameMain.DIF_HELL);
						break;
				}
				
				// Update vars for fade in/out effect
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_main) {
					// start secondary fade
					fade_main = false;
					fade_secondary = true;
					mgr.gameMain.shade_alpha = 1;
					mgr.gameMain.shade_alpha_target = 0;
					
					// No need for a secondary fade if no button was presseed on screen.
					if(room_to_leave_to == PREP_menuTop)
						leave();
				}
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_secondary )
					leave();
				
			}
		}
	}
	
	private int room_to_leave_to = -1;
	public final int PREP_menuTop = 0;
	public final int PREP_game = 1;
	
	public void prepLeave(int destination) {
		if (mgr.gameMain.shade_alpha > 0.98f) {
			room_to_leave_to = destination;
			fade_main = true;
			mgr.gameMain.shade_alpha = 1;
			mgr.gameMain.shade_alpha_target = 0;
		}
	}
	
	private void leave() {
		switch(room_to_leave_to) {
		
			case PREP_menuTop:
				mgr.menuTop.start();
				break;
			case PREP_game:
				mgr.gameMain.startGame();
				break;
				
			}
	}
	
	public void start() {
		fade_main = false;
		fade_secondary = false;
		non_fading_button = -1;
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(game_rooms.ROOM_DIFFICULTY);
	}
	
	
	
//	public void goToGame(int diff) {
//		if (mgr.gameMain.shade_alpha > 0.98f) {
//			trueForGoToGame = true;
//			fade_main = true;
//			mgr.gameMain.shade_alpha = 1;
//			mgr.gameMain.shade_alpha_target = 0;
//		}
//	}
//	
//	public void goToMenuTop() {
//		// If we're faded in, then start fading to go to the menu
//		if (mgr.gameMain.shade_alpha > 0.98f) {
//			trueForGoToGame = false;
//			fade_main = true;
//			mgr.gameMain.shade_alpha = 1;
//			mgr.gameMain.shade_alpha_target = 0;
//		}
//	}
	
}
