package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuRecords extends engine_entity {

	private boolean fade_main;
	private float tab_w = 0;
	private final int tabs = 4;
	private int tab = 0;
	
	masterGameReference mgr;
	public entity_menuRecords(masterGameReference mgr) {
		this.mgr = mgr;
		persistent = true;
		pausable = false;
	}
	
	@Override
	public void sys_firstStep() {
		tab_w = ref.screen_width/4f;
	}
	
	float offset = 0;
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == game_rooms.ROOM_MENURECORDS) {
			
				offset = ref.screen_width * (mgr.gameMain.shade_alpha - 1);

				// the y of the bottom part of the tab group. Defines the top of the content area.
				float tab_base_y;
			
				// right and left ends of the horizontal blue rectangles under the tabs and above the content
				float bluex=0, bluex2=ref.screen_width;
				
				//Draw tabs
				for (int i=0; i<tabs; i++) {
					
					float black_h = tab_w - mgr.menuDifficulty.button_border_size/2;
					float black_offset = mgr.menuDifficulty.button_border_size/4;
					
					float draw_x = tab_w*i + tab_w/2 + offset;
					float draw_y = ref.screen_height - tab_w/2;
					
					if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN)
						if (ref.collision.point_AABB(tab_w, tab_w, draw_x, draw_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
							tab = i;
						}
					
					// Set color for background part of tab
					if (i == tab) {
						ref.draw.setDrawColor(0, 1, 1, 0.9f);
						black_h = tab_w;
						black_offset = mgr.menuDifficulty.button_border_size/2;
						bluex = draw_x - tab_w/2 + mgr.menuDifficulty.button_border_size/2;
						bluex2 = draw_x + tab_w/2 - mgr.menuDifficulty.button_border_size/2;
					} else {
						ref.draw.setDrawColor(1, 1, 1, 0.4f);
					}
					
					ref.draw.drawRectangle(draw_x, draw_y, tab_w, tab_w, 0, 0, 0, game_constants.layer5_underHUD);
					
					
					
					
					// draw black inner rectangle for tab
					if (i == tab)
						ref.draw.setDrawColor(0, 0, 0, 1);
					else
						ref.draw.setDrawColor(0f, 0.1f, 0.1f, 1);
					ref.draw.drawRectangle(
							draw_x,
							draw_y,
							tab_w - mgr.menuDifficulty.button_border_size,
							black_h, 
							0,
							black_offset,
							0,
							game_constants.layer5_underHUD
					);
					
					// draw difficulty letter on tab
					float dx=0,dy=0;
					ref.draw.setDrawColor(1, 1, 1, 1);
					switch(i) {
						case 0:
							ref.draw.text.append("E");
							break;
						case 1:
							ref.draw.text.append("M");
							break;
						case 2:
							ref.draw.text.append("H");
							break;
						case 3:
							ref.draw.text.append("H");
							ref.draw.setDrawColor(1, 0, 0, 1);
							
							float shake_range = mgr.gameMain.text_size/20;
							dx = ref.main.randomRange(-shake_range, shake_range);
							dy = ref.main.randomRange(-shake_range, shake_range);
							
							break;
					}
					ref.draw.drawText(draw_x + dx, draw_y + dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer5_underHUD, game_textures.TEX_FONT1);
					
				}
				
				
				float w,h;
				w = ref.screen_width - bluex2 + offset;
				h = mgr.menuDifficulty.button_border_size/2;
						
				//draw horizontal blue rectangles
				ref.draw.setDrawColor(0, 1, 1, 0.9f);
				ref.draw.drawRectangle(bluex2, ref.screen_height - tab_w, w, h,  -w/2, h/2, 0, game_constants.layer5_underHUD);
				w = bluex;
				ref.draw.drawRectangle(0, ref.screen_height - tab_w, w, h,  -w/2, h/2, 0, game_constants.layer5_underHUD);
				
				// draw black background for content
				tab_base_y = ref.screen_height - tab_w - mgr.menuDifficulty.button_border_size/2;
				ref.draw.setDrawColor(0, 0, 0, 1);
				ref.draw.drawRectangle(
						ref.screen_width/2 + offset,
						tab_base_y/2,
						ref.screen_width,
						tab_base_y,
						0,
						0,
						0,
						game_constants.layer5_underHUD
				);
				
				// Update vars for fade in/out effect
				if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_main) {
					leave();
				}
				
		}
	}

	
	private int room_to_leave_to = -1;
	public final int PREP_menuTop = 0;
	
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
				mgr.menuMain.start();
				break;
			}
	}
	
	public void start() {
		ref.main.unPauseEntities();
		mgr.gameMain.floor_height_target = 0;
		fade_main = false;
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(game_rooms.ROOM_MENURECORDS);
	}
	
	
}
