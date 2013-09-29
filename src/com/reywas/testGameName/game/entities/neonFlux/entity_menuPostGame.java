package com.reywas.testGameName.game.entities.neonFlux;

import android.os.SystemClock;

import com.reywas.testGameName.game.game_rooms;
import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;


public class entity_menuPostGame extends engine_entity {

	masterGameReference mgr;
	public entity_menuPostGame(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		
		this.mgr = mgr;
	}
	
	private float screen_width, screen_height;
	
	@Override
	public void sys_firstStep() {
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
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
			ref.draw.drawRectangle(0, 0, ref.main.get_screen_width(), ref.main.get_screen_height(), ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, 0, 100);

			if (shade_alpha >= 1) {
				if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
					ref.room.changeRoom(game_rooms.ROOM_MENU);
				}
			}
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.drawText(screen_width/2, screen_height/2 + mgr.gameMain.text_size*3/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 101, "SCORE", game_textures.TEX_FONT1);
			
			ref.draw.drawText(screen_width/2, screen_height/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 101, "" + mgr.gameMain.score, game_textures.TEX_FONT1);
			
			if (mgr.gameMain.new_high_score) {
				ref.draw.setDrawColor(0.5f, 0.5f, 1, ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.gameMenu.DEG_TO_RAD / 500f * 180f))) + 1);
				ref.draw.drawText(screen_width/2, screen_height/2 - mgr.gameMain.text_size*3/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 101, "NEW BEST!", game_textures.TEX_FONT1);
			}
			
		}
	}
}
