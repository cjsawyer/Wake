package com.mammothGames.wake.game.entities;

import android.hardware.Camera.Area;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuMain extends engine_entity {

	//This whole thing is hack-y and copied from entity_menuDifficulty. It's clean there! :)
	
	int num_buttons = 4;
	private boolean fade_main, fade_secondary;
	private int non_fading_button;
	
	masterGameReference mgr;
	public entity_menuMain(masterGameReference mgr) {
		this.mgr = mgr;
		persistent = true;
		pausable = false;
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == game_rooms.ROOM_MENUMAIN) {

			float box_w = ref.screen_width - 2*mgr.gameMain.padding_x;
			float box_h = ref.screen_height - 2*mgr.gameMain.padding_y;
			
			float button_gap = 3; // so 1/3 gap vs 1 button height
			float button_height = box_h/( num_buttons + (num_buttons-1)/button_gap);
			float button_height_gap = button_height / button_gap;
			float realative_y = ref.screen_height - mgr.gameMain.padding_y;

			int pressed_button = -1;
			
			// Draw the 2 buttons
			for(int i=0; i<num_buttons; i++) {
				if(i!=0)
					realative_y -= button_height_gap;
				
				float draw_x = ref.screen_width/2;
				float draw_y = realative_y - button_height/2;
				float draw_width = box_w;
				float draw_height = button_height;
				
				float button_alpha = 0;
				button_alpha = mgr.gameMain.shade_alpha;
				
				// fade in/out the buttons based on the shade alpha
//				if ( i==non_fading_button ) {
//					if (fade_main)
//						button_alpha = 1;
//					if (fade_secondary)
//						button_alpha = mgr.gameMain.shade_alpha;
//				} else {
//					
				if (fade_main)
					button_alpha =  mgr.gameMain.shade_alpha;
				if (fade_secondary)
					button_alpha = 0;
//					
//				}
//				
				// hack to avoid rewriting the whole menu. Skips drawing the first two buttons
				if(i<=1) {
					button_alpha = 0;
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
					case 2:
						ref.draw.text.append(button0);
						ref.draw.drawText(draw_x, draw_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
						break;
					case 3:
						ref.draw.text.append(button1);
						ref.draw.drawText(draw_x, draw_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
						break;
				}

				// If the AYSYWTQ? popup isn't open
				if (!mgr.areYouSure.getPopupOpenness())
					// Check if a button is pressed.
					if ( (!fade_main) && (!fade_secondary) && (mgr.gameMain.shade_alpha > 0.98f) ) // only if we haven't already pressed one, and we're already transitioned into the room
						if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN)
							if (ref.collision.point_AABB(draw_width, draw_height, draw_x, draw_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
								pressed_button = i;
							}
				
//				if (i==2) {
//					logo_y_target = (ref.screen_height + realative_y)/2 - mgr.menuFirst.logo_h/4;
//				}
				
				realative_y -= button_height;
				
				if (pressed_button != -1)
					non_fading_button = pressed_button;
				
				switch (pressed_button) {
					case 2:
						prepLeave(PREP_menuDifficulty);
						break;
					case 3:
						prepLeave(PREP_menuRecords);
						break;
				}
				
				// Update vars for fade in/out effect
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_main) {
					leave();
					// start secondary fade
//					fade_main = false;
//					fade_secondary = true;
//					mgr.gameMain.shade_alpha = 1;
//					mgr.gameMain.shade_alpha_target = 0;
				}
//				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_secondary )
//					leave();
			}
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.drawTexture(
				ref.screen_width/2,
//				ref.screen_height - mgr.gameMain.padding_y/2 - mgr.menuFirst.logo_h/4,
				ref.screen_height - mgr.gameMain.padding_y/2 - button_height,
				mgr.menuFirst.logo_w, mgr.menuFirst.logo_h,
				0,
				mgr.menuFirst.logo_h/2f,
				0,
				game_constants.layer6_HUD,
				game_textures.SUB_LOGO,
				game_textures.TEX_SPRITES
			);
		}
	}
	
	
	public final int PREP_menuDifficulty = 0;
	public final int PREP_menuRecords = 1;
	public final int PREP_menuMain = 4;
	
	public void prepLeave(int destination) {
		if (mgr.gameMain.shade_alpha > 0.98f) {
			room_to_leave_to = destination;
			fade_main = true;
			mgr.gameMain.shade_alpha = 1;
			mgr.gameMain.shade_alpha_target = 0;
//			mgr.menuFirst.logo_alpha = 1;
//			mgr.menuFirst.logo_alpha_target = 0;
		}
	}
	
	
	private final String button0 = "PLAY";
	private final String button1 = "RECORDS";
	
	private int room_to_leave_to = -1;
	
	private void leave() {
		switch(room_to_leave_to) {
			case PREP_menuDifficulty:
				mgr.menuDifficulty.start();
				break;
			case PREP_menuRecords:
				mgr.menuRecords.start();
				break;
			case PREP_menuMain:
				mgr.menuFirst.start();
				break;
		}
	}

	
	public void start() {
		mgr.gameMain.floor_height_target = 0; // Make the water go back down
		fade_main = false;
		fade_secondary = false;
		non_fading_button = -1;
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
//		mgr.menuFirst.logo_alpha_target = 1;
		ref.room.changeRoom(game_rooms.ROOM_MENUMAIN);
	}
	
}
