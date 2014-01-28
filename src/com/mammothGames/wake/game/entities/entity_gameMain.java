package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.gameEngine.*;


public class entity_gameMain extends engine_entity {

	masterGameReference mgr;
	public entity_gameMain(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = false;
	}
	
	public float shade_alpha = 0;
	public float shade_alpha_target = 1;
	
	//TODO: blaance these values
	public final int DIF_EASY = 0;
	public final int DIF_MEDIUM = 100;
	public final int DIF_HARD = 300;
	public final int DIF_HELL = 1000;
	public boolean hell_unlocked = false;
	public int current_diff;
	
	public float text_size;
	
	public final int STREAK_PER_LEVEL = 20;
	public int score_multiplier = 1;
	public int streak = 0;
	
	public float speed_multiplier = 1; // 1
	public float speed;
	
	private float speed_base;
	private float speed_max;
	
	public final float speed_gain_per_orb = 0.01f; // 0.01
	
	public float time_start_between_orbs = 333;
	public float time_minimum_between_orbs = 100;
	public float time_between_orbs = time_start_between_orbs; // in milliseconds
	public float time_between_orbs_double = time_between_orbs*2; // in milliseconds
	public final float time_change_per_orb = 0.3f; //2, in ms
	
	public int score;
	int high_score = 0;
	float floor_height_target;
	float floor_per_miss; 
	float floor_per_hit; 
	float floor_height;
	
	public boolean new_high_score;
	
	public void restart() {
//		target_shade_alpha = 0;
		
		score = 0;
		new_high_score = false;
		
		score_multiplier = 1;
		streak = 0;
		
		floor_height_target = 0;
		floor_height = 0;
		time_between_orbs = time_start_between_orbs;
		time_between_orbs_double = time_between_orbs*2;	
		speed_multiplier = 1;
	}
	
	@Override
	public void sys_firstStep() {
		shade_alpha = -4;
		shade_alpha_target = 1;
		
		//TODO: change this from a menu
		current_diff = DIF_MEDIUM;
		
		// Load high score
		String high_score_string = ref.file.load("high_score");
		if (high_score_string.equals("")) {
//		if (high_score_string == null) {
			high_score = 0;
		} else {
			high_score = Integer.parseInt(ref.file.load("high_score")); 
		}
		
		floor_per_miss = ref.screen_height/9;
		floor_per_hit = floor_per_miss/7;
		
		speed_base = ref.screen_height/3;
		speed_max = ref.screen_height/1.05f;
		text_size = ref.screen_width/12;
	}
	
	private int state;
	private final float CHANCE_OF_LAST_STATE = 0.25f;
	
	@Override
	public void sys_step(){
		
		// The shade alpha towards it's target; and if we're in the menu room only slide if the water height is at the bottom
		if (ref.room.get_current_room() == game_rooms.ROOM_MENU) {
			if ( (mgr.gameMain.floor_height < 2) && (mgr.stars.stars_alpha > 0.98f) )
				mgr.gameMain.shade_alpha += (mgr.gameMain.shade_alpha_target - mgr.gameMain.shade_alpha) * 5f * ref.main.time_scale;
		}
		else
			mgr.gameMain.shade_alpha += (mgr.gameMain.shade_alpha_target - mgr.gameMain.shade_alpha) * 5f * ref.main.time_scale;
		
		speed = speed_base * speed_multiplier;
		if (speed > speed_max) {
			speed = speed_max;
		}
		
		if (time_between_orbs < time_minimum_between_orbs) {
			time_between_orbs = time_minimum_between_orbs;
		}
		time_between_orbs_double = time_between_orbs*2;
		
		
		if ( (mgr.orbPatternMaker.state_finished) && (ref.room.get_current_room() == game_rooms.ROOM_GAME) ) {
			
			if ((float)Math.random() > CHANCE_OF_LAST_STATE) {
				// Set a random one.
				state = (int) Math.round( Math.random() * (mgr.orbPatternMaker.num_states-1) );
			}
			mgr.orbPatternMaker.setState(state);
			
		}
		
		// Ease the floor line up
		floor_height+= (floor_height_target - floor_height) * 7 * ref.main.time_scale;

		// Draw floor/water line
		ref.draw.setDrawColor(0.54f, 0.54f, 0.54f, 0.8f); // Floor color
		ref.draw.drawRectangle(0,0, ref.screen_width, floor_height, -ref.screen_width/2, -floor_height/2, 0, game_constants.layer4_overGame);
		
		if (floor_height_target < 0)
			floor_height_target = 0;
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		ref.draw.drawLine(ref.screen_width, floor_height, 0, floor_height, ref.screen_width/25, game_constants.layer4_overGame);
		
		
		if ( (floor_height >= ref.screen_height) && (ref.room.get_current_room() == game_rooms.ROOM_GAME) )
			endGame();
		
		
	}
	
	public void setDifficulty(int diff) {
		current_diff = diff;
	}
	
	public void startGame() {
		ref.room.changeRoom(game_rooms.ROOM_GAME);
		ref.main.unPauseEntities();
		mgr.gameMain.restart();
		mgr.orbSpawner.restart(current_diff);
		mgr.orbPatternMaker.restart();
		mgr.menuPauseHUD.restart();
		mgr.menuPostGame.restart();
	}
	
	public void endGame() {
//		ref.draw.captureDraw();
		
		if(score > high_score) {
			high_score = score;
			new_high_score = true;
			ref.file.save("high_score", String.valueOf(high_score) );
		}
		
//		floor_height_target = 0; // make floor sink
		mgr.gameMain.shade_alpha = -2; // so there's an offset before you can start to see it while fading in
		mgr.gameMain.shade_alpha_target = 1; // fade in post game text
		mgr.menuPauseHUD.HUD_y_target = mgr.menuPauseHUD.base_hud_height*2; // slide HUD back up
		
//		ref.main.pauseEntities();
		ref.room.changeRoom(game_rooms.ROOM_POSTGAME);
	}
	
}