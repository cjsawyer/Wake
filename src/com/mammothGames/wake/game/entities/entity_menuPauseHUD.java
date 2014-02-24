package com.mammothGames.wake.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_sounds;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuPauseHUD extends engine_entity {

	masterGameReference mgr;
	public entity_menuPauseHUD(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		this.mgr = mgr;
	}
	
	private boolean game_paused, gameMuted, menu_open;
	private float text_x, text_y, button_size;
	float base_hud_height;
	private float pause_menu_y, pause_menu_y_target;
	float HUD_y;
	float HUD_y_target;
	public float streak_bar_alpha=0;

	public void restart() {
		game_paused = false;
		streak_width = 0;
		
		HUD_y = base_hud_height*2;
		HUD_y_target = 0;
	}
	
	@Override
	public void sys_firstStep() {
		
		pause_menu_y = 0;
		menu_open = false;
				
		button_size = mgr.gameMain.text_size;
		base_hud_height = button_size*2;
		
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
		
		text_x = ref.screen_width/2;
		text_y = ref.screen_height - mgr.gameMain.text_size/2;
		
		streak_bar_padding = mgr.gameMain.text_size/4;
		//TODO: uncomment this back to play music.
		//ref.sound.setMusicState(gameMuted, true, true);
	}
	
	private float streak_width=0;
	public float streak_bar_padding;
	
	@Override
	public void sys_step() {
		if ( (ref.room.get_current_room() == game_rooms.ROOM_GAME) ||  (ref.room.get_current_room() == game_rooms.ROOM_POSTGAME) ){
			
			// The 'if' is so the menu doesn't slide up immediately in the post-game screen
			if (mgr.gameMain.shade_alpha > 0.98f)
				HUD_y += (HUD_y_target - HUD_y) * 5 * ref.main.time_scale;
			
			streak_bar_alpha += (-streak_bar_alpha) * 5 * ref.main.time_scale;
//			if (streak_bar_alpha < 0)
//				streak_bar_alpha = 0;
			
			if (menu_open)
				pause_menu_y_target = ref.screen_height - 2*button_size; //ref.screen_height/8*((float)Math.sin((float)(SystemClock.uptimeMillis() * 180f * mgr.menuMain.DEG_TO_RAD / 667f))) - ref.screen_height/16 + button_size*4;
			else
				pause_menu_y_target = 0;
			
			pause_menu_y += (pause_menu_y_target - pause_menu_y) * 7 * ref.main.time_scale;
			if (Math.abs(pause_menu_y-pause_menu_y_target) < 2) {
				pause_menu_y = pause_menu_y_target;
			}
			
			
				
			ref.draw.setDrawColor(0, 1, 1, 0.3f);
			
			float rectangle_height = pause_menu_y;
			
			//The top rectangle hud
			if (!game_paused)
				ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-base_hud_height/2 + HUD_y, ref.screen_width, base_hud_height, 0, 0, 0, game_constants.layer5_underHUD);
			
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-rectangle_height/2 - base_hud_height + HUD_y, ref.screen_width, rectangle_height, 0, 0, 0, game_constants.layer5_underHUD);
			
			
			//Draw inner hud rectangle
			float small_rec_width = ref.screen_width - button_size*4;
			float small_rec_height = base_hud_height * 2f/3;
	//		ref.draw.setDrawColor(0, 0.1f, 0.1f, 1);//0.5
			
			// Draw progress bar for streak in smaller hud bar. 
//			if (mgr.gameMain.streak!=0) {
				float current_progress = mgr.gameMain.streak/mgr.gameMain.STREAK_PER_LEVEL;
				
				float extra_x = 0;
				float extra_y = 0;
				
				float streak_bar_height = small_rec_height - streak_bar_padding;
				if (current_progress >= 3) {
					// if we're at max streak of 4
					extra_x = streak_bar_alpha * streak_bar_padding/2;
					extra_y = streak_bar_alpha * (small_rec_height - streak_bar_height)/2 ;
				}
				
				//Draw back border rectangle for streak bar
				ref.draw.setDrawColor(0, 0, 0, 1);//0.5
				ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-button_size + HUD_y, small_rec_width + extra_x, small_rec_height + extra_y, 0, 0, 0, game_constants.layer6_HUD);
				
				
				current_progress = (current_progress < 3) ? mgr.gameMain.streak%mgr.gameMain.STREAK_PER_LEVEL : mgr.gameMain.STREAK_PER_LEVEL;
				
				float percent_bar = (current_progress)/mgr.gameMain.STREAK_PER_LEVEL;
				
				
				// Draw streak bar
				ref.draw.setDrawColor(0, 1, 1, 0.3f + streak_bar_alpha);//0.5
				float full_streak_width = (small_rec_width-streak_bar_padding);
				float target_streak_width = full_streak_width * percent_bar;
				streak_width += (target_streak_width - streak_width) * ref.screen_width/(mgr.gameMain.STREAK_PER_LEVEL*4) * ref.main.time_scale;
				ref.draw.drawRectangle(ref.screen_width/2-full_streak_width/2, ref.screen_height-button_size + HUD_y, streak_width + extra_x, streak_bar_height + extra_y, -streak_width/2, 0, 0, game_constants.layer6_HUD);
