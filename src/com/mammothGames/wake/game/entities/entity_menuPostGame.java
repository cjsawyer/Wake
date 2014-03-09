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
	
	boolean fade_out;
	
	@Override
	public void sys_firstStep() {
	}
	
	public void restart() {
	}
	
	@Override
	public void sys_step() {
		
		// black fade overlay
//		ref.draw.setDrawColor(0, 0, 0, mgr.gameMain.shade_alpha);
//		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, game_constants.layer6_HUD);
		
		if (ref.room.get_current_room() == game_rooms.ROOM_POSTGAME) {
			
			
			if ( (mgr.gameMain.shade_alpha > 0.98f) && (mgr.menuPauseHUD.HUD_y-mgr.menuPauseHUD.HUD_y_target<2) ) {
				if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
					prepLeave();
				}
			}

			// Done fading out, so go to the main menu
			if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_out ) {
				leave();
			}
			
			
		
//			if (mgr.gameMain.new_high_score) {
//				ref.draw.setDrawColor(0.5f, 0.5f, 1, ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.menuFirst.DEG_TO_RAD / 500f * 180f))) + 1);
//				ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height/2 - mgr.gameMain.text_size*3/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "NEW BEST!", game_textures.TEX_FONT1);
//			}
			
			
			
			
			
			float box_width = ref.screen_width - mgr.gameMain.padding_x;
			float box_height = ref.screen_height - mgr.gameMain.padding_y;
			
			float box_inner_padding = mgr.gameMain.padding_y/6;
			float box_top_y = ref.screen_height -mgr.gameMain.padding_y/2;
			float box_partition = (box_height - 2*box_inner_padding)/3;
			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha * (1-(mgr.menuPauseHUD.HUD_y_target-mgr.menuPauseHUD.HUD_y)/mgr.menuPauseHUD.HUD_y_target) ); // to match the sliding menu
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  "DIFFF"   );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(ref.screen_width/2,  (ref.screen_height+box_top_y)/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD,  ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, box_width, box_height, 0, 0, 0, game_constants.layer6_HUD);
			
			float draw_y;
			draw_y = box_top_y;
			
//			
			ref.draw.setDrawColor(1, 0, 0, mgr.gameMain.shade_alpha/2);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y, box_width, box_partition, 0, box_partition/2, 0, game_constants.layer6_HUD);
			draw_y -= box_partition+box_inner_padding;
			
			ref.draw.setDrawColor(0, 1, 0, mgr.gameMain.shade_alpha/2);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y, box_width, box_partition, 0, box_partition/2, 0, game_constants.layer6_HUD);
			draw_y -= box_partition+box_inner_padding;
			
			ref.draw.setDrawColor(0, 0, 1, mgr.gameMain.shade_alpha/2);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y, box_width, box_partition, 0, box_partition/2, 0, game_constants.layer6_HUD);
			
			
		}
	}
	
	public void start() {
		mgr.gameMain.shade_alpha_target = 1;
		fade_out = false;
		ref.room.changeRoom(game_rooms.ROOM_POSTGAME);
		
	}
	
	public void prepLeave() {
		mgr.gameMain.shade_alpha_target = 0;
		fade_out = true;
	}
	
	private void leave() {
		mgr.menuMain.start();
	}
	
}
