package com.mammothGames.wake.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuPostGame extends engine_entity {

	masterGameReference mgr;
	public entity_menuPostGame(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		
		this.mgr = mgr;
	}
	
	@Override
	public void sys_firstStep() {
	}
	
	float shade_alpha;
	public void restart() {
		shade_alpha = 0;
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == game_rooms.ROOM_POSTGAME) {
			
			ref.draw.drawCapturedDraw();
			
			shade_alpha += ref.main.time_delta/2000f;
			ref.draw.setDrawColor(0, 0, 0, shade_alpha);
			ref.draw.drawRectangle(0, 0, ref.screen_width, ref.screen_height, ref.screen_width/2, ref.screen_height/2, 0, game_constants.layer6_HUD);

			if (shade_alpha >= 1) {
				if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
					// Update the high-score string.
					ref.room.changeRoom(game_rooms.ROOM_MENU);
				}
			}
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2 + mgr.gameMain.text_size*3/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "SCORE", game_textures.TEX_FONT1);
			
			
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  mgr.gameMain.score   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(ref.screen_width/2, ref.screen_height/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, game_constants.layer6_HUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
			
			
			if (mgr.gameMain.new_high_score) {
				ref.draw.setDrawColor(0.5f, 0.5f, 1, ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.menuMain.DEG_TO_RAD / 500f * 180f))) + 1);
				ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2 - mgr.gameMain.text_size*3/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "NEW BEST!", game_textures.TEX_FONT1);
			}
			
		}
	}
}