//			}
	
			float menu_openess_ratio = (pause_menu_y + 2*button_size)/ref.screen_height;
			
			// Pause text, above and off the screen when not paused.
			ref.draw.setDrawColor(1, 1, 1, menu_openess_ratio);
			ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height*3/2 - pause_menu_y - 2*button_size + HUD_y, mgr.gameMain.mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_BOTTOM, game_constants.layer5_underHUD, "paused", game_textures.TEX_FONT1);
			ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height*3/2 - pause_menu_y - 2*button_size + HUD_y, mgr.gameMain.mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer5_underHUD, "press back to quit", game_textures.TEX_FONT1);
			
			if (game_paused)
				ref.draw.drawCapturedDraw();

			float pause_alpha;
			if (game_paused) {
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
			ref.draw.drawText(text_x, text_y + HUD_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);

			//Draw score multiplier
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  "+"   );
			ref.strings.builder.append(  mgr.gameMain.score_multiplier   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(text_x + small_rec_width/2 - mgr.gameMain.text_size/4 , text_y + HUD_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
			
			
			ref.draw.setDrawColor(0, 1, 0, 1-pause_alpha);
			ref.draw.drawTextSingleString(text_x, text_y + HUD_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, game_constants.layer7_overHUD, "paused", game_textures.TEX_FONT1);
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.drawTexture(ref.screen_width - button_size/2, ref.screen_height - button_size/2 + HUD_y, button_size, button_size, button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_PAUSE, game_textures.TEX_SPRITES);
			

			// Unpause if the screen is touched
			if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
				
				
				if (game_paused) {
					// unpause if we touch the middle 1/3 of the screen
					if ( (ref.input.get_touch_y(0) > ref.screen_height/3) && (ref.input.get_touch_y(0) < ref.screen_height/3 * 2) ) {
						if (!mgr.areYouSure.getPopupState())
							switchPause();
					}
				}
				
				//right corner
				if ( (ref.input.get_touch_x(0) >= ref.screen_width - button_size*3/2) && (ref.input.get_touch_y(0) >= ref.screen_height - button_size*3/2)  ) {
					switchPause();
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
				ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2 + HUD_y, button_size, button_size, -button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_MUTED, game_textures.TEX_SPRITES);
			} else {
				ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2 + HUD_y, button_size, button_size, -button_size/2, button_size/2, 0, game_constants.layer6_HUD, game_textures.SUB_MUTE, game_textures.TEX_SPRITES);
			}
		}

		
	}
	
	@Override
	public void onRoomLoad() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME){
			mgr.gameMain.shade_alpha = 1;
			mgr.gameMain.shade_alpha_target = 1;
		}
	}
	
	public void setPause(boolean pause) {
		if ( (Math.abs(pause_menu_y-pause_menu_y_target) < 2) && (Math.abs(HUD_y-HUD_y_target) < 2) ) {
			
			// Snap the menu down so there's no gap if we happen to be in the bottom 2 pixels of the slide
			pause_menu_y = pause_menu_y_target;
			
			if(pause) {
				game_paused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
				menu_open = true;
			} else {
				game_paused = false;
				ref.main.unPauseEntities();
				menu_open = false;
			}
		}
	}
	public void switchPause() {
		setPause(!game_paused);
	}
	
	public boolean getPause() {
		return game_paused;
	}
	
	
	@Override
	public void onScreenSleep() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			if (!game_paused) {
				// if not paused, pause
				game_paused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
			}
		}
	}
}
