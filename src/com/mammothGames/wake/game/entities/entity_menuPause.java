package com.mammothGames.wake.game.entities;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_sounds;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuPause extends engine_entity {

	masterGameReference mgr;
	public entity_menuPause(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		this.mgr = mgr;
	}
	
	private boolean gamePaused, gameMuted, menu_open;
	
	private float text_x, text_y, text_size, button_size, menu_y, menu_target_y;

	public void restart() {
		gamePaused = false;
		streak_width = 0;
	}
	
	@Override
	public void sys_firstStep() {
		
		menu_y = 0;
		menu_open = false;
				
		button_size = mgr.gameMain.text_size;
		
		String muted = ref.file.load("muted");
		// If first boot, save "not muted"
		if (muted.equals("")) {
			gameMuted = false;
			ref.file.save("muted", "" + gameMuted);
		} else {
			gameMuted = Boolean.parseBoolean(muted);
		}
		ref.sound.setMusicState(gameMuted, false, false);
		
		if (!gameMuted) {
			ref.sound.playSound(game_sounds.SND_SPLASH);
		}
		
		text_size = mgr.gameMain.text_size;
		text_x = ref.screen_width/2;
		text_y = ref.screen_height - text_size/2;
		
		
		//TODO: uncomment this back to play music.
		//ref.sound.setMusicState(gameMuted, true, true);
	}
	
	private float streak_width=0;
	
	@Override
	public void sys_step() {
		
		if (menu_open)
			menu_target_y = ref.screen_height - 2*button_size; //ref.screen_height/8*((float)Math.sin((float)(SystemClock.uptimeMillis() * 180f * mgr.menuMain.DEG_TO_RAD / 667f))) - ref.screen_height/16 + button_size*4;
		else
			menu_target_y = 0;
		
		menu_y += (menu_target_y - menu_y) * 7 * ref.main.time_scale;
		if (Math.abs(menu_y-menu_target_y) < 2) {
			menu_y = menu_target_y;
		}
		
		
			
		ref.draw.setDrawColor(0, 1, 1, 0.3f);//0.3
		
		float base_hud_height = button_size*2;
		float rectangle_height = menu_y;
		
		//The top rectangle hud
		if (!gamePaused)
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-base_hud_height/2, ref.screen_width, base_hud_height, 0, 0, 0, game_constants.layer5_underHUD);
		
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-rectangle_height/2 - base_hud_height, ref.screen_width, rectangle_height, 0, 0, 0, game_constants.layer5_underHUD);
		
		
		//Draw inner hud rectangle
		float small_rec_width = ref.screen_width - button_size*4;
