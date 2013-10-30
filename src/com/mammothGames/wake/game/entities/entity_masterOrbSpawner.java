package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.gameEngine.*;


public class entity_masterOrbSpawner extends engine_entity {

	masterGameReference mgr;
	
	public entity_masterOrbSpawner(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		this.mgr = mgr;
	}
	
	@Override
	public void sys_firstStep(){
		radius = (ref.main.get_screen_width()/10);
		border_size = radius/10;
		radius_total = radius + border_size;
	}
	
	
	
	protected int radius, border_size, radius_total;
	
	@Override
	public void sys_step(){
	}
	
	public void spawnGreenOrb(float x, float y, float speed) {
		mgr.greenOrbSpawner.spawnGreenOrb(x,y,speed,radius,border_size);
	}
}
