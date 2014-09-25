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
					if ( ! mgr.popup.getPopupOpenness() ) {
						mgr.popup.setPopupState(mgr.popup.STATE_PAUSED);
						mgr.popup.setPopupOpenness(true);
					} else {
					    if (mgr.popup.getPopupState() == mgr.popup.STATE_PAUSED)
					        mgr.popup.setPopupState(mgr.popup.STATE_ABANDON);
					    else if (mgr.popup.getPopupState() == mgr.popup.STATE_SETTINGS)
					        mgr.popup.setPopupState(mgr.popup.STATE_PAUSED);
					    else if (mgr.popup.getPopupState() == mgr.popup.STATE_ABANDON)
					        mgr.popup.abandonCurrentGame();
					}
					
				} else {
					if (!mgr.countdown.counting)
						mgr.menuPauseHUD.setPause(true);
				}
				break;
			case rooms.ROOM_POSTGAME:
				mgr.popup.setPopupOpenness(false);
				
				mgr.menuMain.setDefaultPositionHard();
				
				mgr.menuMain.start();
				break;
			case rooms.ROOM_DIFFICULTY:
				mgr.menuDifficulty.prepLeave(mgr.menuDifficulty.PREP_menuTop);
				break;
			case rooms.ROOM_MENUMAIN:
					if (mgr.menuMain.active_screen != mgr.menuMain.MAIN) {
						if (mgr.popup.getPopupOpenness()) {
							mgr.popup.setPopupOpenness(false);
						} else {
							mgr.menuMain.active_screen = mgr.menuMain.MAIN;
							mgr.menuMain.setRelativePositionTarget(-mgr.menuMain.X_MAIN, -mgr.menuMain.Y_MAIN);
						}
					} else {
						if (!mgr.popup.getPopupOpenness()) {
							mgr.popup.setPopupState(mgr.popup.STATE_QUIT);
							mgr.popup.setPopupOpenness(true);
						} else {
							if (mgr.popup.getPopupState() != mgr.popup.STATE_QUIT)
								mgr.popup.setPopupOpenness(false); // close the settings menu
							else
								mgr.popup.ref.main.exitApp();
						}
					}
				break;

			case rooms.ROOM_MENURECORDS:
				if ( mgr.popup.getPopupOpenness() )
					mgr.popup.setPopupOpenness(false);
				else 
					mgr.menuRecords.prepLeave(mgr.menuRecords.PREP_menuTop);
				
				break;
		}
	}
}
