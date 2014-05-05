package com.mammothGames.wake1.game.entities;

import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.gameEngine.*;


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
			case rooms.ROOM_LOAD:
				// Quit the game if we're in the loading sequence
				ref.main.exitApp();
			case rooms.ROOM_MENUFIRST:
				ref.main.exitApp();
				break;
			case rooms.ROOM_GAME:
				if (mgr.menuPauseHUD.getPause()) {
					
					if ( ! mgr.areYouSure.getPopupOpenness() ) {
						mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_ABANDON);
						mgr.areYouSure.setPopupOpenness(true);
					} else {
						mgr.menuPauseHUD.setPause(false);
						mgr.areYouSure.buttonAction(true);
					}
					
				} else {
					mgr.menuPauseHUD.setPause(true);
				}
				break;
			case rooms.ROOM_POSTGAME:
				mgr.areYouSure.setPopupOpenness(false);
				mgr.menuMain.start();
				break;
			case rooms.ROOM_DIFFICULTY:
				mgr.menuDifficulty.prepLeave(mgr.menuDifficulty.PREP_menuTop);
				break;
			case rooms.ROOM_MENUMAIN:
				if ( ! mgr.areYouSure.getPopupOpenness() ) {
					
					mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_TEST);
//	TODO: put this back				
//					mgr.areYouSure.setPopupAction(mgr.areYouSure.STATE_QUIT);
					mgr.areYouSure.setPopupOpenness(true);
				} else {
					mgr.areYouSure.buttonAction(true);
				}
//				mgr.menuMain.prepLeave(mgr.menuMain.PREP_menuMain);
				break;

			case rooms.ROOM_MENURECORDS:
				if ( mgr.areYouSure.getPopupOpenness() )
					mgr.areYouSure.setPopupOpenness(false);
				else 
					mgr.menuRecords.prepLeave(mgr.menuRecords.PREP_menuTop);
				
				break;
				
			case rooms.ROOM_MENUOPTIONS:
				mgr.menuOptions.prepLeave(mgr.menuOptions.PREP_menuTop);
				break;
				
			case rooms.ROOM_MENUABOUT:
				mgr.menuAbout.prepLeave(mgr.menuAbout.PREP_menuTop);
				break;
		}
		
	}
}
