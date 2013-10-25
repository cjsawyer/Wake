package com.mammothGames.wake.gameEngine;


import com.mammothGames.wake.game.*;

import java.text.DecimalFormat;
import java.util.Random;

import android.util.Log;

public class engine_main {

	engine_reference ref;


	//COUNTERS//
	protected int entities_current = 0;
	protected int entities_total = 0;

	//TEMPS//
	private engine_entity temp_entity;
	private int temp_i_steps;


	private engine_entity[] entity_list;
	private int entity_list_length = 100;
	


	public engine_main(engine_reference r){
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		ref = r;

		entity_list = new engine_entity[entity_list_length];

		ref.room = new engine_room(ref);
		ref.strings = new engine_strings(ref);
		ref.partPool = new engine_particlePool(ref);
		ref.input = new engine_input(ref);
		ref.hud = new engine_hud(ref);
		ref.collision = new engine_collision(ref);
//		ref.back = new game_physicalButtons(ref);
		ref.sound = new engine_sound(ref);
		ref.file = new engine_file(ref);
		ref.ad = new engine_adMob(ref);
		ref.matrix = new engine_gl_matrix(ref);
		ref.loadHelper = new engine_loadHelper();
	}
	
	


	public int addEntity(engine_entity ge){


		ge.id = entities_total;

		entity_list[entities_total] = ge;

		entities_current += 1;
		entities_total +=1;

		if (entity_list.length == entities_total) {
			sys_checkIfEntityListIsFull();
		}

		ge.sys_initiate_entity(ref);
		
		if ( (gamePaused) && (ge.pausable) ) {
			ge.paused = true;
		}

		return entities_total-1;
	}


	protected engine_entity getEntityFromID(int id){
		return entity_list[id];
	}



	private void sys_entitySteps(){
		for(temp_i_steps = 0; temp_i_steps < entities_total; temp_i_steps++){
 
			temp_entity = null;
			temp_entity = entity_list[temp_i_steps];

			if (temp_entity != null){
				
				// First steps
				if (temp_entity.sys_first_step_executed == false){
					temp_entity.sys_firstStep();
					temp_entity.sys_first_step_executed = true;
				}
				

				// Hardware buttons 
				if (backButton) {
					temp_entity.backButton();
				}
				if (menuButton) {
					temp_entity.menuButton();
				}
				
				// Main steps, also ticks alarms
				if ((temp_entity.paused == false)) {
					
					
					temp_entity.sys_step();
					temp_entity.alarmStep();
					sys_cleanDeletedEntities();
				}
			}
		}
		
		
		if (pauseEntities) {
			isPaused = true;
			sys_pauseEntities();
		}
		pauseEntities = false;
		
		if (unPauseEntities) {
			isPaused = false;
			sys_unPauseEntities();
		}
		unPauseEntities = false;
		
		
		backButton = false;
		menuButton = false;

		sys_checkIfEntityListIsFull();
	}


//	private void sys_entityBeforeSteps(){
//		for(temp_i_steps = 0; temp_i_steps < entities_total; temp_i_steps++){
//			temp_entity = null;
//			temp_entity = entity_list[temp_i_steps];
//			if ((temp_entity != null) && ( temp_entity.paused == false)){
//				temp_entity.sys_beforeStep();
//			}
//			sys_cleanDeletedEntities();
//		}
//	}
//	private void sys_entityAfterSteps(){
//		for(temp_i_steps = 0; temp_i_steps < entities_total; temp_i_steps++){
//
//			temp_entity = null;
//			temp_entity = entity_list[temp_i_steps];
//
//			if ((temp_entity != null) && ( temp_entity.paused == false)){
//				temp_entity.sys_afterStep();
//			}
//			sys_cleanDeletedEntities();
//		}
//	}
	private void sys_cleanDeletedEntities(){

		temp_entity = null;
		temp_entity = entity_list[temp_i_steps];

		if (temp_entity != null){
			if (temp_entity.sys_delete_me == true){

				entity_list[temp_i_steps] = null;

				temp_entity = null;
				entities_current -= 1;
			}
		}
	}
	public void cleanDeletedEntitiesStepIndependent(){
		for(int i = 0; i < entities_total; i++){

			temp_entity = null;
			temp_entity = entity_list[i];

			if (temp_entity != null){
				if (temp_entity.sys_delete_me == true){

					entity_list[i] = null;

					temp_entity = null;
					entities_current -= 1;
				}
			}
		}
	}

