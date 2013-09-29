package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.gameEngine.*;


public class entity_castTest extends engine_entity {

	Double doub = 12345.789132456;
	Integer integer;
	
	double doub2 = 12345.789132456;
	int integer2;
	
	@Override
	public void sys_firstStep(){}
	
	@Override
	public void sys_beforeStep(){}
	
	@Override
	public void sys_step(){
		
//		for(temp_i = 0; temp_i<10000; temp_i++) {
//			integer = doub.intValue();
//		}
		
		
		//less alocations.
		for(int temp_i = 0; temp_i<10000; temp_i++) {
			integer2 = (int) doub2;
		}
		
	}
	
	@Override
	public void sys_afterStep(){}
	
}
