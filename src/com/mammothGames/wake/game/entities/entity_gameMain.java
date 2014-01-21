package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.gameEngine.*;


public class entity_gameMain extends engine_entity {

	masterGameReference mgr;
	
	public entity_gameMain(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = true;
	}
	
	public float text_size;
	
	public int score_multiplier = 1;
	public int streak = 0;
	
	public float speed_multiplier = 1; // 1
	public float speed;
	
	private float speed_base;
	private float speed_max;
	
	public final float speed_gain_per_orb = 0.01f; // 0.01
	
	public float time_start_between_orbs = 333;
	public float time_minimum_between_orbs = 150;
	public float time_between_orbs = time_start_between_orbs; // in milliseconds
	public float time_between_orbs_double = time_between_orbs*2; // in milliseconds
	public final float time_change_per_orb = 0.3f; //2, in ms
	
	public int score;
	int high_score = 0;
	float floor_height;
	float floor_per_miss; 
	float floor_per_hit; 
	float floor_height_draw;
	
	public boolean new_high_score;
	
	public void restart() {
		score = 0;
		new_high_score = false;
		
		score_multiplier = 1;
		streak = 0;
		
		floor_height = 0;
		floor_height_draw = ref.screen_height; // For a nice little intro animation
		time_between_orbs = time_start_between_orbs;
		time_between_orbs_double = time_between_orbs*2;	
		speed_multiplier = 1;
	}
	
	@Override
	public void sys_firstStep() {
		
		// Load high score
		String high_score_string = ref.file.load("int_high_score");
		if (high_score_string.equals("")) {
//		if (high_score_string == null) {
			high_score = 0;
		} else {
			high_score = Integer.parseInt(ref.file.load("int_high_score")); 
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
		
		speed = speed_base * speed_multiplier;
		if (speed > speed_max) {
			speed = speed_max;
		}
		
		if (time_between_orbs < time_minimum_between_orbs) {
			time_between_orbs = time_minimum_between_orbs;
		}
		time_between_orbs_double = time_between_orbs*2;
		
		
		if (mgr.orbPatternMaker.state_finished) {
			
			if ((float)Math.random() > CHANCE_OF_LAST_STATE) {
				// Set a random one.
				state = (int) Math.round( Math.random() * (mgr.orbPatternMaker.num_states-1) );
			}
			mgr.orbPatternMaker.setState(state);
			
			// For testing custom ones
//			mgr.orbPatternMaker.setState(mgr.orbPatternMaker.STATE_2TIER_Z);
			
			
			
		}
		
		// Ease the floor line up
		floor_height_draw+= (floor_height - floor_height_draw) * 7 * ref.main.time_scale;
		
		ref.draw.setDrawColor(0.54f, 0.54f, 0.54f, 0.8f); // Floor color
		ref.draw.drawRectangle(0, 0, ref.screen_width, floor_height_draw, ref.screen_width/2, floor_height_draw/2, 0, game_constants.layer4_overGame);
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		ref.draw.drawLine(ref.screen_width, floor_height_draw, 0, floor_height_draw, ref.screen_width/25, game_constants.layer4_overGame);
		
		if (floor_height < 0)
			floor_height = 0;
		
		if (floor_height >= ref.screen_height)
			endGame();
	}
	
	public void restartGame() {
		ref.main.unPauseEntities();
		mgr.gameMain.restart();
		mgr.orbSpawner.restart();
		mgr.orbPatternMaker.restart();
		mgr.menuPause.restart();
		mgr.menuPostGame.restart();
	}
	
	public void endGame() {
		ref.draw.captureDraw();
		
		if(score > high_score) {
			high_score = score;
			new_high_score = true;
			ref.file.save("int_high_score", String.valueOf(high_score) );
		}
		
		ref.main.pauseEntities();
		ref.room.changeRoom(game_rooms.ROOM_POSTGAME);
	}
	
}