	protected void deleteAllNonPersistentEntities(){
		for(temp_i_steps = 0; temp_i_steps < entities_total; temp_i_steps++){

			temp_entity = null;
			temp_entity = entity_list[temp_i_steps];

			if ( (temp_entity != null) && (temp_entity.persistent == false) ){
				temp_entity.instance_destroy();
			}
		}
	}
	/*public void deleteAllEntities(){
		for(temp_i_steps = 0; temp_i_steps < entities_total; temp_i_steps++){
			temp_entity = entity_map.get(temp_i_steps);
			if (temp_entity != null){
				temp_entity.instance_destroy();
			}
		}
	}*/

	private int temp_i = 0;
	private int temp_last_id = 0;
	private int temp_entities_added_to_list = 0;

	public void sys_condenseEntityList(){

		temp_entities_added_to_list = 0;
		temp_last_id = -1;

		for(temp_i=0; temp_i<entities_total; temp_i++){

			temp_entity = entity_list[temp_i];

			if (temp_entity != null){
				if (temp_entity.id != temp_last_id){

					temp_last_id = temp_entity.id;

					entity_list[temp_entities_added_to_list] = temp_entity;
					temp_entity.id = temp_entities_added_to_list;
					temp_entities_added_to_list+=1;

				}
			}
			temp_entity = null;
		}

		entities_total = temp_entities_added_to_list;

	}

	private void sys_checkIfEntityListIsFull(){

		entity_list_length = entity_list.length;

		// if more than half full, condense the list
		if (entities_total > entity_list_length - (entity_list_length/2)){

			sys_condenseEntityList();
			entity_list_length = entity_list.length;
			// if still more than half full, expand the list to 1.5 current size
			if (entities_total > entity_list_length - (entity_list_length/2)){

				engine_entity[] new_list = new engine_entity[entity_list_length + entity_list_length/2];
				System.arraycopy(entity_list, 0, new_list, 0, entity_list_length);
				entity_list = new_list;
				entity_list_length = entity_list_length + entity_list_length/2;
				if(game_constants.devmode) {
					Log.e("reywas","Expanded entity_list to length of: " + entity_list_length);
				}
				new_list = null;

			}
		}
	}


	public float time_delta = -1;
	public float time_scale = -1;
//	public boolean first_step_executed = false;

	public void sys_update_delta_time(){
		time_delta = ref.renderer.sys_delta_time;
		time_scale = time_delta/1000.0f;
		//first_step_executed = ref.renderer.sys_first_step_executed;

	}


	public int get_screen_height(){ return Math.round(ref.renderer.screen_height); }
	public int get_screen_width(){ return Math.round(ref.renderer.screen_width); }

	
	
	protected void onScreenSleep() {
		for(int i = 0; i < entities_total; i++){

			temp_entity = null;
			temp_entity = entity_list[i];

			if (temp_entity != null){
				temp_entity.onScreenSleep();
			}
		}
	}
	
	
	/// PAUSING ///
	private engine_entity temp_entity_pause;
	private boolean gamePaused = false, pauseEntities, unPauseEntities;
	
	private void sys_pauseEntities() {
		gamePaused = true;
		
		for(int i=0; i<entities_total; i++){
			temp_entity_pause = null;
			temp_entity_pause = entity_list[i];
			if ( (temp_entity_pause != null) && (temp_entity_pause.pausable==true) ){
				temp_entity_pause.paused = true;
			}
		}
	}
	
	private void sys_unPauseEntities() {
		gamePaused = false;
		
		for(int i=0; i<entities_total; i++){
			temp_entity_pause = null;
			temp_entity_pause = entity_list[i];
			if ( (temp_entity_pause != null) && (temp_entity_pause.pausable==true) ){
				temp_entity_pause.paused = false;
			}
		}
	}
	
	public void pauseEntities() {
		pauseEntities = true;
	}
	public void unPauseEntities() {
		unPauseEntities = true;
	}
	private boolean isPaused = false;
	public boolean getPauseState() {
		return isPaused;
	}
	/// END PAUSING ///
	
	
	
	
	private boolean is_first_step = true;

	public void sys_gameLoop(){

		/*if (entities_total > entity_list_length-1000){
			sys_condenseEntityList();
		}*/
		if (is_first_step) {
			ref.hud.initiate();
			is_first_step = false;
		}
		
		if (game_constants.devmode){
			ref.hud.update();
		}
		
		ref.loadHelper.checkFinished();
		
		ref.android.sys_updateTouchDurations();
		
//		sys_entityBeforeSteps();
		sys_entitySteps();
//		sys_entityAfterSteps();
		

		ref.room.sys_moveRooms();
		
		//LAG FOR TESTINGG!!
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private boolean backButton = false, menuButton = false; 
	protected void handleBackButton() {
		backButton = true;
	}
	
	protected void handleMenuButton() {
		menuButton = true;
	}
	
	
	Random random = new Random();
	public float randomRange(float min, float max){
		return ( random.nextFloat() * (max-min) ) + min;
	}

}