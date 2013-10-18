package com.reywas.testGameName.game.entities.wake;

import android.util.Log;

import com.reywas.testGameName.gameEngine.*;


public class entity_orbSpawnerState extends engine_entity {
	
	//TODO TODO TODO TODO TODO TODO TODO//
	//////////////////////////////////////
	//edit this when adding a new state!//
	public int num_states = 5;
	//////////////////////////////////////
	//TODO TODO TODO TODO TODO TODO TODO//

	masterGameReference mgr;
	public entity_orbSpawnerState(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = true;
		this.mgr = mgr;
	}
	
	public boolean state_finished = true;
	
	private int state = -1;
	
	public static final int STATE_STRIPE_RIGHT = 0;
	public static final int STATE_STRIPE_LEFT = 1;
	public static final int STATE_STRIPE_HELIX = 2;
	
	public static final int STATE_2TIER_S = 3;
	public static final int STATE_2TIER_Z = 4;
	
	private int current_position;
	private int number_positions = 4;
	
	private boolean spawn_orb;
	private float width;
	private float oneWidth;
	
	private boolean use_double_spawn_time = false;
	private boolean four_step_pattern; // 2 step is false, 4 step is true
	
	public void restart() {
		
		state = -1;
		
		// So it starts the state the same way as if it has just finished one
		current_position = number_positions;
		spawn_orb = false;
		
	}
	
	@Override
	public void sys_firstStep() {
		width = ref.main.get_screen_width();
		oneWidth = width/number_positions;
	}
	
	@Override
	public void sys_step() {
		
		
		
//		ref.draw.setDrawColor(1, 1, 1, 1);
//		for(int i=0; i<number_orbs; i++) {
//			float x = i*oneWidth + oneWidth/2;
//			ref.draw.drawCircle(x, r*2, r, 0, 0, 360, 0, 2);
//		}
		
		//MEEE
//		if (false)
		
		
		switch(state) {
			
			case STATE_STRIPE_RIGHT: {
				if (spawn_orb) {
					spawn_orb = false;
					mgr.masterOrbSpawner.spawnGreenOrb(
							// x
							(current_position-1)*oneWidth + oneWidth/2,
//							(   (  ( ref.main.get_screen_width() - mgr.masterOrbSpawner.radius_total*2 ) / (STRIPE_number_positions-1)   ) * STRIPE_position   ) + mgr.masterOrbSpawner.radius_total,
							//y
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							//speed
							mgr.gameMain.speed );
				}
				break;
			}
			
			case STATE_STRIPE_LEFT: {
				if (spawn_orb) {
					spawn_orb = false;
					mgr.masterOrbSpawner.spawnGreenOrb(
							// x
							-( (current_position-1)*oneWidth + oneWidth/2 ) + width,
							//(   -(  ( ref.main.get_screen_width() - mgr.masterOrbSpawner.radius_total*2 ) / (STRIPE_number_positions-1)   ) * (STRIPE_position-1)   ) - mgr.masterOrbSpawner.radius_total + ref.main.get_screen_width(),
							//y
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							//speed
							mgr.gameMain.speed );
				}
				break;
			}
			case STATE_STRIPE_HELIX: {
				if (spawn_orb) {
					spawn_orb = false;
					//right 
					mgr.masterOrbSpawner.spawnGreenOrb(
							//x 
							(current_position-1)*oneWidth + oneWidth/2,
							//(   (  ( ref.main.get_screen_width() - mgr.masterOrbSpawner.radius_total*2 ) / (STRIPE_number_positions-1)   ) * (STRIPE_position-1)   ) + mgr.masterOrbSpawner.radius_total,
							//y
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							//speed
							mgr.gameMain.speed );
					//left
					mgr.masterOrbSpawner.spawnGreenOrb(
							// x
							-( (current_position-1)*oneWidth + oneWidth/2 ) + width,
							//y
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							//speed
							mgr.gameMain.speed );
				}
				break;
			}
			
			case STATE_2TIER_S: {
				if (spawn_orb) {
					spawn_orb = false;
					
					mgr.masterOrbSpawner.spawnGreenOrb(
							(current_position-2)*oneWidth + oneWidth/2,
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							mgr.gameMain.speed );
					
					mgr.masterOrbSpawner.spawnGreenOrb(
							(current_position-1)*oneWidth + oneWidth/2,
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							mgr.gameMain.speed );
				}
				break;
			}
			
			case STATE_2TIER_Z: {
				if (spawn_orb) {
					spawn_orb = false;
					
					mgr.masterOrbSpawner.spawnGreenOrb(
							-( (current_position-2)*oneWidth + oneWidth/2 ) + width,
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							mgr.gameMain.speed );
					
					mgr.masterOrbSpawner.spawnGreenOrb(
							-( (current_position-1)*oneWidth + oneWidth/2 ) + width,
							ref.main.get_screen_height() + mgr.masterOrbSpawner.radius_total,
							mgr.gameMain.speed );
				}
				break;
			}
			
		}
	}
	
	public void setState(int state) {
		this.state = state;
		state_finished = false;
		
		
		switch (state) {
			case(STATE_STRIPE_RIGHT): {
				use_double_spawn_time = false;
				four_step_pattern = true;
				break;
			}
			case(STATE_STRIPE_LEFT): {
				use_double_spawn_time = false;
				four_step_pattern = true;
				break;
			}
			case(STATE_STRIPE_HELIX): {
				use_double_spawn_time = true;
				four_step_pattern = true;
				break;
			}
			case(STATE_2TIER_S): {
				use_double_spawn_time = true;
				four_step_pattern = false;
				break;
			}
			case(STATE_2TIER_Z): {
				use_double_spawn_time = true;
				four_step_pattern = false;
				break;
			}
		}
		
		alarm[0] = mgr.gameMain.time_between_orbs;
	}
	
	
	@Override
	public void alarm0() {
		
		if (current_position == number_positions) {
			alarm[0] = -1;
			current_position = 0;
			state_finished = true;
		} else {
			
			if(four_step_pattern)
				current_position++;
			else
				current_position += 2;
			
			spawn_orb = true;
			
			if ( (use_double_spawn_time) && (current_position != number_positions) ) {
				alarm[0] = mgr.gameMain.time_between_orbs_double;
			} else {
				alarm[0] = mgr.gameMain.time_between_orbs;
			}
			
		}
//		Log.e("eaedsd", STRIPE_position + " " + alarm[0]);
		
	}
}
