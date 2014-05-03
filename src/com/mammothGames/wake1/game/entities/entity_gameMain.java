package com.mammothGames.wake1.game.entities;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.gameEngine.*;


public class entity_gameMain extends engine_entity {

	masterGameReference mgr;
	public entity_gameMain(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = false;
	}
	
	float padding_x, padding_y;
	
	public float shade_alpha = 0;
	public float shade_alpha_target = 1;
	float ANIMATION_SCALE = 7f;
	
	public static int DIF_EASY = 250;
	public static int DIF_MEDIUM = 625;
	public static int DIF_HARD = 1000;
	public static int DIF_HELL = 1500;
	public int current_diff;
	public String current_diff_string;
	
	public float text_size;
	
	// streak is in internal var for number of orbs tapped in a row for the score multiplier
	// points_streak is number of points scored in a row, and what the "streak" score counter tracks.
	public final int STREAK_PER_LEVEL = 20;
	public int score_multiplier = 1;
	public int streak = 0;
	public int best_points_streak_this_game = 0;
	public int best_points_streak = 0;
	public int points_streak = 0;
	
	public float speed_multiplier = 1; // 1
	public float speed;
	
	private float speed_base;
	private float speed_max;
	
	public float speed_gain_per_orb;//0.01
	
	public float time_start_between_orbs = 500;//333
	public float time_minimum_between_orbs = 85;//100
	public float time_between_orbs; // in milliseconds
	public float time_change_per_orb; //0.3, in ms
	
	public int score;
	int high_score = 0;
	float floor_height_target;
	float floor_per_miss; 
	float floor_per_hit; 
	float floor_height;
	
	public boolean new_high_score, new_best_streak;
	
	public void restart() {
//		target_shade_alpha = 0;
		
		score = 0;
		new_high_score = false;
		new_best_streak = false;
		
		score_multiplier = 1;
		streak = 0;
		best_points_streak_this_game = 0;
		points_streak = 0;
		
		floor_height_target = 0;
		floor_height = 0;
		time_between_orbs = time_start_between_orbs;
		speed_multiplier = 1;
	}
	
	@Override
	public void sys_firstStep() {
		
		padding_x = ref.screen_width/10;
		padding_y = padding_x * 3;
		
		shade_alpha = -4;
		shade_alpha_target = 1;
		
		
		floor_per_miss = ref.screen_height/9;
		floor_per_hit = floor_per_miss/7;
		
		speed_base = ref.screen_height/3.5f;
		speed_max = ref.screen_height/0.75f;
		speed_gain_per_orb = ref.screen_height * 0.000001f;
		
		time_change_per_orb = -(time_minimum_between_orbs-time_start_between_orbs)/DIF_HELL; 
				
		text_size = ref.screen_width/12;
		
	}
	
	private int state;
	private final float CHANCE_OF_LAST_STATE = 0.25f;
	private final float CHANCE_OF_SISTER_STATE = 0.5f;
	
	@Override
	public void sys_step(){
		
		// The shade alpha towards it's target; and if we're in the menu room only slide if the water height is at the bottom
		if (ref.room.get_current_room() == rooms.ROOM_MENUFIRST) {
			if ( (mgr.gameMain.floor_height < 2) && (mgr.stars.stars_alpha > 0.98f) )
				mgr.gameMain.shade_alpha += (mgr.gameMain.shade_alpha_target - mgr.gameMain.shade_alpha) * ANIMATION_SCALE * ref.main.time_scale;
		}
		else
			mgr.gameMain.shade_alpha += (mgr.gameMain.shade_alpha_target - mgr.gameMain.shade_alpha) * ANIMATION_SCALE * ref.main.time_scale;
		
		speed = speed_base * speed_multiplier;
		if (speed > speed_max) {
			speed = speed_max;
		}
		
		if (time_between_orbs < time_minimum_between_orbs) {
			time_between_orbs = time_minimum_between_orbs;
		}
		
		if (points_streak > best_points_streak_this_game) {
			best_points_streak_this_game = points_streak;
		}
		
		
		if ( (mgr.orbPatternMaker.state_finished) && (ref.room.get_current_room() == rooms.ROOM_GAME) ) {
			
			if ((float)Math.random() > CHANCE_OF_LAST_STATE) {

				state = mgr.orbPatternMaker.getRandomState();
				
			} else if ((float)Math.random() > CHANCE_OF_SISTER_STATE) {
				
				state = mgr.orbPatternMaker.getSisterState(state);
				
			}
			mgr.orbPatternMaker.setState(state);
			
		}
		
		// Ease the floor line up
		if (!mgr.menuPauseHUD.getPause())
			floor_height+= (floor_height_target - floor_height) * 7 * ref.main.time_scale;

		// Draw floor/water line
		ref.draw.setDrawColor(0.54f, 0.54f, 0.54f, 0.8f); // Floor color
		ref.draw.drawRectangle(0,0, ref.screen_width, floor_height, -ref.screen_width/2, -floor_height/2, 0, constants.layer4_overGame);
		
		if (floor_height_target < 0)
			floor_height_target = 0;
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		ref.draw.drawLine(ref.screen_width, floor_height, 0, floor_height, ref.screen_width/25, constants.layer4_overGame);
		
		
		if ( (floor_height >= ref.screen_height) && (ref.room.get_current_room() == rooms.ROOM_GAME) )
			endGame();
		
		
	}
	
