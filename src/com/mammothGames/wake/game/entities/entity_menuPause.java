package com.mammothGames.wake.game.entities;

import android.os.SystemClock;

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
	
	private boolean gamePaused, gameMuted;
	
	public void restart() {
		gamePaused = false;
	}
	
	float button_size;
	
	@Override
	public void sys_firstStep() {
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
		//TODO: uncomment this back to play music.
		//ref.sound.setMusicState(gameMuted, true, true);
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			
			// Draw pause menu, if game is paused
			if (gamePaused) {
				
				ref.draw.drawCapturedDraw();
				
				ref.draw.setDrawColor(1, 1, 1, 1);
				ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "tap to unpause", game_textures.TEX_FONT1);
				ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2 - (mgr.gameMain.text_size), mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "press back to quit", game_textures.TEX_FONT1);
			}

			
			if (gamePaused) {
				ref.draw.setDrawColor(1, 0, 0, ((float)Math.sin((float)(SystemClock.uptimeMillis() * 180f * mgr.menuMain.DEG_TO_RAD / 667f))) + 1);
			} else {
				ref.draw.setDrawColor(1, 1, 1, 1);
			}
			ref.draw.drawTexture(ref.screen_width - button_size/2, ref.screen_height - button_size/2, button_size, button_size, -button_size/2, -button_size/2, 0, 200, game_textures.SUB_PAUSE, game_textures.TEX_SPRITES);
			

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
			ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2, button_size, button_size, button_size/2, -button_size/2, 0, 200, game_textures.SUB_MUTED, game_textures.TEX_SPRITES);
		} else {
			ref.draw.drawTexture(button_size/2, ref.screen_height - button_size/2, button_size, button_size, button_size/2, -button_size/2, 0, 200, game_textures.SUB_MUTE, game_textures.TEX_SPRITES);
		}
		
	}
	
	private void switchPause() {
		if (!gamePaused) {
			gamePaused = true;
			ref.draw.captureDraw();
			ref.main.pauseEntities();
		} else {
			gamePaused = false;
			ref.main.unPauseEntities();
		}
	}
	
	@Override
	public void backButton() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			
			if (!gamePaused) {
				// if not paused, pause
				gamePaused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
			} else {
				// if paused, go back to menu
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
