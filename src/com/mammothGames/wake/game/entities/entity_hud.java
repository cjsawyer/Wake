package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_hud extends engine_entity {

	masterGameReference mgr;
	
	public entity_hud(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = true;
	}
	
	private float screen_width, screen_height, text_x, text_y, text_size, shake_x, shake_y, shake_range_x, shake_range_y, shake_snap_back_time;
	private int score, old_score;
	private boolean shake;
	@Override
	public void sys_firstStep() {
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
		
		
		text_size = mgr.gameMain.text_size;
		text_x = screen_width/2;
		text_y = screen_height - text_size/2;
		
		score = -1;
		old_score = score;
		
		shake = false;
		shake_range_x = screen_width/10;
		shake_range_y = text_size;
		shake_snap_back_time = shake_range_x/2;
		
	}
	
	
	@Override
	public void sys_step() {
		
		shake = false;
		score = mgr.gameMain.score;
		if (old_score != score) {
			shake = true;
		}
		old_score = score;
		
		// Shaky text. Just turning it off for now :)
//		if (shake) {
//			shake_x = ref.main.randomRange(-shake_range_x, shake_range_x) + text_x;
//			shake_y = ref.main.randomRange(-shake_range_y, shake_range_y) + text_y;
//		}
//		shake_x += (text_x - shake_x) * shake_snap_back_time * ref.main.time_scale;
//		shake_y += (text_y - shake_y) * shake_snap_back_time * ref.main.time_scale;
		shake_x = text_x;
		shake_y = text_y;
		
		
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME) {
			ref.draw.setDrawColor(1, 1, 1, 1);
			
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  score   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(shake_x, shake_y, text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, 0, 200,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
			//text_x + shake_x, text_y + shake_y,
		}
	}
}
