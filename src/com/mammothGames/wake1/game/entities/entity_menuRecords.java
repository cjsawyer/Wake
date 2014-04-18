package com.mammothGames.wake1.game.entities;

import android.content.Intent;
import android.net.Uri;

import com.mammothGames.wake1.game.game_constants;
import com.mammothGames.wake1.game.game_rooms;
import com.mammothGames.wake1.game.game_textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_menuRecords extends engine_entity {

	private boolean fade_main;
	private float tab_w = 0, tab_h = 0;
	private final int tabs = 4;
	private int tab = 0;
	private int last_tab = -1;
	
	masterGameReference mgr;
	public entity_menuRecords(masterGameReference mgr) {
		this.mgr = mgr;
		persistent = true;
		pausable = false;
	}
	
	@Override
	public void sys_firstStep() {
		tab_w = ref.screen_width/4f;
		tab_h = ref.screen_width/5f;
	}
	
	float offset = 0;
	int high_score,high_streak, games_played;
	
	// c/p from gameMain
	final String SCO_E = "SCO_E";
	final String SCO_M = "SCO_M";
	final String SCO_H = "SCO_H";
	final String SCO_HE = "SCO_HE";
	
	final String STR_E = "STR_E";
	final String STR_M = "STR_M";
	final String STR_H = "STR_H";
	final String STR_HE = "STR_HE";
	
	final String PLY_E = "PLY_E";
	final String PLY_M = "PLY_M";
	final String PLY_H = "PLY_H";
	final String PLY_HE = "PLY_HE";
	
	private String STR, SCO, PLY;
	
	
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
					float draw_y = ref.screen_height - tab_h/2;
					
					if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN)
						if (ref.collision.point_AABB(tab_w, tab_h, draw_x, draw_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
							last_tab = tab;
							tab = i;
							if (last_tab != tab) {
								loadScores();
							}
							
						}
					
					// Set color for background part of tab
					if (i == tab) {
						ref.draw.setDrawColor(0, 1, 1, 0.9f * mgr.gameMain.shade_alpha);
						black_h = tab_h;
						black_offset = mgr.menuDifficulty.button_border_size/2;
						bluex = draw_x - tab_w/2 + mgr.menuDifficulty.button_border_size/2;
						bluex2 = draw_x + tab_w/2 - mgr.menuDifficulty.button_border_size/2;
					} else {
						ref.draw.setDrawColor(1, 1, 1, 0.4f * mgr.gameMain.shade_alpha);
					}
					
					ref.draw.drawRectangle(draw_x, draw_y, tab_w, tab_h, 0, 0, 0, game_constants.layer5_underHUD);
					
					
					
					
					// draw black inner rectangle for tab
					if (i == tab)
						ref.draw.setDrawColor(0, 0, 0, 1 * mgr.gameMain.shade_alpha);
					else
						ref.draw.setDrawColor(0f, 0.1f, 0.1f, 1 * mgr.gameMain.shade_alpha);
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
					ref.draw.setDrawColor(1, 1, 1, 1 * mgr.gameMain.shade_alpha);
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
							ref.draw.setDrawColor(1, 0, 0, 1 * mgr.gameMain.shade_alpha);
							
//							float shake_range = mgr.gameMain.text_size/20;
//							dx = ref.main.randomRange(-shake_range, shake_range);
//							dy = ref.main.randomRange(-shake_range, shake_range);
							
							break;
					}
					ref.draw.drawText(draw_x + dx, draw_y + dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer5_underHUD, game_textures.TEX_FONT1);
					
				}
				
				
				float w,h;
				w = ref.screen_width - bluex2 + offset;
				h = mgr.menuDifficulty.button_border_size/2;
						
				//draw horizontal blue rectangles
				ref.draw.setDrawColor(0, 1, 1, 0.9f * mgr.gameMain.shade_alpha);
				ref.draw.drawRectangle(bluex2, ref.screen_height - tab_h, w, h,  -w/2, h/2, 0, game_constants.layer5_underHUD);
				w = bluex;
				ref.draw.drawRectangle(0, ref.screen_height - tab_h, w, h,  -w/2, h/2, 0, game_constants.layer5_underHUD);
				
				// draw black background for content
				tab_base_y = ref.screen_height - tab_h - mgr.menuDifficulty.button_border_size/2;
				ref.draw.setDrawColor(0, 0, 0, 1 * mgr.gameMain.shade_alpha);
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
				
				//Draw scores
				float text_x = ref.screen_width/2 - mgr.menuDifficulty.button_border_size;
				float text_y = tab_base_y;
				
				
				text_y -= mgr.gameMain.text_size;
				ref.draw.setDrawColor(1, 1, 1, 1);
				float dx=0,dy=0;
				switch(tab) {
					case 0:
						ref.draw.text.append("EASY");
						break;
					case 1:
						ref.draw.text.append("MEDIUM");
						break;
					case 2:
						ref.draw.text.append("HARD");
						break;
					case 3:
						ref.draw.text.append("HELL");
						ref.draw.setDrawColor(1, 0, 0, 1 * mgr.gameMain.shade_alpha);
						
						float shake_range = mgr.gameMain.text_size/20;
						dx = ref.main.randomRange(-shake_range, shake_range);
						dy = ref.main.randomRange(-shake_range, shake_range);
					
					break;
				}
				
				ref.draw.drawText(ref.screen_width/2+dx + offset, text_y+dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				
				ref.draw.setDrawColor(1, 1, 1, 1 * mgr.gameMain.shade_alpha);
				text_y -= 1.5*mgr.gameMain.text_size;
				ref.draw.text.append("High Score");
				ref.draw.drawText(ref.screen_width/2 - text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				ref.draw.text.append(high_score);
				ref.draw.drawText(ref.screen_width/2 + text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				
				text_y -= 1.5*mgr.gameMain.text_size;
				ref.draw.text.append("BEST STREAK");
				ref.draw.drawText(ref.screen_width/2 - text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				ref.draw.text.append(high_streak);
				ref.draw.drawText(ref.screen_width/2 + text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				text_y -= 1.5*mgr.gameMain.text_size;
				ref.draw.text.append("GAMES PLAYED");
				ref.draw.drawText(ref.screen_width/2 - text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				ref.draw.text.append(games_played);
				ref.draw.drawText(ref.screen_width/2 + text_x + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				
				//RESET BUTTON
				text_y -= mgr.gameMain.text_size + mgr.menuDifficulty.draw_height/2;
				//draw back, blue rectangle
				ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
				ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width, mgr.menuDifficulty.draw_height, 0, 0, 0, game_constants.layer6_HUD);
				
				// draw inner black rectangle
				ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
				ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, game_constants.layer6_HUD);			
				
				ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
				ref.draw.text.append("ERASE ALL");
				ref.draw.drawText(ref.screen_width/2 + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) && (!mgr.areYouSure.getPopupOpenness()) )
					if (ref.collision.point_AABB( mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, ref.screen_width/2, text_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
						mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_ERASE);
						mgr.areYouSure.setPopupOpenness(true);
					}
				
				//DONATE LINK BUTTON
				if (!game_constants.pro) {
					text_y -= .5f*mgr.gameMain.text_size + mgr.menuDifficulty.draw_height;
					//draw back, blue rectangle
					ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
					ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width, mgr.menuDifficulty.draw_height, 0, 0, 0, game_constants.layer6_HUD);
					
					// draw inner black rectangle
					ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
					ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, game_constants.layer6_HUD);			
					
					ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
					ref.draw.text.append("AD FREE VERSION");
					ref.draw.drawText(ref.screen_width/2 + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
					
					if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) && (!mgr.areYouSure.getPopupOpenness()) )
						if (ref.collision.point_AABB( mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, ref.screen_width/2, text_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
							
							final String appPackageName = "com.mammothGames.wake1"; // getPackageName() from Context or Activity object
							try {
								ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
							} catch (android.content.ActivityNotFoundException anfe) {
								ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
							}
							
						}
				}
				
				//RATE LINK BUTTON
				text_y -= .5f*mgr.gameMain.text_size + mgr.menuDifficulty.draw_height;
				//draw back, blue rectangle
				ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
				ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width, mgr.menuDifficulty.draw_height, 0, 0, 0, game_constants.layer6_HUD);
				
				// draw inner black rectangle
				ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
				ref.draw.drawRectangle(ref.screen_width/2 + offset, text_y, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, game_constants.layer6_HUD);			
				
				ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
				ref.draw.text.append("RATE");
				ref.draw.drawText(ref.screen_width/2 + offset, text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, game_textures.TEX_FONT1);
				
				if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) && (!mgr.areYouSure.getPopupOpenness()) )
					if (ref.collision.point_AABB( mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, ref.screen_width/2, text_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
						
						final String appPackageName = ref.android.getPackageName(); // getPackageName() from Context or Activity object
						try {
							ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
						} catch (android.content.ActivityNotFoundException anfe) {
							ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
						}
						
					}
				
		}
	}
	
	public void loadScores() {
		// Load high scores
		
		switch(tab) {
			case 0:
				SCO  = SCO_E;
				STR  = STR_E;
				PLY = PLY_E;
				break;
			case 1:
				SCO  = SCO_M;
				STR  = STR_M;
				PLY = PLY_M;
				break;
			case 2:
				SCO  = SCO_H;
				STR  = STR_H;
				PLY = PLY_H;
				break;
			case 3:
				SCO  = SCO_HE;
				STR  = STR_HE;
				PLY = PLY_HE;
				break;
		}
		
		String high_score_string = ref.file.load(SCO);
		if (high_score_string.equals("")) {
			high_score = 0;
		} else {
			high_score = Integer.parseInt(ref.file.load(SCO)); 
		}
		String best_streak_string = ref.file.load(STR);
		if (best_streak_string.equals("")) {
			high_streak = 0;
		} else {
			high_streak = Integer.parseInt(ref.file.load(STR)); 
		}
		String games_played_string = ref.file.load(PLY);
		if (games_played_string.equals("")) {
			games_played = 0;
		} else {
			games_played = Integer.parseInt(ref.file.load(PLY)); 
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
	
	public void start(int tab) {
		this.tab = tab;
			
		ref.main.unPauseEntities();
		mgr.gameMain.floor_height_target = 0;
		fade_main = false;
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(game_rooms.ROOM_MENURECORDS);
		loadScores();
	}
	
	
}
