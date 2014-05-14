package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


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
		
		if (ref.room.get_current_room() == rooms.ROOM_POSTGAME) {
			
			// Done fading out, so go to the main menu
			if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_out ) {
				leave();
			}
		
			
			float box_width = ref.screen_width - mgr.gameMain.padding_x;
			float box_height = ref.screen_height - mgr.gameMain.padding_y;
			
			float box_inner_padding = mgr.gameMain.padding_y/9;
			float box_top_y = ref.screen_height -mgr.gameMain.padding_y/2;
			float box_partition = mgr.gameMain.text_size*4;//(box_height - 3*box_inner_padding)/4;
			
			// The text at the top gets it's own alpha so it doesn't clash with the sliding HUD
			float diff_text_alpha = mgr.gameMain.shade_alpha * (1-(mgr.menuPauseHUD.HUD_y_target-mgr.menuPauseHUD.HUD_y)/mgr.menuPauseHUD.HUD_y_target);
			float blink_alpha = ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.menuFirst.DEG_TO_RAD / 500f * 180f))) + 1;
			blink_alpha = blink_alpha > 1 ? 1:blink_alpha;
			blink_alpha *= mgr.gameMain.shade_alpha;
			
			float dx=0;
			float dy=0;
			if(mgr.gameMain.current_diff==mgr.gameMain.DIF_HELL) {
				// Do the shaky text for hell mode
				float shake_range = mgr.gameMain.text_size/20;
				dx = ref.main.randomRange(-shake_range, shake_range);
				dy = ref.main.randomRange(-shake_range, shake_range);
				ref.draw.setDrawColor(1, 0, 0, diff_text_alpha);
				ref.draw.text.append("HELL");
			} else {
				// Normal text for the other modes
				ref.draw.setDrawColor(1, 1, 1, diff_text_alpha);
				ref.draw.text.append(mgr.gameMain.current_diff_string);	
			}
			
			ref.draw.drawText(ref.screen_width/2 + dx,  (ref.screen_height+box_top_y)/2 + dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			
//			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
//			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, box_width, box_height, 0, 0, 0, game_constants.layer6_HUD);
			
			float draw_y, text_y, text_h;
			draw_y = box_top_y;
			text_h = mgr.menuDifficulty.draw_width/2-mgr.menuDifficulty.button_border_size;
			
			//Gray rectangle behind text.
			ref.draw.setDrawColor(1, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
//			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_partition - box_inner_padding/2, mgr.menuDifficulty.draw_width, box_partition*2 + box_inner_padding , 0, 0, 0, game_constants.layer6_HUD);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_inner_padding/2 - (box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size)/2, mgr.menuDifficulty.draw_width, box_partition + mgr.menuDifficulty.draw_height + box_inner_padding , 0, 0, 0, constants.layer6_HUD);
			// darker gray inner box
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_inner_padding/2 - (box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size)/2, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);
			
//																																							draw_y -= box_partition+box_inner_padding;
//																																							draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;
			//vvvvvv
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			text_y = draw_y - box_partition/2 + (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Score");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.score);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			////////
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			text_y = draw_y - box_partition/2 - (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Streak");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.best_points_streak_this_game);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			//^^^^^^
			
//			draw_y -= 2*mgr.gameMain.text_size+box_inner_padding;
//			draw_y -= box_partition+box_inner_padding;
			draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;
			
			//vvvvvv
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			if (mgr.gameMain.new_high_score)
				ref.draw.setDrawColor(0, 1, 1, blink_alpha);
			
			text_y = draw_y - box_partition/2 + (box_inner_padding+mgr.gameMain.text_size)/2;
			
			
			ref.draw.text.append("High Score");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.high_score);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			////////
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			if (mgr.gameMain.new_best_streak)
				ref.draw.setDrawColor(0, 1, 1, blink_alpha);
			
			text_y = draw_y - box_partition/2 - (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Best Streak");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.best_points_streak);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			//^^^^^^
			
			draw_y -=  mgr.menuDifficulty.draw_height/2+box_partition/2+box_inner_padding;
			
			//vvvvvv
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(ref.screen_width/2, draw_y-box_partition/2, mgr.menuDifficulty.draw_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y-box_partition/2, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("RECORDS");
			ref.draw.drawText(ref.screen_width/2, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB( mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, ref.screen_width/2, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_records);
				}
			//^^^^^^
			
			draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;//box_partition+box_inner_padding;
			
			//vvvvvv
			//Left, "AGAIN!" button
			float lr_width = ((mgr.menuDifficulty.draw_width-box_inner_padding)/2);
			float l_button_x = ref.screen_width/2 - (mgr.menuDifficulty.draw_width+box_inner_padding)/4;
			float r_button_x = ref.screen_width/2 + (mgr.menuDifficulty.draw_width+box_inner_padding)/4;
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(l_button_x, draw_y-box_partition/2, lr_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(l_button_x, draw_y-box_partition/2, lr_width - mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("AGAIN");
			ref.draw.drawText(l_button_x, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB(lr_width, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, l_button_x, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_game);
				}
			////////
			//Left, "QUIT" button
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(r_button_x, draw_y-box_partition/2, lr_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(r_button_x, draw_y-box_partition/2, lr_width - mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("QUIT");
			ref.draw.drawText(r_button_x, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB( lr_width, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, r_button_x, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_menu);
				}
			//^^^^^^
		}
	}
	
	private final int room_game=0, room_records=1, room_menu=2;
	private int room_to_leave_to;
	
	public void start() {
		mgr.gameMain.floor_height_target = 0;
		mgr.gameMain.shade_alpha_target = 1;
		fade_out = false;
		ref.room.changeRoom(rooms.ROOM_POSTGAME);
		
	}
	
	
	public void prepLeave(int room_to_leave_to) {
		mgr.gameMain.shade_alpha_target = 0;
		fade_out = true;
		
		this.room_to_leave_to = room_to_leave_to;  
	}
	
	private void leave() {
		
		switch (room_to_leave_to){
		case room_game:
			mgr.gameMain.startGame();
			break;
		case room_records:
			mgr.menuRecords.start(mgr.menuDifficulty.tab);
			break;
		case room_menu:
			mgr.menuMain.start();
			break;
		}
	
	}
	
}