//		ref.draw.setDrawColor(0, 0.1f, 0.1f, 1);//0.5
		ref.draw.setDrawColor(0, 0, 0, 1);//0.5
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-button_size, small_rec_width, base_hud_height * 2f/3, 0, 0, 0, game_constants.layer6_HUD);
		
		// Draw progress bar for streak in smaller hud bar. 
		if (mgr.gameMain.streak!=0) {
			float current_progress = mgr.gameMain.streak/mgr.gameMain.STREAK_PER_LEVEL;
			current_progress = (current_progress < 3) ? mgr.gameMain.streak%mgr.gameMain.STREAK_PER_LEVEL : mgr.gameMain.STREAK_PER_LEVEL;
			
			float percent_bar = (current_progress)/mgr.gameMain.STREAK_PER_LEVEL;
			
			ref.draw.setDrawColor(0, 1, 1, 0.3f);//0.5
			float full_streak_width = (small_rec_width-text_size/4);
			float target_streak_width = full_streak_width * percent_bar;
			streak_width += (target_streak_width - streak_width) * ref.screen_width/(mgr.gameMain.STREAK_PER_LEVEL*4) * ref.main.time_scale;
			ref.draw.drawRectangle(ref.screen_width/2-full_streak_width/2, ref.screen_height-button_size, streak_width, base_hud_height * 2f/3 - text_size/4, -streak_width/2, 0, 0, game_constants.layer6_HUD);
			
		}

		float menu_openess_ratio = (menu_y + 2*button_size)/ref.screen_height;
		
		// Pause text, above and off the screen when not paused.
		ref.draw.setDrawColor(1, 1, 1, menu_openess_ratio);
		ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height*3/2 - menu_y - 2*button_size, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_BOTTOM, game_constants.layer5_underHUD, "paused", game_textures.TEX_FONT1);
		ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height*3/2 - menu_y - 2*button_size, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer5_underHUD, "press back to quit", game_textures.TEX_FONT1);
		
		// Draw pause menu, if game is paused
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			if (gamePaused)
				ref.draw.drawCapturedDraw();

			float pause_alpha;
			if (gamePaused) {
				pause_alpha = ((float)Math.sin((float)(SystemClock.uptimeMillis() * 180f * mgr.menuMain.DEG_TO_RAD / 1666f)));
				pause_alpha *= menu_openess_ratio; // always fade in even if we catch the clock at a bad time
			} else {
				pause_alpha = 1;
			}
			
			//Draw score
			ref.draw.setDrawColor(1, 1, 1, pause_alpha);
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  mgr.gameMain.score   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(text_x, text_y, text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);

			//Draw score multiplier
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  "+"   );
			ref.strings.builder.append(  mgr.gameMain.score_multiplier   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(text_x + small_rec_width/2 - text_size/4 , text_y, text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
			
			
			ref.draw.setDrawColor(0, 1, 0, 1-pause_alpha);
			ref.draw.drawTextSingleString(text_x, text_y, text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD, "paused", game_textures.TEX_FONT1);
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.drawTexture(ref.screen_width - button_size/2, ref.screen_height - button_size/2, button_size, button_size, button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_PAUSE, game_textures.TEX_SPRITES);
			

			// Unpause if the screen is touched
			if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
				
				
				if (gamePaused) {
					if ( (ref.input.get_touch_y(0) > ref.screen_height/3) && (ref.input.get_touch_y(0) < ref.screen_height/3 * 2) ) {
						switchPause();
					}
				}
				
				//right corner
				if ( (ref.input.get_touch_x(0) >= ref.screen_width - button_size*3/2) && (ref.input.get_touch_y(0) >= ref.screen_height - button_size*3/2)  ) {
					switchPause();
				}
				
				
				
			}
			
		}

		// mute/unmute button
		if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
			// Left corner
			if (  (ref.input.get_touch_x(0) <= button_size*3/2) && (ref.input.get_touch_y(0) >= ref.screen_height - button_size*3/2)  ) {
				gameMuted = !gameMuted;
				ref.file.save("muted", "" + gameMuted);
				// Play music.
					//TODO: uncomment this back to play music.
					// ref.sound.setMusicState(gameMuted, true, true);
				ref.sound.setMusicState(gameMuted, false, false);
			}
		}
		ref.draw.setDrawColor(1, 1, 1, 1);
		if (gameMuted) {
			ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2, button_size, button_size, -button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_MUTED, game_textures.TEX_SPRITES);
		} else {
			ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2, button_size, button_size, -button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_MUTE, game_textures.TEX_SPRITES);
		}
		
	}
	
	private void switchPause() {
		
		// If the menu isn't still sliding
		if (Math.abs(menu_y-menu_target_y) < 2) {
			
			if (!gamePaused) {
				gamePaused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
			} else {
				gamePaused = false;
				ref.main.unPauseEntities();
			}
			menu_open = !menu_open;
			
		}
	}
	
	@Override
	public void backButton() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			
			if(gamePaused) {
				switchPause();
			} else {
				menu_open = false;
				ref.main.pauseEntities();
				ref.room.changeRoom(game_rooms.ROOM_MENU);
			}
		}
	}
	
	@Override
	public void onScreenSleep() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			if (!gamePaused) {
				// if not paused, pause
				gamePaused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
			}
		}
	}
}
