package com.mammothGames.wake.game.entities;

import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.gameEngine.*;


public class entity_backButton extends engine_entity {

	masterGameReference mgr;
	public entity_backButton(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = false;
	}
	
	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {}
	
	@Override
	public void backButton() {
		
		int room = ref.room.get_current_room();
		
		switch(room) {
			case game_rooms.ROOM_GAME: {
				
				mgr.menuPauseHUD.switchPause();
				break;
			}
			case game_rooms.ROOM_POSTGAME: {
				
				mgr.menuMain.goToMainMenu();
				break;
			}
			case game_rooms.ROOM_MENU: {
				
				ref.android.finish();
				break;
			}
			case game_rooms.ROOM_DIFFICULTY: {
				mgr.menuDifficulty.goToMainMenu();
				break;
			}
		}
		
	}
}
