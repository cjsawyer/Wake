package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.gameEngine.*;


public class entity_gameStateManager extends engine_entity {

	masterGameReference mgr;
	public entity_gameStateManager(masterGameReference mgr) {
		this.persistent = true;
		this.mgr = mgr;
	}
	
	@Override
	public void sys_step() {}
//	
//	public void pauseGame() {
//		mgr.gameMain.paused = true;
//		mgr.greenOrbSpawner.paused = true;
//		mgr.orbSpawnerState.paused = true;
//		mgr.hud.paused = true;
//		ref.main.pauseParticles();
//	}
//	
//	public void unPauseGame() {
//		mgr.gameMain.paused = false;
//		mgr.greenOrbSpawner.paused = false;
//		mgr.orbSpawnerState.paused = false;
//		mgr.hud.paused = false;
//		ref.main.unPauseParticles();
//	}
	
	
}
