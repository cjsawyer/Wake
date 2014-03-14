package com.mammothGames.wake.game.entities;

import android.util.Log;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_areYouSure extends engine_entity {

	private boolean popup_open = false;
	private float popup_alpha = 0, popup_alpha_target = 0;
	
	masterGameReference mgr;
	public entity_areYouSure(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;

		this.mgr = mgr;
	}

	
	
	public final int STATE_ABANDON = 0;
	public final int STATE_QUIT = 1;
	private final String[] state_text = {
		"Abandon game?",
		"Quit game?"
	};
	public void buttonAction(boolean yes_or_no_action) {
		switch (action) {
		
			case STATE_ABANDON:
				if (yes_or_no_action) {
					setPopupOpenness(false);
					mgr.menuPauseHUD.setPause(false);
					mgr.gameMain.endGame();
					mgr.gameMain.floor_height_target = 0;
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_QUIT:
				if (yes_or_no_action) {
					ref.main.exitApp();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
		}
	}
	
	
	
	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {
		
		popup_alpha += (popup_alpha_target - popup_alpha) * 8f * ref.main.time_scale;
		
		float border_draw_width = ref.screen_width*4/5; 
		float draw_width = border_draw_width - mgr.menuDifficulty.button_border_size;
		float draw_x = ref.screen_width/2;
		float draw_y = ref.screen_height/2;
		
		float black_height = draw_width*3/4 - mgr.menuDifficulty.button_border_size/2;
		float black_y = draw_y + draw_width/8 + mgr.menuDifficulty.button_border_size/4;
		
		// relative to the above vars
		float button_width = draw_width/2 - mgr.menuDifficulty.button_border_size/4;
		float button_height = draw_width/4;
		float button_x = draw_width/4 + mgr.menuDifficulty.button_border_size/8;
		float button_y = -button_height - button_height/2; // Center of 4th region in the draw area
		
		// gray background
		ref.draw.setDrawColor(0,0,0, 0.7f * popup_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, game_constants.layer7_overHUD);
		
		// Blue border rectangle
		ref.draw.setDrawColor(0, 1, 1, 0.3f * popup_alpha);
		ref.draw.drawRectangle(draw_x, draw_y, border_draw_width, border_draw_width, 0, 0, 0, game_constants.layer7_overHUD);
		
		// Black inner rectangle
		ref.draw.setDrawColor(0, 0, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x, black_y, draw_width, black_height, 0, 0, 0, game_constants.layer7_overHUD);
		
		// Yes Button rectangle
		ref.draw.setDrawColor(0, 0.4f, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x-button_x, draw_y+button_y, button_width, button_height, 0, 0, 0, game_constants.layer7_overHUD);
		
		// No button rectangle
		ref.draw.setDrawColor(0.4f, 0, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x+button_x, draw_y+button_y, button_width, button_height, 0, 0, 0, game_constants.layer7_overHUD);
		
		// Popup text
		ref.draw.setDrawColor(1, 1, 1, 0.8f * popup_alpha);
		ref.draw.text.append(state_text[action]);
		ref.draw.drawText(draw_x, black_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer7_overHUD, game_textures.TEX_FONT1);
		
		
		ref.draw.text.append("YES");
		ref.draw.drawText(draw_x-button_x, draw_y+button_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer7_overHUD, game_textures.TEX_FONT1);
		
		ref.draw.text.append("NO");
		ref.draw.drawText(draw_x+button_x, draw_y+button_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer7_overHUD, game_textures.TEX_FONT1);
		
		if(popup_open) {
			
			
			if (popup_alpha > 0.8f)
				if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
					if (ref.collision.point_AABB(button_width, button_height, draw_x-button_x, draw_y+button_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
						buttonAction(true);
					} else if (ref.collision.point_AABB(button_width, button_height, draw_x+button_x, draw_y+button_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
						buttonAction(false);
					}
				}
		}
		
	}
	
	@Override
	public void onScreenSleep() {
		setPopupOpennessHard(false);
	}
	
	private int action = 0;
	private String text = "";

	public void setPopupAction(int action) {
		this.action = action;
	}
	
	public void setPopupOpenness(boolean open) {
		if (Math.abs(popup_alpha-popup_alpha_target) < 0.2f) {
			popup_open = open;
			popup_alpha = popup_alpha_target;
			popup_alpha_target = open ? 1 : 0;
		}
	}
	public void setPopupOpennessHard(boolean open) {
		popup_open = open;
		popup_alpha_target = open ? 1 : 0;
		popup_alpha = popup_alpha_target;
		
	}
	public boolean getPopupOpenness() {
		return popup_open;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}