package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.gameEngine.*;


public class entity_areYouSure extends engine_entity {

	public boolean are_you_sure;
	
	
	masterGameReference mgr;
	public entity_areYouSure(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;

		this.mgr = mgr;
		
		are_you_sure = false;
	}
	
	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {}
	
	
	public void setPopupState(boolean openOrClosed) {
		are_you_sure = openOrClosed;
	}
	public boolean getPopupState() {
		return are_you_sure;
	}
}
