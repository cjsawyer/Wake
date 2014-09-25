package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.sounds;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_menuPauseHUD extends engine_entity {

	masterGameReference mgr;
	public entity_menuPauseHUD(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		this.mgr = mgr;
	}
	
	boolean game_paused;
	private float text_x, text_y, button_size;
	float base_hud_height;
//	float HUD_y;
//	float HUD_y_target;
	public float streak_bar_alpha=0;
	
	boolean leaving_post = false; // set in menuPostGame

	public void restart() {
		streak_width = 0;
		
		setTweenPosition(0);
		
		
//		HUD_y = base_hud_height*2;
//		HUD_y_target = 0;
	}
	
	float y2;
	
	@Override
	public void sys_firstStep() {
		
		button_size = mgr.gameMain.text_size;
		base_hud_height = button_size*2;
		
		text_x = ref.screen_width - button_size;
		text_y = ref.screen_height - mgr.gameMain.text_size/2;
		
		streak_bar_padding = mgr.gameMain.text_size/4;

		y = base_hud_height*2;
		oldy = y;
		ytarget = y;
		
		y2 = y;
		
		restart();
		
		//TODO: uncomment this back to play music.
		//ref.sound.setMusicState(gameMuted, true, true);
	}
	
	private float streak_width=0;
	public float streak_bar_padding;
	
	
	float y, ytarget, oldy, tween_start = 0;
	final float tween_time = 500;
	
	public void setTweenPosition(float yt) {
		ytarget = yt;
		oldy = y;
		tween_start = SystemClock.uptimeMillis();
	}
	/**
     * 
     * @param t current time
     * @param b start value
     * @param c change in value
     * @param d duration
     * @return
     */
    private float tween(float t, float b, float c, float d) {
    	//bouncy
    	t/=d;
    	float ts=t*t;
    	float tc=ts*t;
    	return b+c*(33*tc*ts + -106*ts*ts + 126*tc + -67*ts + 15*t);
	}
	
	@Override
	public void sys_step() {
		
		
		if ( (ref.room.get_current_room() == rooms.ROOM_GAME) ||  (ref.room.get_current_room() == rooms.ROOM_POSTGAME) ){
			
			if (ref.input.get_touch_state(3) == ref.input.TOUCH_DOWN) {
				setTweenPosition(y2);
				y2/=-1;
			}
			
			float current_time = SystemClock.uptimeMillis() - tween_start;
			y = tween(current_time, oldy, ytarget-oldy, tween_time);
			if (tween_time - current_time < 1) {
				y = ytarget;
			}
			
			streak_bar_alpha += (-streak_bar_alpha) * 5 * ref.main.time_scale;
			
			
				
			if (game_paused)
				ref.draw.drawCapturedDraw();
			
			
			//The top rectangle hud
			ref.draw.setDrawColor(0, 1, 1, 0.3f);
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-base_hud_height*1/4+y, ref.screen_width, base_hud_height*3/2f, 0, 0, 0, constants.layer5_underHUD, true);
			
//			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-rectangle_height/2 - base_hud_height + HUD_y, ref.screen_width, rectangle_height, 0, 0, 0, game_constants.layer5_underHUD);
			
			
			//Draw inner hud rectangle
			float small_rec_width = ref.screen_width - button_size*4;
			float small_rec_height = base_hud_height * 2f/3;

			
			// Draw progress bar for streak in smaller hud bar. 
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
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height-button_size + y, small_rec_width + extra_x, small_rec_height + extra_y, 0, 0, 0, constants.layer6_HUD, true);
			
			
			current_progress = (current_progress < 3) ? mgr.gameMain.streak%mgr.gameMain.STREAK_PER_LEVEL : mgr.gameMain.STREAK_PER_LEVEL;
			
			float percent_bar = (current_progress)/mgr.gameMain.STREAK_PER_LEVEL;
			
			
			// Draw streak bar
			ref.draw.setDrawColor(0, 1, 1, 0.3f + streak_bar_alpha);//0.5
			float full_streak_width = (small_rec_width-streak_bar_padding);
			float target_streak_width = full_streak_width * percent_bar;
			streak_width += (target_streak_width - streak_width) * ref.screen_width/(mgr.gameMain.STREAK_PER_LEVEL*4) * ref.main.time_scale;
			ref.draw.drawRectangle(ref.screen_width/2-full_streak_width/2, ref.screen_height-button_size + y, streak_width + extra_x, streak_bar_height + extra_y, -streak_width/2, 0, 0, constants.layer6_HUD, true);
	

			
			//Draw score
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.text.append(  mgr.gameMain.score  );
			ref.draw.drawText(ref.screen_width/2, text_y + y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, constants.layer7_overHUD, textures.TEX_FONT1, true);
			

			//Draw score multiplier
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.text.append(  "+"   );
			ref.draw.text.append(  mgr.gameMain.score_multiplier   );
			ref.draw.drawText(text_x, text_y + y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, constants.layer7_overHUD, textures.TEX_FONT1, true);
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			ref.draw.drawTexture(
			        button_size/2,
			        ref.screen_height - button_size/2 + y,
			        button_size,
			        button_size,
			        -button_size/2,
			        button_size/2,
			        0,
			        constants.layer6_HUD,
			        textures.SUB_PAUSE,
			        textures.TEX_SPRITES,
			        true);
			

			// Un-pause if the pause button is touched
			if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
				//right corner
				if (ref.room.get_current_room() == rooms.ROOM_GAME)
					if (  (ref.input.get_touch_x(0) <= button_size*2) && (ref.input.get_touch_y(0) >= ref.screen_height - button_size*3/2)  ) {
						if (!mgr.popup.getPopupOpenness() && (!game_paused) && (!mgr.countdown.counting))
							setPause(true);
					}
			}
		}

		doPause(newPause);
	}
	
	@Override
	public void onRoomLoad() {
		if (ref.room.get_current_room() == rooms.ROOM_GAME){
			mgr.gameMain.shade_alpha = 1;
			mgr.gameMain.shade_alpha_target = 1;
		}
	}

	boolean startPause = false, newPause;
	
	public void setPause(boolean pause) {
		startPause = true;
		newPause = pause;
	}
	

	public void doPauseHard(boolean pause) {
		if(pause) {
			game_paused = true;
			ref.draw.captureDraw();
			ref.main.pauseEntities();
		} else {
			game_paused = false;
			ref.main.unPauseEntities();
		}
	}
	
	public void doPause(boolean pause) {
		if (startPause) {
			startPause = false;
			
			if ( (pause) && (mgr.gameMain.floor_height < ref.screen_height) ) {
				game_paused = true;
				ref.draw.captureDraw();
				ref.main.pauseEntities();
				// this is in an alarm so the just-starting-to-fade-in popup won't be captured with the pause image
				alarm[0] = ref.main.time_delta*2;
			} else {
				game_paused = false;
				ref.main.unPauseEntities();
			}
		}
	}
	
	@Override
	public void alarm0() {
		mgr.popup.setPopupState(mgr.popup.STATE_PAUSED);
		mgr.popup.setPopupOpenness(game_paused);
	}

	public void switchPause() {
		setPause(!game_paused);
	}
	
	public boolean getPause() {
		return game_paused;
	}
	
	
	@Override
	public void onScreenSleep() {
		if (ref.room.get_current_room() == rooms.ROOM_GAME) {
			if ( !getPause() || mgr.countdown.counting ) {
				doPauseHard(true);
				mgr.countdown.stopCountdown();
			}
		}
	}
	
	@Override
	public void onScreenWake() {
	    if(ref.room.get_current_room() == rooms.ROOM_GAME) {
	    	alarm[0] = ref.main.time_delta*2;
	    }
	}
	
}
