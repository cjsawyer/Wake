package com.mammothGames.wake1.blanks;

import com.mammothGames.wake1.gameEngine.*;


public class entBLANK_pool extends engine_entity {

	utility_pool<entBLANK_poolObj> poolObj_pool;
	entBLANK_poolObj tmp_obj;
	
	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {
		updateAllPoolObj();
	}
	
	private void poolObjLogic(){
		// TODO: Put logic for an individual poolObj "tmp_obj" here. 
	}
	
	// Do not modify!
	private void updateAllPoolObj() {
		// You don't need to change anything here.
		for(int i = 0; i < poolObj_pool.MAX_OBJECTS; i++) {
			tmp_obj = poolObj_pool.getInstance(i);
			if ((tmp_obj.sys_in_use)) {
				poolObjLogic();
			}
		}
	}
}