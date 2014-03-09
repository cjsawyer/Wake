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
			case game_rooms.ROOM_LOAD:
			case game_rooms.ROOM_MENUFIRST:
				// Quit the game if we're in the loading sequence
				ref.main.exitApp();
				break;
			case game_rooms.ROOM_GAME:
				if (mgr.menuPauseHUD.getPause()) {
					
					if ( ! mgr.areYouSure.getPopupOpenness() ) {
						mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_ABANDON);
						mgr.areYouSure.setPopupOpenness(true);
					} else {
						mgr.areYouSure.buttonAction(false);
					}
					
				} else {
					mgr.menuPauseHUD.setPause(true);
				}
				break;
			case game_rooms.ROOM_POSTGAME:
				mgr.menuMain.start();
				break;
			case game_rooms.ROOM_DIFFICULTY:
				mgr.menuDifficulty.prepLeave(mgr.menuDifficulty.PREP_menuTop);
				break;
			case game_rooms.ROOM_MENUMAIN:
				if ( ! mgr.areYouSure.getPopupOpenness() ) {
					mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_QUIT);
					mgr.areYouSure.setPopupOpenness(true);
				} else {
					mgr.areYouSure.buttonAction(false);
				}
//				mgr.menuMain.prepLeave(mgr.menuMain.PREP_menuMain);
				break;

			case game_rooms.ROOM_MENURECORDS:
				mgr.menuRecords.prepLeave(mgr.menuRecords.PREP_menuTop);
				break;
				
			case game_rooms.ROOM_MENUOPTIONS:
				mgr.menuOptions.prepLeave(mgr.menuOptions.PREP_menuTop);
				break;
				
			case game_rooms.ROOM_MENUABOUT:
				mgr.menuAbout.prepLeave(mgr.menuAbout.PREP_menuTop);
				break;
		}
		
	}
}
