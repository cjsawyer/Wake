package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuOptions extends engine_entity {

	int num_buttons = 4;
	private boolean fade_main, fade_secondary;
	private int non_fading_button;
	
	masterGameReference mgr;
	public entity_menuOptions(masterGameReference mgr) {
		this.mgr = mgr;
		persistent = true;
		pausable = false;
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == game_rooms.ROOM_MENUOPTIONS) {

			
				ref.draw.setDrawColor(1, 1, 1, 0.9f * mgr.gameMain.shade_alpha);
				ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "options menu here", game_textures.TEX_FONT1);
				
				
				
				// Update vars for fade in/out effect
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_main) {
					// start secondary fade
					fade_main = false;
					fade_secondary = true;
					//mgr.gameMain.shade_alpha = 1;
					//un comment out if this is a 2 part effect
					mgr.gameMain.shade_alpha_target = 0;
				}
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_secondary )
					leave();
				
		}
	}

	
	private int room_to_leave_to = -1;
	public final int PREP_menuTop = 0;
	
	public void prepLeave(int destination) {
		if (mgr.gameMain.shade_alpha > 0.98f) {
			room_to_leave_to = destination;
			//TODO			fade_main = true;
			fade_secondary = true;
			mgr.gameMain.shade_alpha = 1;
			mgr.gameMain.shade_alpha_target = 0;
		}
	}
	
	private void leave() {
		switch(room_to_leave_to) {
			case PREP_menuTop:
				mgr.menuMain.start();
				break;
			}
	}
	
	public void start() {
		fade_main = false;
		fade_secondary = false;
		non_fading_button = -1;
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(game_rooms.ROOM_MENUOPTIONS);
	}
	
	
}