	final String SCO_E = "SCO_E";
	final String SCO_M = "SCO_M";
	final String SCO_H = "SCO_H";
	final String SCO_HE = "SCO_HE";
	
	final String STR_E = "STR_E";
	final String STR_M = "STR_M";
	final String STR_H = "STR_H";
	final String STR_HE = "STR_HE";
	
	final String PLY_E = "PLY_E";
	final String PLY_M = "PLY_M";
	final String PLY_H = "PLY_H";
	final String PLY_HE = "PLY_HE";
	
	private String STR, SCO, PLY;
	
	public void setDifficulty(int diff) {
		current_diff = diff;

		
		if(diff == DIF_EASY) {
			current_diff_string = "EASY";
			SCO  = SCO_E;
			STR  = STR_E;
			PLY = PLY_E;
		}
		if(diff == DIF_MEDIUM) {
			current_diff_string = "MEDIUM";
			SCO  = SCO_M;
			STR  = STR_M;
			PLY = PLY_M;
		}
		if(diff == DIF_HARD) {
			current_diff_string = "HARD";
			SCO  = SCO_H;
			STR  = STR_H;
			PLY = PLY_H;
		}
		if(diff == DIF_HELL) {
			current_diff_string = "HELL";
			mgr.stars.red_alpha_target = 0.8f;
			SCO  = SCO_HE;
			STR  = STR_HE;
			PLY = PLY_HE;
		}
		else
			mgr.stars.red_alpha_target = 0;
		
		
		// Load high scores
		String high_score_string = ref.file.load(SCO);
		if (high_score_string.equals("")) {
			high_score = 0;
		} else {
			high_score = Integer.parseInt(ref.file.load(SCO)); 
		}
		String best_streak_string = ref.file.load(STR);
		if (best_streak_string.equals("")) {
			best_points_streak = 0;
		} else {
			best_points_streak = Integer.parseInt(ref.file.load(STR)); 
		}
		
		
		
	}
	
	public void startGame() {
		ref.room.changeRoom(rooms.ROOM_GAME);
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
			ref.file.save(SCO, String.valueOf(high_score) );
		}
		if(best_points_streak_this_game > best_points_streak) {
			best_points_streak = best_points_streak_this_game;
			new_best_streak = true;
			ref.file.save(STR, String.valueOf(best_points_streak) );
		}
		
		
		int games;
		String games_string = ref.file.load(PLY);
		if (games_string.equals("")) {
			games = 0;
		} else {
			games = Integer.parseInt(ref.file.load(PLY)); 
		}
		ref.file.save(PLY, String.valueOf(games+1) );
		
		
		
//		floor_height_target = 0; // make floor sink
		mgr.gameMain.shade_alpha = -2; // so there's an offset before you can start to see it while fading in
		mgr.gameMain.shade_alpha_target = 1; // fade in post game text
		mgr.menuPauseHUD.HUD_y_target = mgr.menuPauseHUD.base_hud_height*2; // slide HUD back up
		mgr.menuPauseHUD.setPause(false);
		
		mgr.menuPostGame.start();
	}
	
}